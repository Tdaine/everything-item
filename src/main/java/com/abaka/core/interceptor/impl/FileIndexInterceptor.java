package com.abaka.core.interceptor.impl;

import com.abaka.core.common.FileConverThing;
import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.interceptor.FileInterceptor;
import com.abaka.core.model.Thing;

import java.io.File;

/**
 * 拦截文件
 * 在按照路径进行文件扫描时拦截
 */
public class FileIndexInterceptor implements FileInterceptor {

    private static int fileNumber = 0;

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    //[转换，写入(Thing)]
    @Override
    public void apply(File file) {
        Thing thing = FileConverThing.convert(file);
        fileNumber++;
        this.fileIndexDao.insert(thing);
    }

    public static int getFileNumber() {
        return fileNumber;
    }
}
