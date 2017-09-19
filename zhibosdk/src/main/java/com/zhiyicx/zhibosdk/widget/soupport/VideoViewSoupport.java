package com.zhiyicx.zhibosdk.widget.soupport;

/**
 * Created by jungle on 16/7/7.
 * com.zhiyicx.zhibosdk.widget.soupport
 * zhibo_android
 * email:335891510@qq.com
 */
public interface VideoViewSoupport {
    void init();

    /**
     * 调用在setVideoPath之后
     * 不需要调用start,准备好自动播放
     * @param b
     * @param path
     */
    void isNeedStartOnPrepared(boolean b,String path);

}
