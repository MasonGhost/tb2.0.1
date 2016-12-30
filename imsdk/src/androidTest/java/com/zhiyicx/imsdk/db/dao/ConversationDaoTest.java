package com.zhiyicx.imsdk.db.dao;

import android.test.AndroidTestCase;

import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.utils.common.LogUtils;

import junit.framework.Assert;

import java.util.List;


/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.imsdk.db.dao
 * zhibo_android
 * email:335891510@qq.com
 */
public class ConversationDaoTest extends AndroidTestCase {
    private static final String TAG = "ConversationDaoTest";
    private ConversationDao mConversationDao;

    public void setUp() throws Exception {
        mConversationDao = ConversationDao.getInstance(getContext());
    }

    public void tearDown() throws Exception {
        mConversationDao.close();

    }

    /**
     * 插入对话
     *
     * @throws Exception
     */
    public void testInsertConversation() throws Exception {
        for (int i = 0; i < 20; i++) {
            Conversation conversation = new Conversation();
            conversation.setType(1);
            conversation.setCid(i);
            conversation.setName("测试群组Conversation" + i);
            conversation.setLast_message_time(System.currentTimeMillis());
            Assert.assertEquals(true, mConversationDao.insertConversation(conversation) != 0);
        }
        for (int i = 0; i < 20; i++) {
            Conversation conversation = new Conversation();
            conversation.setType(0);
            conversation.setCid(i);
            conversation.setName("测试单聊Conversation" + i);
            conversation.setLast_message_time(System.currentTimeMillis());
            conversation.setUsids("zbuser_11");
            Assert.assertEquals(true, mConversationDao.insertConversation(conversation) != 0);
        }

    }

    /**
     * 获取对话列表
     *
     * @throws Exception
     */
    public void testGetConversationList() throws Exception {
        List<Conversation> conversations = mConversationDao.getConversationList(1);
        LogUtils.debugInfo(TAG, "---database.query---" + conversations);
        Assert.assertEquals(false, conversations.size() == 10);
    }

    /**
     * 删除对话
     *
     * @throws Exception
     */
    public void testDelConversation() throws Exception {
        Assert.assertEquals(true, mConversationDao.delConversation(6, 2));
    }
}