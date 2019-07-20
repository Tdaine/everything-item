package com.abaka.core.monitor;

import com.abaka.config.HandlerPath;

public interface FileMonitor {
//    启动监控器
    void start();

    /**
     * 监控
     * 监控文件夹
     * 解决文件不同步问题
     */
    void monitor(HandlerPath handlerPath);

    //停止监控器
    void stop();
}
