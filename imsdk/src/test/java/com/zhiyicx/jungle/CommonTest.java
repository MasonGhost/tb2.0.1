package com.zhiyicx.jungle;

import com.zhiyicx.imsdk.entity.Message;

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
        System.out.println("mid>>23 = " +( mid >> 23));
        long result = (mid >> 23) + TIME_DEFAULT_ADD;
        System.out.println("result = " + result);

        System.out.println("getStandardTimeWithYeay(result) = " + getStandardTimeWithYeay(result));
    }

    /**
     * 测试增强 for 循环
     * 增强 for 循环的缺点， 不能拿到当前的 position ； 不能增加或者删除数据（除非操作之后直接 break）
     *
     * @throws Exception
     */
    @Test
    public void testfor() throws Exception {
        List<Message> data = new ArrayList<>();
        data.add(new Message(2));
        data.add(new Message(1));
        data.add(new Message(3));
//        for (Message message : data) {
//            if (message.getId() == 2) {
//                message.setId(40);
//                message.setTxt("niaho");
//                data.add(message);
//            }
//        }
//        int j=1;
//        for (int i = 0; i < data.size(); i++) {
//            if (j++==1) {
//                data.add(0,new Message(10));
//
//                System.out.println(data.size());
//            }
//        }

        for (Message message : data) {
            System.out.println("data = " + message.getId());
        }

//        Assert.assertTrue(40 == data.get(1).getId());
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

    @Test
    public void getArraylst() {
        ArrayList<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        data.add(3);
        data.add(4);
        ArrayList<Integer> datab = new ArrayList<>();
        datab = data;
        datab.add(5);
        System.out.println("datab = " + datab);
//        datab.addAll(data);
//        datab.add(5);
//        data.clear();
//        data=datab;
//        System.out.println("data = " + data);
        for (Integer tmp : datab) {
            if (tmp == 5) {
                tmp = 6;
            }
        }
        System.out.println("datab = " + datab);
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            messages.add(new Message(i));
        }
        for (Message message : messages) {
            if (message.getId() == 5) {
                message.setTxt("123456");
            }
        }
        for (Message message : messages) {
            if (null != message.getTxt()) {
                System.out.println("message = " + message.getTxt());
            }
        }

    }

    @Test
    public void getListAdd() {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i);
        }
        data.add(3, 99);
        System.out.println("data = " + data);
    }
}
