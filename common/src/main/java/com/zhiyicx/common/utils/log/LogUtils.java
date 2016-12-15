package com.zhiyicx.common.utils.log;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class LogUtils {
    private static final String APPLICATION_TAG = "LogUtils";
    public static final int LOGGER_METHODCOUNT = 3;
    public static final int LOGGER_METHODOFFSET = 2;

    public static void init() {
        Logger
                .init(APPLICATION_TAG)           // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.NONE)        // default LogLevel.FULL
                .methodOffset(2);              // default 0
        // .logAdapter(new AndroidLogAdapter()); //default AndroidLogAdapter
    }

    public static void d(String tag, Object object) {
        Logger.t(tag).d(object);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(Object object) {
        Logger.d(object);
    }

    public static void e(String message, Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void v(String message, Object... args) {
        Logger.v(message, args);
    }

    public static void w(String message, Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(String message, Object... args) {
        Logger.wtf(message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.t(tag).json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Logger.xml(xml);
    }

    public static void xml(String tag, String xml) {
        Logger.t(tag).xml(xml);
    }
}
