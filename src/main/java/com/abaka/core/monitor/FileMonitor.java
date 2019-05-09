package com.abaka.core.monitor;

import com.abaka.config.HandlerPath;

public interface FileMonitor {
    void start();

    /**
     * 监控
     */
    void monitor(HandlerPath handlerPath);

    void stop();
}
