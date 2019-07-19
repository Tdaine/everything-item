package com.abaka.core.search.impl;

import com.abaka.core.dao.DataSourceFactory;
import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.interceptor.impl.ThingClearInterceptor;
import com.abaka.core.model.Condition;
import com.abaka.core.model.Thing;
import com.abaka.core.search.ThingSearch;

import javax.sql.DataSource;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * 业务层
 */
public class ThingSearchImpl implements ThingSearch {

    //被final修饰的变量初始化有三种，直接赋值，构造方法，构造块
    //获取数据源对象
    private final FileIndexDao fileIndexDao;

    private final ThingClearInterceptor interceptor;

    private final Queue<Thing> thingQueue = new ArrayBlockingQueue<>(1024);

    public ThingSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
        this.interceptor = new ThingClearInterceptor(this.fileIndexDao,thingQueue);
        //如果一旦创建了interceptor对象就启动清理线程
        this.backgroundClearThread();
    }

    //
    private void backgroundClearThread() {
        //进行后台清理工作，让它不影响我们的查询线程
        Thread thread = new Thread(this.interceptor);
        thread.setName("Thread-Clear");
        //设置为守护线程
        thread.setDaemon(true);
        //开启清理数据库文件线程，后台在run方法中调用了fileIndexDao的delete操作
        thread.start();
    }


    @Override
    public List<Thing> search(Condition condition) {
        //BUG
        //如果本地文件系统将文件删除，数据库中仍然存储到索引信息
        //此时如果查询结果存在已经在文件系统中删除的文件，那么需要在数据库中清除掉该文件的索引信息
        List<Thing> things = this.fileIndexDao.query(condition);
        Iterator<Thing> iterator = things.iterator();
        while (iterator.hasNext()){
            Thing thing = iterator.next();
            File file = new File(thing.getPath());
            //判断要查询的文件是否存在
            if (!file.exists()){
                //不存在删除迭代器中的文件
                iterator.remove();
                //清除数据库中的文件
                this.thingQueue.add(thing);
            }
        }
        return things;
    }
}
