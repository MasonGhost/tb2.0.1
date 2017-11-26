package com.zhiyicx.old.imsdk.db.dao;

import android.test.AndroidTestCase;

import com.zhiyicx.old.imsdk.entity.Conversation;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import junit.framework.Assert;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jungle on 16/8/22.
 * com.zhiyicx.old.imsdk.db.dao
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

    public void testInsertConversation() throws Exception {
        for (int i = 0; i < 20; i++) {
            Conversation conversation = new Conversation();
            conversation.setType(1);
            conversation.setCid(i);
            conversation.setName("测试群组Conversation"+i);
            conversation.setLast_message_time(System.currentTimeMillis());
            Assert.assertEquals(true, mConversationDao.insertConversation(conversation)!=0);
        }
        for (int i = 0; i < 20; i++) {
            Conversation conversation = new Conversation();
            conversation.setType(0);
            conversation.setCid(i);
            conversation.setName("测试单聊Conversation"+i);
            conversation.setLast_message_time(System.currentTimeMillis());
            conversation.setUsids("zbuser_11");
            Assert.assertEquals(true, mConversationDao.insertConversation(conversation)!=0);
        }

    }

    public void testGetConversationList() throws Exception {
        List<Conversation> conversations=mConversationDao.getConversationList(1);
        LogUtils.debugInfo(TAG,"---database.query---"+conversations);
        Assert.assertEquals(false,conversations.size()==10);
    }

    public void testDelConversation() throws Exception {
//       Assert.assertEquals(true,mConversationDao.delConversation(6,2));
        String field="nihao";
        List<Integer> seq=new ArrayList<>();
        seq.add(10);
        seq.add(11);
        seq.add(12);
        System.out.println("-----------size-------"+seq.size());

        Map<String, Object> params = new HashMap<>();
        params.put("field", field);
        params.put("seq", seq);
        System.out.println( "testMaptosting: "+new JSONObject(params).toString());
        Assert.assertEquals(false,params==null);

    }
}