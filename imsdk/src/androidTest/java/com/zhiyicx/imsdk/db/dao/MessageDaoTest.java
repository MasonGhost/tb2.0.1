package com.zhiyicx.imsdk.db.dao;

import android.test.AndroidTestCase;

import com.zhiyicx.imsdk.db.dao.soupport.MessageDaoSoupport;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageExt;
import com.zhiyicx.imsdk.utils.common.LogUtils;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */

public class MessageDaoTest extends AndroidTestCase {
    private static final String TAG = "MessageDaoTest";
    private MessageDao dbs;

    @Override
    protected void setUp() throws Exception {
        dbs = MessageDao.getInstance(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        dbs.close();
    }

    /**
     * instanceof 测试，可是使用于接口
     *
     * @throws Exception
     */
    public void testIntstanceOf() throws Exception {
        if (dbs instanceof MessageDaoSoupport) {
            org.junit.Assert.assertTrue(true);
        } else {
            org.junit.Assert.assertTrue(false);
        }
    }

    public void testInsertMessage() throws Exception {
        for (int i = 10100; i < 11100; i++) {
            Message message = new Message();
            message.id = i;
            message.cid = 1;
            MessageExt ext = new MessageExt("zbuser_11", null);
            message.ext = ext;
            message.txt = "你好" + i;
            dbs.insertOrUpdateMessage(message);
            Assert.assertEquals(true, dbs.hasMessage(0));
        }

    }

    public void testDataNums() {
        long rows = dbs.getCounts();
        System.out.println("rows = " + rows);
        Assert.assertEquals(true, rows > 1000);
    }

    public void testGetMessageListByCid() throws Exception {
        List<Message> messages = dbs.getMessageListByCid(10, 1);
        LogUtils.debugInfo(TAG, "-----Messages----" + messages);
        Assert.assertEquals("你好", messages.get(0).txt);
    }

    public void testGetLastMessageByCid() throws Exception {
        Message messages = dbs.getLastMessageByCid(10);
        LogUtils.debugInfo(TAG, "-----Messages----" + messages);
        Assert.assertEquals("你好", messages.txt);
    }

    public void testReadMessage() throws Exception {
        dbs.readMessage(9);
        List<Message> messages = dbs.getMessageListByCid(9, 1);
        Assert.assertEquals(true, messages.get(0).is_read);
    }

    /**
     * 测试大规模修改数据耗时
     *
     * @throws Exception
     */
    public void testupDate() throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Start------------ = " + start);
        int rows = dbs.upDate(1);
        long end = System.currentTimeMillis();
        System.out.println("end--------------- = " + end);

        System.out.println("rows = " + rows);
        System.out.println("times = " + (end - start));

        Assert.assertEquals(true, rows > 1000);

    }


    public void testDelDatabase() throws Exception {
        dbs.delDataBase();
    }

    public void testJavaBaseSyntx() {
        List<Message> data = new ArrayList<>();
        Message tmp = null;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                tmp = new Message();
                tmp.type = i;
            } else {
                tmp.txt = "nihao_" + i;
                data.add(tmp);
            }

        }

        System.out.println("data = " + data.toString());


    }
}