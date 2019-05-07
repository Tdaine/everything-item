package com.abaka.core.interceptor;

import com.abaka.core.model.Thing;

public interface ThingInterceptor {

    /**
     * 检索结果thing 的拦截器
     * @param thing
     */
    void apply(Thing thing);
}
