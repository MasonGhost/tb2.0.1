package com.zhiyicx.jungle;

import com.zhiyicx.imsdk.entity.Message;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.zhiyicx.imsdk.db.base.BaseDao.TIME_DEFAULT_ADD;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/20
 * @Contact master.jungle68@gmail.com
 */

public class CommonTest {

    @Test
    public void getConvertMid() throws Exception {
        long mid = 302553698851094665L;
        long result = (mid >> 23) + TIME_DEFAULT_ADD;
        System.out.println("result = " + result);

        System.out.println("getStandardTimeWithYeay(result) = " + getStandardTimeWithYeay(result));
    }

    /**
     * 测试增强 for 循环
     * @throws Exception
     */
    @Test
    public void testfor() throws Exception {
        List<Message> data=new ArrayList<>();
        data.add(new Message(1));
        data.add(new Message(2));
        data.add(new Message(3));
        for (Message message : data) {
            if (message.getId()==2){
                message.setId(40);
                message.setTxt("niaho");
                data.add(message);
                break;
            }
        }
        System.out.println("data = " + data.get(1).getTxt());
        Assert.assertTrue(40==data.get(1).getId());
    }

    /**
     * 通过时间戳获取 yyyy-MM-dd HH:mm
     *
     * @param timestamp
     * @return
     */
    public static String getStandardTimeWithYeay(long timestamp) {
        return getTime(timestamp, "yyyy-MM-dd HH:mm:ss");
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

}
