package com.zhiyicx.common.utils;

import android.support.annotation.Nullable;

import com.zhiyicx.common.config.ConstantConfig;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Describe 时间格式化工具类 ,api接口里 的时间返回 统一用时间空间字符串格式 UTC+ 0 时区 例如; 2017-03-01 01:28:33
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */

public class TimeUtils {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 将时间戳转为时间字符串，转换默认时区
     * <p>格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param millis 毫秒时间戳
     * @return 时间字符串
     */
    public static String millis2String(long millis) {
        return new SimpleDateFormat(DEFAULT_PATTERN, Locale.getDefault()).format(new Date(millis));
    }

    /**
     * 动态列表 \评论列表 时间戳格式转换
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前
     * 一天内显示几小时前
     * 1天到2天显示昨天
     * 2天到9天显示几天前
     * 9天以上显示月日如（05-21）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyNormal(String timestr) {
        String result = "1分钟前";
        long timesamp = string2Millis(timestr);
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = getFriendlyTimeForBeforHour(timesamp, result);
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "两天前";
                break;
            case 3:
                result = "三天前";
                break;
            case 4:
                result = "四天前";
                break;
            case 5:
                result = "五天前";
                break;
            case 6:
                result = "六天前";
                break;
            case 7:
                result = "七天前";
                break;
            case 8:
                result = "八天前";
                break;
            case 9:
                result = "九天前";
                break;

            default:
                result = getStandardTimeWithMothAndDay(timesamp);
                break;
        }
        return result;
    }


    /**
     * 聊天详情页 备注：聊天时间显示间隔6分钟
     * <p>
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（五天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timesamp 时间戳 单位 ms
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyForChat(long timesamp) {
        return handleDetailTime(timesamp);
    }


    /**
     * 详情页(动态详情页、聊天详情页) 备注：聊天时间显示间隔6分钟
     * <p>
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（五天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好时间字符串
     */
    public static String getTimeFriendlyForDetail(String timestr) {
        long timesamp = string2Millis(timestr);
        return handleDetailTime(timesamp);
    }

    /**
     * 个人主页
     * 1天内显示今天，
     * 1天到2天显示昨天，
     * 2天以上显示月日如（05-21）
     *
     * @param timestr 时间 例如; 2017-03-01 01:28:33
     * @return 友好的时间字符串
     */
    public static String getTimeFriendlyForUserHome(String timestr) {
        String result = "1分钟前";
        long timesamp = string2Millis(timestr);
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = "今,天";
                break;
            case 1:
                result = "昨,天";
                break;

            default:
                result = getStandardTimeWithMothAndDayOne(timesamp);
                break;
        }
        return result;
    }

    /**
     * 一分钟内显示一分钟
     * 一小时内显示几分钟前，
     * 一天内显示几小时前，
     * 1天到2天显示如（昨天 20:36），
     * 2天到9天显示如（五天前 20：34），
     * 9天以上显示如（02-28 19:15）
     *
     * @param timesamp
     * @return
     */
    private static String handleDetailTime(long timesamp) {
        String result = "1分钟前";
        switch (getifferenceDays(timesamp)) {
            case 0:
                result = getFriendlyTimeForBeforHour(timesamp, result);
                break;
            case 1:
                result = "昨天 " + getStandardTimeWithHour(timesamp);
                break;
            case 2:
                result = "两天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 3:
                result = "三天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 4:
                result = "四天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 5:
                result = "五天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 6:
                result = "六天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 7:
                result = "七天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 8:
                result = "八天前 " + getStandardTimeWithHour(timesamp);
                break;
            case 9:
                result = "九天前 " + getStandardTimeWithHour(timesamp);
                break;

            default:
                result = getStandardTimeWithDate(timesamp);
                break;
        }
        return result;
    }


    /**
     * 输入时间和当前时间间隔的天数
     *
     * @param timesamp 输入时间
     * @return 输入时间和当前时间间隔的天数
     */
    public static int getifferenceDays(long timesamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        return Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));
    }


    /**
     * 获取几分钟前、和几小时前
     *
     * @param timesamp     时间戳
     * @param defaltString 默认文字
     * @return
     */
    @Nullable
    private static String getFriendlyTimeForBeforHour(long timesamp, String defaltString) {
        long curTime = System.currentTimeMillis();
        long time = curTime - timesamp;
        if (time < ConstantConfig.MIN) {
            return defaltString;
        } else if (time < ConstantConfig.HOUR) {
            return time / ConstantConfig.MIN + "分钟前";
        } else if (time < ConstantConfig.DAY) {
            return time / ConstantConfig.HOUR + "小时前";
        }
        return defaltString;
    }

    /**
     * 通过时间戳获取 yyyy
     *
     * @param timestamp
     * @return
     */
    public static int getYear(long timestamp) {
        return Integer.parseInt(getTime(timestamp, "yyyy"));
    }

    /**
     * 通过时间戳获取 yyyy-MM-dd HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTimeWithYeay(long timestamp) {
        return getTime(timestamp, "yyyy-MM-dd HH:mm");
    }

    /**
     * 通过时间戳获取 format类型时间
     *
     * @param timestamp ms
     * @param format    格式类型
     * @return
     */
    public static String getTime(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 通过时间戳获取 MM-dd HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTimeWithDate(long timestamp) {
        return getTime(timestamp, "MM-dd HH:mm");
    }

    /**
     * 通过时间戳获取 HH:mm
     */
    public static String getStandardTimeWithHour(long timestamp) {
        return getTime(timestamp, "HH:mm");
    }

    /**
     * 通过时间戳获取 MM-dd
     */
    public static String getStandardTimeWithMothAndDay(long timestamp) {
        return getTime(timestamp, "MM-dd");
    }

    /**
     * 通过时间戳获取 dd,MM月
     */
    public static String getStandardTimeWithMothAndDayOne(long timestamp) {
        return getTime(timestamp, "dd,M月");
    }

    /**
     * 通过时间戳获取 mm:ss
     */
    public static String getStandardTimeWithMinAndSec(long timestamp) {
        return getTime(timestamp, "mm:ss");
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为yyyy-MM-dd HH:mm:ss</p>
     *
     * @param time 时间字符串
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time) {
        return string2Millis(time, DEFAULT_PATTERN);
    }

    /**
     * 将时间字符串转为时间戳
     * <p>time格式为pattern</p>
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return 毫秒时间戳
     */
    public static long string2Millis(String time, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.getDefault()).parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
