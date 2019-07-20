package com.abaka.cmd;

import com.abaka.config.EverythingConfig;
import com.abaka.core.EverythingManager;
import com.abaka.core.model.Condition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class EverythingCmdApplication {

    public static void main(String[] args) {
        //0.Everything Config
        if (args.length >= 1){
            String configFile = args[0];
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(configFile));
                //将p赋值给EverythingConfig对象
                everythingConfigInit(p);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //1.EverythingManager
        EverythingManager manager = EverythingManager.getInstance();
        manager.monitor();

        //2.Scanner
        Scanner scanner = new Scanner(System.in);

        //3.用户交互输入
        System.out.println("欢迎使用，阿巴卡的Everything");
        while (true){
            System.out.print(">>");
            String line = scanner.nextLine();
            //JDK1.7之后switch支持使用字符串了
            switch (line){
                case "help":{
                    manager.help();
                    break;
                }
                //退出
                case "quit":{
                    manager.quit();
                    break;
                }
                case "index":{
                    manager.buildIndex();
                    break;
                }
                default:{
                    if (line.startsWith("search")){
                        //解析参数
                        String[] segments = line.split(" ");
                        if (segments.length >= 2){
                            Condition condition = new Condition();
                            String name = segments[1];
                            condition.setName(name);
                            if (segments.length >= 3){
                                String type = segments[2];
                                condition.setFileType(type.toUpperCase());
                            }
                            //打印出查询的结果
                            manager.search(condition).forEach(
                                    thing -> {
                                        System.out.println(thing.getPath());
                                    }
                            );
                        }else {
                            manager.help();
                        }
                    }else {
                        manager.help();
                    }
                }
            }
        }
    }
    private static void everythingConfigInit(Properties p){
        EverythingConfig config = EverythingConfig.getInstance();
        String maxReturn = p.getProperty("everything.max_return");
        String interval = p.getProperty("everything.interval");
        try {
            if (maxReturn != null){
                config.setMaxReturn(Integer.parseInt(maxReturn));
            }
            if (interval != null){
                config.setInterval(Integer.parseInt(interval));
            }
        }catch (NumberFormatException e){

        }
        String enableBuildIndex = p.getProperty("everything.enable_build_index");
//        只要传的字符串是null就会返回false
        config.setEnableBuildindex(Boolean.parseBoolean(enableBuildIndex));

        String orderByDEsc = p.getProperty("everything.order_by_desc");
        config.setOrderByDesc(Boolean.parseBoolean(orderByDEsc));

        //处理的目录
        String includePaths = p.getProperty("everything.handle_path.include_path");
        if (includePaths != null){
            String[] paths = includePaths.split(";");
            if (paths.length > 0){
                //清理到已经有的默认值
                config.getHandlerPath().getIncludePath().clear();
                for (String path : paths){
                    config.getHandlerPath().addIncludePath(path);
                }
            }
        }

        String excludePaths = p.getProperty("everything.handle_path.exclude_path");
        if (excludePaths != null){
            String[] paths = excludePaths.split(";");
            if (paths.length > 0){
                //清理到已经有的默认值
                config.getHandlerPath().getExcludePath().clear();
                for (String path:paths){
                    config.getHandlerPath().addExcludePath(path);
                }
            }
        }
        System.out.println(config);
    }
}
