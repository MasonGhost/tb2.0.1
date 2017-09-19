package com.zhiyicx.zhibolibrary.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by jungle on 16/6/21.
 * com.zhiyicx.zhibo.util
 * zhibo_android
 * email:335891510@qq.com
 */
public class TimeHelper {

    public static String friendlyTime(String timestamp) {
        try {
            return friendlyTime(Integer.valueOf(timestamp));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "刚刚";

    }

    public static String friendlyTime(int timestamp) {

        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数

        long toZero = currentSeconds / (24 * 60 * 60) * (24 * 60 * 60);

        long todayGap = currentSeconds - toZero;

        int currentYear = getYear(currentSeconds);
        int timeYear = getYear(timestamp);
        String timeStr = null;
        if (timeYear < currentYear) {
            timeStr = getStandardTimeWithYeay(timestamp);
        }
        else if (timeGap > (todayGap + 24 * 60 * 60)) {
            timeStr = getStandardTimeWithDate(timestamp);
        }
        else if (timeGap > 24 * 60 * 60 || timeGap > todayGap) {// 1天以上
            // timeStr = timeGap/(24*60*60)+"天前";
            timeStr = "昨天" + getStandardTimeWithHour(timestamp);
        }
        else if (timeGap > 60 * 60 && timeGap < todayGap) {// 1小时-24小时
            timeStr = "今天  " + getStandardTimeWithHour(timestamp);
        }
        else if (timeGap > 60 && timeGap < 3600) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        }
        else if (timeGap > 0 && timeGap < 60) {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        else {
            timeStr = "刚刚"; // throw new TimeIsOutFriendly();
        }
        return timeStr;
    }

    public static String friendlyTimeFromeStringTime(String timeTemp)
            throws Exception {

        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - getTimeInt(timeTemp);// 与现在时间相差秒数

        long toZero = currentSeconds / (24 * 60 * 60) * (24 * 60 * 60);
        long todayGap = currentSeconds - toZero;

        String timeStr = null;

        if (timeGap > 24 * 60 * 60 || timeGap > todayGap) {// 1天以上
            // timeStr = timeGap/(24*60*60)+"天前";
            timeStr = getStandardTimeWithDate(getTimeInt(timeTemp));
        }
        else if (timeGap > 60 * 60 && timeGap < todayGap) {// 1小时-24小时
            timeStr = "今天  " + getStandardTimeWithHour(getTimeInt(timeTemp));
        }
        else if (timeGap > 60 && timeGap < 3600) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "分钟前";
        }
        else if (timeGap > 0 && timeGap < 60) {// 1秒钟-59秒钟
            timeStr = "刚刚";
        }
        else {
            timeStr = "刚刚"; // throw new TimeIsOutFriendly();
        }
        return timeStr;
    }

    /*
       获取到当前年月
        */
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static int getYear(long timestamp) {
        String data = getStandardTimeWithYeay(timestamp);
        String str[] = data.split("-");
        return Integer.parseInt(str[0]);
    }

    public static String getStandardTimeWithYeay(long timestamp) {
        return getTime(timestamp, "yyyy-MM-dd HH:mm");
    }

    public static String getTime(long timestamp) {
        return getTime(timestamp, "yyyy年MM月dd日");

    }

    public static String getTime(long timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date(timestamp * 1000);
        return sdf.format(date);
    }

    /*
    获取年月字符串
     */
    public static String getYearAndMonthstr(long timestamp) {
        return getTime(timestamp, "yyyy年MM月");

    }

    public static String getStandardTimeWithDate(long timestamp) {
        return getTime(timestamp, "MM-dd HH:mm");
    }


    public static String getStandardTimeWithSen(long timestamp) {
        return getTime(timestamp, "mm:ss");

    }

    public static long getTimeInt(String timeTemp) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date date = sdf.parse(timeTemp);
        return date.getTime() / 1000;
    }

    /*
    获取年月时间戳
     */
    public static long getYearAndMoth(String timeTemp) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        Date date = sdf.parse(timeTemp);
        return date.getTime() / 1000;
    }

    /*
     获取时间戳
      */
    public static long timestamp(String timeTemp) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = sdf.parse(timeTemp);
        return date.getTime() / 1000;
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /*
        通过时间戳获取时分
         */
    public static String getStandardTimeWithHour(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = new Date(timestamp * 1000);
        return sdf.format(date);
    }

    /*
       通过时间戳获取月日
        */
    public static String getMonthAndDay(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd", Locale.getDefault());
        Date date = new Date(timestamp * 1000);
        return sdf.format(date);
    }
            /*
        获取账单time1
         */

    public static String[] getBillTime(String timestr) {
        int timestamp = 0;
        try {
            timestamp = (int) timestamp(timestr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        String[] tmp = new String[]{"今天", "刚刚"};//保存账单中的time1和time2
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - timestamp;// 与现在时间相差秒数

        long toZero = currentSeconds / (24 * 60 * 60) * (24 * 60 * 60);
        long todayGap = currentSeconds - toZero;
        System.out.println(todayGap);
        int currentYear = getYear(currentSeconds);
        int timeYear = getYear(timestamp);
        if (timeYear <= currentYear) {//显示周几

            //测试时间戳  1460709955  对应2016.04.15  16：45：55
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 指定一个日期
            Date date = null;
            try {
                date = dateFormat.parse(timestr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 对 calendar 设置为 date 所定的日期
            calendar.setTime(date);
            int weekday = 1;
            weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            switch (weekday) {
                case 1:
                    tmp[0] = "周一";
                    break;
                case 2:
                    tmp[0] = "周二";
                    break;
                case 3:
                    tmp[0] = "周三";
                    break;
                case 4:
                    tmp[0] = "周四";
                    break;
                case 5:
                    tmp[0] = "周五️";
                    break;
                case 6:
                    tmp[0] = "周六";
                    break;
                case 7:
                    tmp[0] = "周日";
                    break;
            }
            tmp[1] = getMonthAndDay(timestamp);

        }
        else if ((timeGap > 24 * 60 * 60 || timeGap > todayGap) && timeGap < 2 * 24 * 60 * 60) {// 1天以上
            // timeStr = timeGap/(24*60*60)+"天前";
            tmp[0] = "昨天";
            tmp[1] = getStandardTimeWithHour(timestamp);
        }
        else if (timeGap < todayGap) {// 1小时-24小时
            tmp[0] = "今天";
            tmp[1] = getStandardTimeWithHour(timestamp);
        }
        return tmp;
    }

    public static String getStandardTimeWithMinute(long millions) {
        long sencond =millions/1000;
        int minutes = (int) (sencond/60);
        int seconds = (int) (sencond%60);

        return minutes+":"+seconds;
    }
    public static long getDeadLineTime(String createTime, long minutes) {
        Log.v("taglei", "create: " + createTime + "   " + minutes);
        long create = 0;
        try {
            create = Long.parseLong(createTime) * 1000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return  create + minutes * 60 * 1000 - System.currentTimeMillis();
    }
}
