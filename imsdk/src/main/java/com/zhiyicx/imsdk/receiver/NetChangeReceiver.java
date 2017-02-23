package com.zhiyicx.imsdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.imsdk.service.SocketService;

/**
 * Created by jungle on 16/5/21.
 * com.zhiyicx.zhibo.app.receiver
 * zhibo_android
 * email:335891510@qq.com
 */
public class NetChangeReceiver extends BroadcastReceiver {
    private static final String NETWORK_CHANGE_SATE = "android.net.conn.CONNECTIVITY_CHANGE";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (NETWORK_CHANGE_SATE.equals(intent.getAction())) {
            // socket重连
            Intent socketretry = new Intent(SocketService.SOCKET_RETRY_CONNECT);
            context.sendBroadcast(socketretry);


        }
    }
}
