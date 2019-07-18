package com.abaka.core.index.impl;

import com.abaka.config.EverythingConfig;
import com.abaka.core.dao.DataSourceFactory;
import com.abaka.core.dao.impl.FileIndexDaoImpl;
import com.abaka.core.index.FileScan;
import com.abaka.core.interceptor.FileInterceptor;
import com.abaka.core.interceptor.impl.FileIndexInterceptor;
import com.abaka.core.interceptor.impl.FilePrintInterceptor;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.LinkedList;
import java.util.Set;

public class FileScanImpl implements FileScan {

//拦截器
    private final LinkedList<FileInterceptor> interceptors = new LinkedList<>();

    private EverythingConfig  config = EverythingConfig.getInstance();


    @Override
    public void index(String path) {
        Set<String> excludepaths = config.getHandlerPath().getExcludePath();
        //C:\Windows
        //C:\Windows C:\Windows\System32
        //判断A path 是否在B pathZ中
        for (String excludePath : excludepaths){
            if (path.startsWith(excludePath)){
                return;
            }
        }
        //通过管道机制直接将文件去处理
        //因为里面是递归调用，所以当你在方法最开始检测到这个文件或者目录是在排除路径里面，就不会产生file
        //当产生了file说明要遍历整个file中的所有文件，所以就可以使用interceptors对整个路径进行遍历输出
        File file = new File(path);
        //如果该路径是一个文件夹就进行递归
        if (file.isDirectory()){
            File[] files = file.listFiles();
            if (files != null){
                for (File f:files){
                    index(f.getAbsolutePath());
                }
            }
        }
        //可以到这一步就表示这是一个文件，如果是一个文件夹就会在上面进行递归，知道递归到文件才会到这里进行输出
        //这里可以打印所有的文件，因为上面的遍历过程会将每一个文件记录在file中
        //打印该文件路径
        //遍历interceptors可以获得具体的apply操作。
        for (FileInterceptor interceptor : this.interceptors){
            interceptor.apply(file);
        }
    }

    //将具体类的对象添加到interceptors中
    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

//    public static void main(String[] args) {
//        FileScan scan = new FileScanImpl();
//        FileInterceptor printInterceptor = new FilePrintInterceptor();
//
//        scan.interceptor(printInterceptor);
//        FileInterceptor fileInterceptor = new FileIndexInterceptor(new FileIndexDaoImpl(DataSourceFactory.getInstance()));
//        scan.interceptor(fileInterceptor);
//        scan.index("D:\\learning");
//    }
}
