package com.abaka.core.model;

import lombok.Data;

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
     */
    public Boolean orderByDepthAsc;
}
