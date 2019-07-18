package com.abaka.core.dao.impl;

import com.abaka.core.dao.DataSourceFactory;
import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.model.Condition;
import com.abaka.core.model.FileType;
import com.abaka.core.model.Thing;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexDaoImpl implements FileIndexDao {

    //DataSource.getInstance 通过数据源工厂获取DataSource实例化对象

    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        //JDBC操作

        //准备连接数据库的连接
        Connection connection = null;
        //准备命令
        PreparedStatement statement = null;
        try {
            //1.获取数据库连接
            connection = this.dataSource.getConnection();
            //2.准备SQL语句 insert into file_index
            String sql = "insert into file_index(name, path, depth, file_type) values(?,?,?,?)";
            //3.准备命令
            statement = connection.prepareStatement(sql);
            //4.设置参数1 2 3 4  就是要存储的信息
            //预编译命令中SQL的占位符赋值
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            //FileType.DOC -> DOC   获取枚举中的属性
            statement.setString(4,thing.getFileType().name());
            //5.执行命令
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //5.关闭连接
            releaseResource(null, statement, connection);
        }
    }

    @Override
    public void delete(Thing thing) {
        //thing -> path =>  D:\a\b\hello.java
        //thing -> path =>  D:\a\b
        //                  D:\a\ba
        //like path%
        //=  最多删除一个，绝对匹配

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            String sql = "delete from thing where path = ?";
            statement = connection.prepareStatement(sql);
            //预编译命令中SQL的占位符赋值
            statement.setString(1,thing.getPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }

    }

    /**
     *     查询
     */
    @Override
    public List<Thing> query(Condition condition) {
        List<Thing> things = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        //name     : like
        //fileType : =
        //limit    : limit offset
        //orderbyASC :order by
        try {
            connection = this.dataSource.getConnection();
            //StringBuffer 线程安全  方法在虚拟机栈中
            //因为现在是在方法中，是线程私有的所以不存在多线程共享所以使用StringBuilder
            StringBuilder sb = new StringBuilder();
            sb.append(" select name,path,depth,file_type from file_index ");
            sb.append(" where ");
            //search <name> [file_name]
            //name的采用模糊匹配
            //前模糊
            //后模糊
            //前后模糊 √
            sb.append(" name like '%").append(condition.getName()).append("%' ");
            if (condition.getFileType() != null){

//                FileType fileType = FileType.lookByExtend(condition.getFileType());
//                sb.append("and file_type = '" + fileType.name() + "'");
                sb.append("and file_type = '")
                        .append(condition.getFileType().toUpperCase())
                        .append("' ");
            }

            //limit, order by
            //先order by 后 limit
            sb.append(" order by depth ").append(condition.getOrderByDepthAsc() ? "asc" : "desc");
            sb.append(" limit ").append(condition.getLimit()).append(" offset 0 ");

            statement = connection.prepareStatement(sb.toString());
            //5.执行命令  executeQuery 执行查询返回结果集
            resultSet = statement.executeQuery();
            //6.处理结果
            while (resultSet.next()){
                //Record -> Thing
                //数据库中的行记录 ---> Java中的Thing对象
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType(FileType.lookupByName(resultSet.getString("file_type")));
                things.add(thing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(resultSet,statement,connection);
        }
        return things;
    }

    //在不改变程序的功能和业务的前提下，对代码进行优化，使得代码更易阅读和扩展
    //解决大量内部代码重复问题： 重构
    private void releaseResource(ResultSet resultSet, PreparedStatement statement,Connection connection)  {
        if (resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        FileIndexDao fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.getInstance());
//        Thing thing = new Thing();
//        thing.setName("简历2.ppt");
//        thing.setPath("D:\\a\\test\\简历2.ppt");
//        thing.setDepth(3);
//        thing.setFileType(FileType.DOC);
//
////        fileIndexDao.insert(thing);
//
//        Condition condition = new Condition();
//        condition.setName("简历");
//        condition.setLimit(3);
//        condition.setOrderByDepthAsc(true);
//        condition.setFileType("doc");
//        List<Thing> things = fileIndexDao.query(condition);
//        for (Thing t : things){
//            System.out.println(t);
//        }
//    }
}
