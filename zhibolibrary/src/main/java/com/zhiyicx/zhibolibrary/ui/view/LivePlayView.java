package com.zhiyicx.zhibolibrary.ui.view;

import com.zhiyicx.zhibosdk.manage.ZBPlayClient;
import com.zhiyicx.zhibosdk.widget.ZBMediaPlayer;
import com.zhiyicx.zhibosdk.widget.ZBVideoView;
import com.zhiyicx.zhibosdk.widget.soupport.ZBMediaController;

/**
 * Created by zhiyicx on 2016/4/1.
 */
public interface LivePlayView extends BaseView {

    ZBVideoView getZBVideoView();

    ZBPlayClient getZBplayClient();

    /**
     * 现实播放核心页面
     */
    void showCore();

    /**
     * 设置默认图片
     */
    void setPlaceHolder(String url);

    /**
     * 设置播放器监听器
     */
    void setListener(

            ZBMediaPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener
    );

    /**
     * 设置placeholder是否可见
     *
     * @param isVisible
     */
    void setPlaceHolderVisible(boolean isVisible);

    /**
     * 设置播放控制器
     *
     * @param mediaController
     */
    void setMediaController(ZBMediaController mediaController);

    /**
     * 设置提示消息
     */
    void setWarnMessage(String warn);

    /**
     * 提示信息
     */
    void showWarn();


    /**
     * 显示主播信息
     */
    void showPresenterInfo();

    /**
     * 播放完
     */
    void playComplete();

    /**
     * 隐藏提示信息
     */
    void hideWarn();

    /**
     * 设置重播蒙层是否可见
     *
     * @param isVisable
     */
    void setCoverVisable(boolean isVisable);

    /**
     * 播放器是否开始播放视频
     */
    boolean isPlaying();

    /**
     * 设置关注按钮状态
     */
    void setFollowEnable(boolean isEnable);

    /**
     * 设置关注
     *
     * @param isFollow
     */
    void setFollow(boolean isFollow);
}
