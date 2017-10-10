package com.zhiyicx.imsdk.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.test.ServiceTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.WebSocketConnection;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.EventContainer;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageContainer;
import com.zhiyicx.imsdk.utils.common.LogUtils;

import junit.framework.Assert;

import static org.mockito.Mockito.mock;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/26
 * @Contact master.jungle68@gmail.com
 */
public class SocketServiceTest extends ServiceTestCase<SocketService> {
    private static final String TAG = "SocketServiceTest";
    IMConfig imConfig;
    Bundle bundle;

    /**
     * Constructor
     */
    public SocketServiceTest() {
        super(SocketService.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        LogUtils.debugInfo(TAG, "setUp");
        System.setProperty(
                "dexmaker.dexcache",
                getSystemContext().getCacheDir().getPath());
        imConfig = new IMConfig();
        imConfig.setImUid(1001);
        imConfig.setToken("vZ2aegWZlQVSPp9ysLrSGukDAAA");
        imConfig.setWeb_socket_authority("ws://192.168.10.222:9900");
        bundle = new Bundle();
        bundle.putSerializable(SocketService.BUNDLE_IMCONFIG, imConfig);
        startService();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        LogUtils.debugInfo(TAG, "tearDown");
        stopService();
    }


    /**
     * Test basic startup/shutdown of Service
     */
    private void startService() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        startService(startIntent);
    }

    /**
     * Test basic startup/shutdown of Service
     */
    private void stopService() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        getContext().stopService(startIntent);
    }

    /**
     * Test binding to service
     */
    @MediumTest
    public void testBindable() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), SocketService.class);
        IBinder service = bindService(startIntent);
    }

    /**
     * IM 登出
     */
    @MediumTest
    public void testLoginOut() {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LOGINOUT);
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * IM 登录
     */
    @MediumTest
    public void testLogin() {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LOGIN);
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * IM 发消息
     */
    @MediumTest
    public void testsendMessage() {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_SEND_MESSAGE);
        bundle.putSerializable(SocketService.BUNDLE_MESSAGECONTAINER, new MessageContainer(ImService.MSG, new Message()));
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * 消息去重，消息不重复的情况
     */
    @MediumTest
    public void testCheckDuplicateMessages() {
        boolean result = false;

        EventContainer eventContanter = new EventContainer();
        MessageContainer messageContainer = new MessageContainer(ImService.MSG, new Message());
        messageContainer.msg.mid = (int) (System.currentTimeMillis());
        messageContainer.msg.cid = 1000;
        eventContanter.mEvent = messageContainer.mEvent;
        eventContanter.mMessageContainer = messageContainer;
        Conversation newConversation = new Conversation();
        newConversation.setCid(eventContanter.mMessageContainer.msg.cid);
        if (!ConversationDao.getInstance(getContext()).hasConversation(newConversation.getCid())) {
            ConversationDao.getInstance(getContext()).insertConversation(newConversation);
        }
        result = getService().checkDuplicateMessages(eventContanter);
        Assert.assertFalse(result);
    }

    /**
     * 消息去重,没有创建对话的情况
     */
    @MediumTest
    public void testCheckDuplicateMessagesNOcid() {
        boolean result = false;
        getService().setService(new ImService(mock(WebSocketConnection.class)));
        EventContainer eventContanter = new EventContainer();
        MessageContainer messageContainer = new MessageContainer(ImService.MSG, new Message());
        messageContainer.msg.mid = (int) (System.currentTimeMillis());
        messageContainer.msg.cid = 10001;
        eventContanter.mEvent = messageContainer.mEvent;
        eventContanter.mMessageContainer = messageContainer;
        Conversation newConversation = new Conversation();
        newConversation.setCid(eventContanter.mMessageContainer.msg.cid);
        if (!ConversationDao.getInstance(getContext()).hasConversation(newConversation.getCid())) {
            result = getService().checkDuplicateMessages(eventContanter);
            Assert.assertTrue(result);
        }else{
            Assert.assertTrue(false);
        }
    }


    /**
     * 消息去重,消息重复的情况
     */
    @MediumTest
    public void testDuplicateMessages() {
        boolean result = false;
        EventContainer eventContanter = new EventContainer();
        MessageContainer messageContainer = new MessageContainer(ImService.MSG, new Message());
        messageContainer.msg.mid = (int) (System.currentTimeMillis());
        messageContainer.msg.cid = 1000;
        eventContanter.mEvent = messageContainer.mEvent;
        eventContanter.mMessageContainer = messageContainer;
        Conversation newConversation = new Conversation();
        newConversation.setCid(eventContanter.mMessageContainer.msg.cid);
        if (!ConversationDao.getInstance(getContext()).hasConversation(newConversation.getCid())) {
            ConversationDao.getInstance(getContext()).insertConversation(newConversation);
        }
        if (!MessageDao.getInstance(getContext()).hasMessage(messageContainer.msg.mid)) {
            MessageDao.getInstance(getContext()).insertOrUpdateMessage(messageContainer.msg);
        }
        result = getService().checkDuplicateMessages(eventContanter);
        Assert.assertTrue(result);
    }
    /**
     * IM 重连
     */
    @MediumTest
    public void testConect() {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_RECONNECT);
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * IM  加入聊天室
     */
    @MediumTest
    public void testJoinConversation() throws Exception {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_JOIN_CONVERSATION);
        bundle.putInt(SocketService.BUNDLE_ROOMID, 123);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, 123);
        bundle.putString(SocketService.BUNDLE_CONVR_PWD, "123");
        WebSocketConnection mockWebSocketConnection = mock(WebSocketConnection.class);
        getService().setService(new ImService(mockWebSocketConnection));
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * IM  加入聊天室
     */
    @MediumTest
    public void testLeaveConversation() throws Exception {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_LEAVE_CONVERSATION);
        bundle.putInt(SocketService.BUNDLE_ROOMID, 123);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, 123);
        bundle.putString(SocketService.BUNDLE_CONVR_PWD, "123");
        WebSocketConnection mockWebSocketConnection = mock(WebSocketConnection.class);
        getService().setService(new ImService(mockWebSocketConnection));
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    /**
     * IM  消息同步
     */
    @MediumTest
    public void testSyncMessage() throws Exception {
        bundle.putInt(SocketService.EVENT_SOCKET_TAG, SocketService.TAG_IM_SYNC);
        bundle.putInt(SocketService.BUNDLE_ROOMID, 10);
        bundle.putInt(SocketService.BUNDLE_MSG_GT, 12);
        bundle.putInt(SocketService.BUNDLE_MSG_LT, 15);
        bundle.putInt(SocketService.BUNDLE_MSG_ID, 1000);

        getService().setService(new ImService(new MockSocketService()));
        Assert.assertTrue(getService().dealMessage(bundle));
    }

    @MediumTest
    public void testChangeHeartBeatRate() {
        testLogin();
        LogUtils.debugInfo(TAG, "testChangeHeartBeatRate");
        // socket重连
        Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
        getContext().sendBroadcast(socketretry);
    }

    /**
     * wifi和移动网络切换
     */
    @MediumTest
    public void testNetReconnected() {
        testLogin();
        LogUtils.debugInfo(TAG, "testNetReconnected");
        // socket重连
        Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
        getContext().sendBroadcast(socketretry);
    }

}