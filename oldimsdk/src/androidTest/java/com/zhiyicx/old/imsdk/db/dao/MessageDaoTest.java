package com.zhiyicx.old.imsdk.db.dao;

import android.test.AndroidTestCase;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageExt;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jungle on 16/8/15.
 * com.zhiyicx.old.imsdk.db.dao
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

    public void testInsertMessage() throws Exception {
        for (int i = 30; i < 40; i++) {
            Message message = new Message();
            message.id = i;
            message.cid = 1;
            MessageExt ext=new MessageExt("zbuser_11",null);
            message.ext=ext;
            message.txt = "你好"+i;
            dbs.insertMessage(message);
            Assert.assertEquals(true, dbs.hasMessage(0));
        }

    }

    public void testGetMessageListByCid() throws Exception {
        List<Message> messages = dbs.getMessageListByCid(10, 1);
        LogUtils.debugInfo(TAG,"-----Messages----"+messages);
        Assert.assertEquals("你好", messages.get(0).txt);
    }
    public void testGetLastMessageByCid() throws Exception {
        Message messages = dbs.getLastMessageByCid(10);
        LogUtils.debugInfo(TAG,"-----Messages----"+messages);
        Assert.assertEquals("你好", messages.txt);
    }
    public void testReadMessage() throws Exception {
        dbs.readMessage(9);
        List<Message> messages = dbs.getMessageListByCid(9, 1);
        Assert.assertEquals(true, messages.get(0).is_read);
    }
    public void testDelDatabase() throws Exception {
        dbs.delDataBase();
    }
    public void testJavaBaseSyntx(){
        List<Message>  data=new ArrayList<>();
        Message tmp = null;
        for(int i=0;i<10;i++)
        {
            if(i%2==0)
            {
                tmp=new Message();
                tmp.type=i;
            }else {
                tmp.txt="nihao_"+i;
                data.add(tmp);
            }

        }

        System.out.println("data = " + data.toString());


    }
}