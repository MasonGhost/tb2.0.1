package com.zhiyicx.zhibosdk.manage;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;
import com.zhiyicx.zhibosdk.manage.soupport.ConversationManagerSoupport;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBBaseJson;

import java.util.HashMap;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/8/18.
 * com.zhiyicx.zhibosdk.manage
 * zhibo_android
 * email:335891510@qq.com
 */
public class ZBConversationManager implements ConversationManagerSoupport {
    private static final int CONVERSATION_TYPE_PRIVATE_CHAT = 0;
    private static final int CONVERSATION_TYPE_TEAM_CHAT = 1;

    private volatile static ZBConversationManager sConversationManager;

    private ZBConversationManager() {

    }


    public static ZBConversationManager getInstance() {

        if (sConversationManager == null) {
            synchronized (ZBConversationManager.class) {
                if (sConversationManager == null) {
                    sConversationManager = new ZBConversationManager();
                }
            }
        }
        return sConversationManager;
    }

    /**
     * 创建私聊
     *
     * @param toUsid   发起聊天对方的标识
     * @param callback
     */
    @Override
    public void createPrivateChat(String toUsid, ZBCloudApiCallback callback) {
        createConversation(CONVERSATION_TYPE_PRIVATE_CHAT, toUsid, null, null, callback);

    }

    /**
     * @param toUsid 发起聊天对方的标识
     * @return
     */
    @Override
    public Observable<JsonObject> createPrivateChatForRx(String toUsid) {
        return createConversationForRx(CONVERSATION_TYPE_PRIVATE_CHAT, toUsid, null, null);
    }


    /**
     * 创建群组
     *
     * @param toUsid           发起聊天对方的标识
     * @param conversationName 群组的名字
     * @param pwd              该群组的加入密码,默认为空
     * @param callback
     */
    @Override
    public void createTeamChat(String toUsid, String conversationName, String pwd, ZBCloudApiCallback callback) {
        createConversation(CONVERSATION_TYPE_TEAM_CHAT, toUsid, conversationName, pwd, callback);
    }

    /**
     * @param toUsids          发起聊天对方的标识
     * @param conversationName
     * @param pwd
     * @return
     */
    @Override
    public Observable<JsonObject> createTeamChatForRx(String toUsids, String conversationName, String pwd) {
        return createConversationForRx(CONVERSATION_TYPE_TEAM_CHAT, toUsids, conversationName, pwd);
    }


    private void createConversation(int type, String toUsid, String conversationName, String pwd, final ZBCloudApiCallback callback) {
        createConversationForRx(type, toUsid, conversationName, pwd).subscribe(new Action1<JsonObject>() {
            @Override
            public void call(JsonObject jsonObject) {
                if (callback != null)
                    callback.onResponse(jsonObject.toString());
                ZBBaseJson<Conversation> result = new Gson().fromJson(jsonObject, new TypeToken<ZBBaseJson<Conversation>>() {
                }.getType());
                if (result.code.equals(ZBApi.REQUEST_SUCESS)) {
                }


            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (callback != null)
                    callback.onError(throwable);
            }
        });
    }

    private Observable<JsonObject> createConversationForRx(int type, String toUsid, String conversationName, String pwd) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("usids", toUsid);
        if (!TextUtils.isEmpty(conversationName))
            params.put("name", conversationName);
        if (!TextUtils.isEmpty(pwd))
            params.put("pwd", pwd);
        return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBApi.API_CREATE_CONVERSATION, params).subscribeOn(Schedulers.io());
    }
}
