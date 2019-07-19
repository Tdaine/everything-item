package com.abaka.core.common;

import com.abaka.core.model.FileType;
import com.abaka.core.model.Thing;

import java.io.File;

/**
 * 将给定的文件路径转换为thing对象
 * 文件对象转换Thing对象的辅助类
 *
 * 因为是辅助的工具类所以不需要别人继承和实例化，所以定义成私有的(不可以常见对象)和final(不可以被继承)的
 */
public final class FileConverThing {

    private FileConverThing(){}
    public static Thing convert(File file){
        Thing thing = new Thing();
        String name = file.getName();
        thing.setName(name);
        thing.setPath(file.getAbsolutePath());//绝对路径

        //提取出去让程序简洁
//        int index = name.lastIndexOf(".");
//        String extend = "*";
//        if (index != -1 && (index + 1) < name.length()){
//            extend = name.substring(index + 1);
//        }
//        thing.setFileType(FileType.lookByExtend(extend));

        thing.setFileType(computerFileType(file));
        thing.setDepth(computePathDepth(file.getAbsolutePath()));
        return thing;
    }

    /**
     * 计算文件扩展名
     *          //目录 -> *
     *         //文件 -> 有扩展名，通过扩展名获取FileType
     *         //无扩展名，*
     * @param file
     * @return
     */
    private static FileType computerFileType(File file){
        //如果这是一个目录就没有扩展名
        if (file.isDirectory())
            return FileType.OTHER;

        String fileName = file.getName();
        int index = fileName.lastIndexOf(".");
        if (index != -1 && index < fileName.length() - 1){
            //防止是 abc.dfr.ddd 这种文件名称
            String extend = fileName.substring(index + 1);
            return FileType.lookByExtend(extend);
        }else {
            return FileType.OTHER;
        }
    }

    /**
     * 计算文件深度
     * @param path 绝地路径
     * @return
     */
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

//    public static void main(String[] args) {
//        File file = new File("D:\\arm");
//        Thing thing = FileConverThing.convert(file);
//        System.out.println(thing);
//    }
}
