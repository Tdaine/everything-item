package com.abaka.cmd;

import com.abaka.config.EverythingConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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





    }
    private static void everythingConfigInit(Properties p){

    }
}
