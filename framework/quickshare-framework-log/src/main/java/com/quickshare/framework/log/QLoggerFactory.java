package com.quickshare.framework.log;

import com.quickshare.framework.core.global.GlobalVariable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.*;
import org.apache.logging.log4j.core.async.AsyncLoggerConfig;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.builder.api.*;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.logging.log4j.core.Filter.Result.DENY;

/**
 * 基于原 {@link org.slf4j.LoggerFactory}
 * @author liu_ke
 */
public class QLoggerFactory {

    private static final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    private static final Configuration config = ctx.getConfiguration();

    /**启动一个动态的logger*/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void start(String loggerName,String folder) {
        Layout layout = PatternLayout.newBuilder()
                .withConfiguration(config).withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n").build();
        // -- %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n

        //OnStartupTriggeringPolicy ostp = OnStartupTriggeringPolicy.createPolicy(OnStartupTriggeringPolicy.DEFAULT_STOP_TIMEOUT);
        TimeBasedTriggeringPolicy tbtp = TimeBasedTriggeringPolicy.newBuilder()
                .withInterval(1).build();
        SizeBasedTriggeringPolicy sbtp = SizeBasedTriggeringPolicy.createPolicy("50M");
        CompositeTriggeringPolicy policyComposite = CompositeTriggeringPolicy.createPolicy(tbtp, sbtp);

        IfFileName ifFileName = IfFileName.createNameCondition("*/*.log.gz",null);
        IfLastModified ifLastModified = IfLastModified.createAgeCondition(Duration.parse("30d"));
        IfAccumulatedFileSize ifAccumulatedFileSize = IfAccumulatedFileSize.createFileSizeCondition("20G");
        IfAccumulatedFileCount ifAccumulatedFileCount = IfAccumulatedFileCount.createFileCountCondition(100);
        IfAny ifAny = IfAny.createOrCondition(ifLastModified,ifAccumulatedFileSize,ifAccumulatedFileCount);
        DeleteAction deleteAction = DeleteAction.createDeleteAction(
                folder, false, 1, false, null,
                new PathCondition[]{ifFileName, ifAny}, null, config);
        Action[] actions = new Action[]{deleteAction};
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withCustomActions(actions)
                .withMax("1000")
                .withConfig(config)
                .build();

        String loggerDir = Paths.get((String)System.getProperties().get("LOG_PATH"), folder, loggerName).toString();
        // 默认的
        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .withFileName(loggerDir + ".log")
                .withFilePattern(loggerDir + ".%d{yyyy-MM-dd}.%i.log.gz")
                .withAppend(true)
                .withStrategy(strategy)
                .withPolicy(policyComposite)
                .setName(loggerName+"_appender")
                .setLayout(layout)
                .build();
        appender.start();

        // 错误信息
        ThresholdFilter filterError = ThresholdFilter.createFilter(Level.ERROR, Filter.Result.ACCEPT, DENY);
        RollingFileAppender appender_error = RollingFileAppender.newBuilder()
                .withFileName(loggerDir + ".error.log")
                .withFilePattern(loggerDir + ".error.%d{yyyy-MM-dd}.%i.log.gz")
                .withAppend(true)
                .withStrategy(strategy)
                .withPolicy(policyComposite)
                .setName(loggerName+"_appender_error")
                .setFilter(filterError)
                .setLayout(layout)
                .build();
        appender_error.start();
        config.addAppender(appender);
        config.addAppender(appender_error);

        AppenderRef refDefault = AppenderRef.createAppenderRef(loggerName+"_appender", null, null);
        AppenderRef refError = AppenderRef.createAppenderRef(loggerName+"_appender_error", null, null);
        AppenderRef[] refs = null;
        // 控制台输出
        boolean useConsole = false;
        if(GlobalVariable.Args != null) {
            useConsole = Boolean.parseBoolean(GlobalVariable.Args.getOrDefault("logging.console.enabled", "false"));
        }
        if(useConsole){
            AppenderRef refConsole = AppenderRef.createAppenderRef("Console", null, null);
            refs = new AppenderRef[]{refDefault,refError,refConsole};
        }
        else{
            refs = new AppenderRef[]{refDefault,refError};
        }
        //AsyncLoggerConfig
        LoggerConfig loggerConfig = AsyncLoggerConfig.createLogger(false,
                Level.ALL, loggerName, "false", refs, null, config, null);
        loggerConfig.addAppender(appender, Level.ALL, null);
        loggerConfig.addAppender(appender_error, Level.ERROR, null);
        if(useConsole){
            loggerConfig.addAppender(config.getAppenders().get("Console"), Level.ALL, null);
        }
        config.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();
    }

    /**启动一个动态的logger*/
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void start2(String loggerName,String folder) {
        ConfigurationBuilder<BuiltConfiguration> builder
                = ConfigurationBuilderFactory.newConfigurationBuilder();

        LayoutComponentBuilder layout = builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n");
        // -- %d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n

        ComponentBuilder triggeringPolicies = builder.newComponent("Policies")
                .addComponent(builder.newComponent("OnStartupTriggeringPolicy"))
                .addComponent(builder.newComponent("TimeBasedTriggeringPolicy")
                        .addAttribute("interval", 1))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                        .addAttribute("size", "50M"));
        ComponentBuilder rolloverStrategy = builder.newComponent("DefaultRolloverStrategy")
                .addAttribute("max",1000)
                .addComponent(builder.newComponent("Delete")
                        .addAttribute("basePath",folder)
                        .addAttribute("maxDepth",1)
                        .addComponent(builder.newComponent("IfFileName")
                                .addAttribute("glob","*/*.log.gz")
                                .addComponent(builder.newComponent("IfAny")
                                        .addComponent(builder.newComponent("IfLastModified")
                                                .addAttribute("age","30d"))
                                        .addComponent(builder.newComponent("IfAccumulatedFileSize")
                                                .addAttribute("exceeds","20 G"))
                                        .addComponent(builder.newComponent("IfAccumulatedFileCount")
                                                .addAttribute("exceeds",100))
                            )
                        )
                );


        String loggerDir = Paths.get(folder,loggerName).toString();
        AppenderComponentBuilder rollingFile = builder.newAppender("rolling", "RollingFile");
        rollingFile.addAttribute("fileName", loggerDir + ".log");
        rollingFile.addAttribute("filePattern", loggerDir + ".%d{yyyy-MM-dd}.%i.log.gz");
        rollingFile.add(layout);
        rollingFile.addComponent(triggeringPolicies);
        rollingFile.addComponent(rolloverStrategy);
        builder.add(rollingFile);

        //LoggerComponentBuilder logger = builder.newAsyncLogger("rollingLogger",Level.TRACE,false);
        LoggerComponentBuilder loggerBuilder = builder.newLogger(loggerName,Level.ALL);
        loggerBuilder.add(builder.newAppenderRef("rolling"));
        loggerBuilder.addAttribute("additivity",false);

        builder.add(loggerBuilder);
        try {
            builder.writeXmlConfiguration(System.out);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Configurator.initialize(builder.build());
        ctx.updateLoggers();
    }

    public static Logger getLogger(String loggerName) {
        return getLogger(loggerName,"");
    }

    public static Logger getDefaultLogger(){
        return getLogger("default");
    }

    public static Logger getLogger(String loggerName,String folder) {
        synchronized (config) {
            if (!config.getLoggers().containsKey(loggerName)) {
                start(loggerName,folder);
            }
        }
        return LoggerFactory.getLogger(loggerName);
    }
}
