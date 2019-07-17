package com.abaka.core.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件类型
 */
public enum  FileType {
    IMG("jpg","jpeg","png","bmp","gif"),
    DOC("doc","docx","pdf","ppt","pptx","xls","txt"),
    BIN("exe","jar","sh","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");

    /**
     * 对应的文件类型的扩展名集合
     */
    private Set<String> extend = new HashSet<>();

    FileType(String... extend){
        //将extend变为集合
        this.extend.addAll(Arrays.asList(extend));
    }

    /**
     * 根据文件扩展名获取文件类型
     * @param extend
     * @return
     */
    public static FileType lookByExtend(String extend){
        //FileType.values()获取枚举中的文件类型
        for (FileType fileType : FileType.values()){
            if (fileType.extend.contains(extend)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

    public static FileType lookupByName(String name){
        //FileType.values()获取枚举
        for (FileType fileType : FileType.values()){
            if (fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

//    public static void main(String[] args) {
//        System.out.println(FileType.lookByExtend("png"));
//        System.out.println(FileType.lookByExtend("ppt"));
//        System.out.println(FileType.lookByExtend("java"));
//    }
}
