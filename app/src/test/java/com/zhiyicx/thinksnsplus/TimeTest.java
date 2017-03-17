package com.zhiyicx.thinksnsplus;

import com.zhiyicx.common.utils.TimeUtils;

import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */

public class TimeTest {

    /**
     * utc 时间测试
     */
    @Test
    public void finalTest() {
        System.out.println(System.currentTimeMillis());
        String time = "2017-03-17 8:28:56";
        System.out.println("TimeUtils.converTime(time, TimeZone.getDefault()) = " + TimeUtils.utc2LocalStr(time));
        System.out.println("TimeUtils.getTimeFriendlyForDetail() = " + TimeUtils.getTimeFriendlyForDetail(TimeUtils.utc2LocalStr(time)));

        java.util.Calendar ca2 = java.util.Calendar.getInstance();
        System.out.println("ca2 = " + ca2.getTimeInMillis());
    }

    /**
     * 获取当前 0 时区时间测试
     */
    @Test
    public void getCurrenZeroTime() {
        System.out.println("cal str = " + TimeUtils.getCurrenZeroTimeStr());
        System.out.println("cal  long= " + TimeUtils.getCurrenZeroTime());

    }
}
