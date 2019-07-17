package com.abaka.core.model;

import lombok.Data;


/**
 * 索引file的属性之后的信息
 * 也就是数据库中应该储存的信息
 */
//使用注解，生成get,set,toString
@Data
public class Thing {
    /**
     * 文件名称（不含路径）
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;

    /**
     * 文件路径深度
     */
    private Integer depth;

    /**
     * 文件类型
     */
    private FileType fileType;
}
