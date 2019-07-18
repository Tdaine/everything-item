package com.abaka.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 配置了信息应该使用单例
 * 不希望被多次new，这样容易使配置信息改变
 */
@ToString
public class EverythingConfig {

    private static volatile EverythingConfig config;

    /**
     * 索引目录
     */
    @Getter
    private HandlerPath handlerPath = HandlerPath.getDefaultHandlerPath();

    /**
     * 最大检索返回的结果数
     */
    @Setter
    @Getter
    private Integer maxReturn = 30;

    /**
     * 是否开启构建索引
     * 默认：程序运行不开启构建索引
     * 1.运行程序时，指定参数
     * 2.通过功能命令 index
     */
    @Getter
    @Setter
    private Boolean enableBuildindex = false;

    /**
     * 检索时Depth深度的排序规则
     * true：表示降序
     * false：表示升序
     */
    @Getter
    @Setter
    private Boolean orderByDesc = false;
    /**
     * 文件监控的间隔时间10s
     */
    @Getter
    @Setter
    private Integer interval = 600 * 10;

    private EverythingConfig(){

    }

    public static EverythingConfig getInstance(){
        if (config == null){
            synchronized (EverythingConfig.class){
                if (config == null)
                    config = new EverythingConfig();
            }
        }
        return config;
    }

//    public static void main(String[] args) {
//        EverythingConfig everythingConfig = EverythingConfig.getInstance();
//        System.out.println(everythingConfig.handlerPath.getIncludePath());
//        System.out.println(everythingConfig.handlerPath.getExcludePath());
//    }
}
