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

public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor {

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
        //监控的目录
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
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void onDirectoryCreate(File directory){
        System.out.println("onDirectoryCreate : " + directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(directory));
    }

    @Override
    public void onDirectoryDelete(File directory){
        System.out.println("onDirectoryCreat :" + directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConverThing.convert(directory));
    }

    @Override
    public void onFileDelete(File file){
        System.out.println("onFileDElete : " + file.getAbsolutePath());
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
