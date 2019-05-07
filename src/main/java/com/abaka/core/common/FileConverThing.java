package com.abaka.core.common;

import com.abaka.core.model.FileType;
import com.abaka.core.model.Thing;

import java.io.File;

/**
 * 文件对象转换Thing对象的辅助类
 */
public class FileConverThing {

    public static Thing convert(File file){
        Thing thing = new Thing();
        String name = file.getName();
        thing.setName(name);
        thing.setPath(file.getAbsolutePath());

        //目录 -> *
        //文件 -> 有扩展名，通过扩展名获取FileType
        //无扩展名，*
        int index = name.lastIndexOf(".");
        String extend = "*";
        if (index != -1 && (index + 1) < name.length()){
            extend = name.substring(index + 1);
        }
        thing.setFileType(FileType.lookByExtend(extend));
        thing.setDepth(computePathDepth(file.getAbsolutePath()));
        return thing;
    }

    private static int computePathDepth(String path) {
        //path => D:\a\b =>2
        //path => D:\a\b\hello.java => 3
        //window: \
        //Linux: /
        int depth = 0;
        for (char c : path.toCharArray()){
            if (c == File.separatorChar){
                depth += 1;
            }
        }
        return depth;
    }
}
