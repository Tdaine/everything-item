package com.abaka.core.interceptor.impl;

import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.interceptor.ThingInterceptor;
import com.abaka.core.model.Thing;

import java.util.Queue;


//如果文件已经被删除了，应该清除数据库中的文件
public class ThingClearInterceptor implements Runnable,ThingInterceptor {

    private final FileIndexDao fileIndexDao;
    private final Queue<Thing> thingQueue;

    public ThingClearInterceptor(FileIndexDao fileIndexDao, Queue<Thing> thingQueue) {
        this.fileIndexDao = fileIndexDao;
        this.thingQueue = thingQueue;
    }

    @Override
    public void apply(Thing thing) {
        this.fileIndexDao.delete(thing);
    }

    /**
     * 执行清理工作
     */
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thing thing = this.thingQueue.poll();
            if (thing != null){
                //调用删除数据库文件操作
                this.apply(thing);
            }
        }

    }
}
