package com.abaka.core.interceptor.impl;

import com.abaka.core.common.FileConverThing;
import com.abaka.core.dao.FileIndexDao;
import com.abaka.core.interceptor.FileInterceptor;
import com.abaka.core.model.Thing;

import java.io.File;

public class FileIndexInterceptor implements FileInterceptor {

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    //[转换，写入(Thing)]
    @Override
    public void apply(File file) {
        Thing thing = FileConverThing.convert(file);
        this.fileIndexDao.insert(thing);
    }
}
