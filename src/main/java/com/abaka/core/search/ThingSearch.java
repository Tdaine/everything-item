package com.abaka.core.search;

import com.abaka.core.model.Condition;
import com.abaka.core.model.Thing;

import java.util.List;

/**
 * 文件检索业务
 */
public interface ThingSearch {
    /**
     * 根据condition条件进行数据库的检索
     * @param condition
     * @return 返回thing集合
     */
    List<Thing> search(Condition condition);
}
