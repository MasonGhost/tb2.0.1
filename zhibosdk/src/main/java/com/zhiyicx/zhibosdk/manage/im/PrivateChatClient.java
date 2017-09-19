package com.zhiyicx.zhibosdk.manage.im;

/**
 * Created by jungle on 16/8/19.
 * com.zhiyicx.zhibosdk.manage.im
 * zhibo_android
 * email:335891510@qq.com
 */
public class PrivateChatClient {

    private volatile static PrivateChatClient sPrivateChatClient;

    private PrivateChatClient() {

    }


    public static PrivateChatClient getInstance() {

        if (sPrivateChatClient == null) {
            synchronized (PrivateChatClient.class) {
                if (sPrivateChatClient == null) {
                    sPrivateChatClient = new PrivateChatClient();
                }
            }
        }
        return sPrivateChatClient;
    }


}
