package com.abaka.core.interceptor;

import java.io.File;

public interface FileInterceptor {
    /**
     * 文件拦截器，处理指定文件
     */
    void apply(File file);
}
