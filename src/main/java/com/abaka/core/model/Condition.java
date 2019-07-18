package com.abaka.core.model;

import lombok.Data;


/**
 * 检索的参数条件
 */
@Data
public class Condition {
    /**
     * 文件名
     */
    private String name;

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 限制数量
     */
    private Integer limit;

    /**
     * 是否按照dept进行升序
     * 检索结果文件信息的排序规则
     * 1.默认是true -> asc
     * 2.false -> desc
     */
    public Boolean orderByDepthAsc;
}
