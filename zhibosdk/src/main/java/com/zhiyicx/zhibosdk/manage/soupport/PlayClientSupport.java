package com.zhiyicx.zhibosdk.manage.soupport;

import com.zhiyicx.zhibosdk.manage.listener.OnVideoStartPlayListener;
import com.zhiyicx.zhibosdk.widget.ZBVideoView;

/**
 * Created by jungle on 16/7/15.
 * com.zhiyicx.zhibosdk.manage.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface PlayClientSupport extends ChatRoomIMSupport {

    /**
     * 手动继续重连
     */
    void reconnect();
    /**
     * 断开手动连接
     */
    void reStartConnect();
    /**
     * 播放直播
     * @param zbVideoView
     * @param uid         主播id
     * @param streamId    流id
     * @param l
     */
    void startLive(ZBVideoView zbVideoView,String uid, String streamId,OnVideoStartPlayListener l);

    /**
     * 播放回放
     * @param vid 回放视频id
     */
    void startVedio(ZBVideoView zbVideoView, String vid,OnVideoStartPlayListener l);

    void onResume();

    void onPause();

    void onDestroy();

}
