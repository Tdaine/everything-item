package com.abaka.core.index.impl;

import com.abaka.config.EverythingConfig;
import com.abaka.core.index.FileScan;
import com.abaka.core.interceptor.FileInterceptor;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.LinkedList;
import java.util.Set;

public class FileScanImpl implements FileScan {

    private final LinkedList<FileInterceptor> interceptors = new LinkedList<>();

    private EverythingConfig  config = EverythingConfig.getInstance();


    @Override
    public void index(String path) {
        Set<String> excludepaths = config.getHandlerPath().getExcludePath();
        //C:\Windows
        //C:\Windows C:\Windows\System32
        //判断A path 是否在B pathZ中
        for (String excludePath : excludepaths){
            if (path.startsWith(excludePath)){
                return;
            }
        }
        File file = new File(path);
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null){
                for (File f:files){
                    index(f.getAbsolutePath());
                }
            }
        }
        for (FileInterceptor interceptor : this.interceptors){
            interceptor.apply(file);
        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }
}
