package com.abaka.core.index;

import com.abaka.core.interceptor.FileInterceptor;

public interface FileScan {
    //path -> File -> Thing -> Database Record

    /**
     * 将指定path路径下的所有目录和文件以及子目录和文件递归扫描
     * 索引到数据库
     * @param path
     */
    void index(String path);

    /**
     * 拦截器对象
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);

}
