package com.abaka.core.dao;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.rmi.activation.ActivationGroup_Stub;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 工厂模式 + 单例模式实现
 * new 的对象是可以反复new，也就是对象在堆上的空间占有的特别多
 * 而数据源的对象不要需要反复new，只需要创建一次。所以就是一个单例
 *
 *
 *
 */
public class DataSourceFactory {
    /**
     * DruidDataSource 阿里巴巴提供的数据库的数据源  单例
     */
    private static volatile DruidDataSource instance;

    //不应该让用户实例化
    public DataSourceFactory(){

    }

    //使用阿里巴巴提供的数据库连接池
    //使用单例模式创建数据库连接池对象
    public static DataSource getInstance(){
        if (instance == null){
            synchronized (DataSource.class){
                if (instance == null){
                    //实例化  获取了一个数据源接口的实例化对象
                    instance = new DruidDataSource();

                    //连接H2数据库的配置
                    instance.setTestWhileIdle(false);

                    //JDBC driver class
                    //内部通过反射的方式实现了一个driver对象
                    instance.setDriverClassName("org.h2.Driver");

                    //url,username,passward
                    //采用的是h2的嵌入式数据库，数据库以本地文件的方式存储，只需要提供url接口
                    //JDBC规范中关于MySqL jdbC:mysql://ip:port/databaseName ->存储到本地文件
                    //JDBC规范中关于H2  jdbc:h2:~/filepath ->存储到当前用用户的home目录
                    //JDBC规范中关于H2  jdbC:H2://ip:port/databaseName ->存储到服务器
                    //获取当前工程路径System.getProperty("user.dir") + File.separator+ "everything_item
                    String path = System.getProperty("user.dir") + File.separator + "everything_item";
                    instance.setUrl("jdbc:h2:" + path);
                    //数据库创建完成之后，初始化表结构
                   // databaseInit(false);
                }
            }
        }
        return instance;
    }

    public static void databaseInit(boolean buildIndex){

        //1.获取数据源
        //DataSource dataSource = DataSourceFactory.getInstance();


        StringBuilder sb = new StringBuilder();

        //2.获取sql语句  DataSourceFactory.class.getClassLoader().getResourceAsStream("database.sql")
        //DataSourceFactory.class.getClassLoader()  获取类加载器
        //getResourceAsStream将资源文件读取，并转为stream流
        //try-with-resources  为了保证流可以关闭
        try(InputStream in = DataSourceFactory.class.getClassLoader().getResourceAsStream("everything_item.sql");
        ){
            if (in != null){
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in)
                )){
                    String line = null;
                    //表示没有读到末尾
                    while ((line = reader.readLine()) != null){
                        if(!line.startsWith("#")){
                            sb.append(line);
                        }
                    }
                }catch (IOException e) {
                  e.printStackTrace();
                }
            }else {
                throw new RuntimeException("everything_item.sql script can't load please check it.");
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        //3.通过数据库连接和名称执行SQL
        String sql = sb.toString();
        //JDBC
        //3.1获取数据库的连接
        try (Connection connection = getInstance().getConnection();){
            if (buildIndex){
                try (PreparedStatement statement = connection.prepareStatement("drop table if exists thing;")){
                    statement.executeUpdate();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            //3.2创建命令  Statement对象主要是将SQL语句发送到数据库中
            try (PreparedStatement statement = connection.prepareStatement(sql);){
                //执行SQL语句
                statement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        DataSourceFactory.databaseInit(false);
//    }

}
