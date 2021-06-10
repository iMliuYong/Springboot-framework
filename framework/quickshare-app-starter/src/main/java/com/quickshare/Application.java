package com.quickshare;

import com.quickshare.framework.core.annotation.UniqueNameGenerator;
import com.quickshare.framework.core.global.GlobalVariable;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.logging.ClasspathLoggingApplicationListener;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 应用程序入口
 * @author liu_ke
 */
@ComponentScan(basePackages = "com.quickshare",nameGenerator = UniqueNameGenerator.class)
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        GlobalVariable.Args = new HashMap<>();
        for (String arg:args) {
            int index = arg.indexOf("=");
            if(index > 0 ){
                String key = arg.substring(0,index);
                String value = arg.substring(index + 1);
                while (key.startsWith("-")){
                    key = key.substring(1);
                }
                if(!key.isEmpty()){
                    GlobalVariable.Args.put(key,value);
                }
            }
        }

        SpringApplication app=new SpringApplication(Application.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}