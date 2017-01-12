package com.zhiyicx.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */
public class TimeUtilsTest {

    @Test
    public void testGetFriendlyNormal() throws Exception {
        long now = System.currentTimeMillis()/1000;//转换s
        System.out.println("分钟前 ："+TimeUtils.getFriendlyNormal(now));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now).contains("分钟前"));
        System.out.println("分钟前 ："+TimeUtils.getFriendlyNormal(now-3590));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now-3590).contains("分钟前"));
        System.out.println("小时前 ："+TimeUtils.getFriendlyNormal(now-3610));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now-3610).contains("小时前"));
        System.out.println("昨天 ："+TimeUtils.getFriendlyNormal(now-(3600*23)));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now-(3600*23)).contains("昨天"));
        System.out.println("天前 ："+TimeUtils.getFriendlyNormal(now-(3600*24*6)));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now-3600*24*6).contains("天前"));
        System.out.println("月日 ："+TimeUtils.getFriendlyNormal(now-(3600*24*10)));
        Assert.assertTrue(TimeUtils.getFriendlyNormal(now-(3600*24*10)).contains("-"));
    }

    @Test
    public void testGetFriendlyForDetail() throws Exception {
        long now = System.currentTimeMillis()/1000;//转换s
        System.out.println("分钟前 ："+TimeUtils.getFriendlyForDetail(now));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now).contains("分钟前"));
        System.out.println("分钟前 ："+TimeUtils.getFriendlyForDetail(now-3590));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now-3590).contains("分钟前"));
        System.out.println("小时前 ："+TimeUtils.getFriendlyForDetail(now-3610));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now-3610).contains("小时前"));
        System.out.println("昨天 ："+TimeUtils.getFriendlyForDetail(now-(3600*23)));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now-(3600*23)).contains("昨天 "));
        System.out.println("天前 ："+TimeUtils.getFriendlyForDetail(now-(3600*24*6)));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now-3600*24*6).contains("天前 "));
        System.out.println("月日 ："+TimeUtils.getFriendlyForDetail(now-(3600*24*10)));
        Assert.assertTrue(TimeUtils.getFriendlyForDetail(now-(3600*24*10)).contains(":"));
    }
}