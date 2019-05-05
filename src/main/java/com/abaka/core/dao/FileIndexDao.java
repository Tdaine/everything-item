package com.abaka.core.dao;

import com.abaka.core.model.Condition;
import com.abaka.core.model.Thing;

import java.util.List;

/**
 * 数据库访问的对象
 */
public interface FileIndexDao {
    /**
     * 插入
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 删除
     * @param thing
     */
    void delete(Thing thing);

    /**
     * 查询
     * @param condition
     * @return
     */
    List<Thing> query(Condition condition);
}
