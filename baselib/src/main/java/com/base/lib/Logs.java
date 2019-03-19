package com.base.lib;

import android.util.Log;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Created by biao.yin on 2018/4/25.
 * 6.0之后需要配置文件读写权限
 *
 * @author yinbiao
 */

public class Logs {

    /**
     * 文件最大5MB
     */
    private static final int LOG_FILE_MAX_SIZE = 5 * 1024 * 1024;
    private static boolean isDebug = false;

    private Logs() {
    }

    /**
     * 初始化，必须先赋予文件读写权限
     *
     * @param isDebug 是否调试模式
     * @param path    文件存储完整路径
     */
    public static void configure(boolean isDebug, String path) {
        Logs.isDebug = isDebug;
        if (!isDebug) {
            return;
        }
        final LogConfigurator logConfigurator = new LogConfigurator();
//        Date nowtime = new Date();
        // String needWriteMessage = myLogSdf.format(nowtime);
        //设置文件名
        logConfigurator.setFileName(path);
        //设置root日志输出级别 默认为DEBUG
        logConfigurator.setRootLevel(Level.DEBUG);
        // 设置日志输出级别
        logConfigurator.setLevel("org.apache", Level.INFO);
        //设置 输出到日志文件的文字格式 默认 %d %-5p [%c{2}]-[%L] %m%n
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        //设置输出到控制台的文字格式 默认%m%n
        logConfigurator.setLogCatPattern("%m%n");
        //设置总文件大小
        logConfigurator.setMaxFileSize(LOG_FILE_MAX_SIZE);
        //设置最大产生的文件个数
        logConfigurator.setMaxBackupSize(1);
        //设置所有消息是否被立刻输出 默认为true,false 不输出
        logConfigurator.setImmediateFlush(true);
        //是否本地控制台打印输出 默认为true ，false不输出
        logConfigurator.setUseLogCatAppender(true);
        //设置是否启用文件附加,默认为true。false为覆盖文件
        logConfigurator.setUseFileAppender(true);
        //设置是否重置配置文件，默认为true
        logConfigurator.setResetConfiguration(true);
        //是否显示内部初始化日志,默认为false
        logConfigurator.setInternalDebugging(false);
        try {
            logConfigurator.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Logger log = Logger.getLogger(tag);
            log.debug(msg);
        } else {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Logger log = Logger.getLogger(tag);
            log.error(msg);
        } else {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Logger log = Logger.getLogger(tag);
            log.info(msg);
        } else {
            Log.i(tag, msg);
        }
    }

}
