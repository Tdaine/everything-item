package com.abaka.core.monitor.impl;

import com.abaka.config.EverythingConfig;
import com.abaka.config.HandlerPath;
import com.abaka.core.common.FileConverThing;
import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.monitor.FileMonitor;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.Set;

/**
 * 使用了外部框架的类来实现监控 commons-io
 * FileAlterationListenerAdaptor
 * FileAlterationMonitor
 * FileAlterationObserver
 */
public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor {

    //框架中的文件监控
    private FileAlterationMonitor monitor;

    private final FileIndexDao fileIndexDao;

    public FileMonitorImpl(FileIndexDao fileIndexDao) {
        this.monitor = new FileAlterationMonitor(EverythingConfig.getInstance().getInterval());
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void start() {
        //启动
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlerPath handlerPath) {
        //监控的目录是handlerPath中包含的目录
        Set<String> includePath = handlerPath.getIncludePath();
        for (String path:includePath){
            FileAlterationObserver observer =
                    new FileAlterationObserver(new File(path),pathname -> {
                        for (String exclude : handlerPath.getExcludePath()){
                            if (pathname.getAbsolutePath().startsWith(exclude)){
                                return false;
                            }
                        }
                        return true;
                    });
            //用来出来文件同步问题  this:FileAlterationListenerAdaptor的对象
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    ///////各种监听动作处理
    @Override
    public void onDirectoryCreate(File directory){
        System.out.println("onDirectoryCreate : " + directory.getAbsolutePath());
        //文件对象转换为things
        this.fileIndexDao.insert(FileConverThing.convert(directory));
    }

    @Override
    public void onDirectoryDelete(File directory){
        System.out.println("onDirectoryDelete :" + directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(directory));
    }

    @Override
    public void onFileDelete(File file){
        System.out.println("onFileDelete : " + file.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(file));
    }

    @Override
    public void stop() {
        //停止
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
