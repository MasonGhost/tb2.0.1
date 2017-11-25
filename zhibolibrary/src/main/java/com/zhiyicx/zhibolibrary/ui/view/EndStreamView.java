package com.zhiyicx.zhibolibrary.ui.view;


import android.app.Activity;
import android.content.Context;

import com.zhiyicx.zhibolibrary.ui.adapter.DefaultAdapter;

/**
 * Created by zhiyicx on 2016/3/29.
 */
public interface EndStreamView extends BaseView {
    /**
     * 杀死自己
     */
    void killMyself();

    /**
     * 设置粉丝数
     *
     * @param s
     */
    void setFans(String s);

    /**
     * 设置赞的个数
     *
     * @param s
     */
    void setStar(String s);

    /**
     * 设置金币数
     *
     * @param s
     */
    void setGold(String s);


    /**
     * 显示进度条
     */
    void showLoading();

    /**
     * 隐藏进度条
     */
    void hideLoading();

    /**
     * 显示隐藏gold
     */
    void setGoldVisible(Boolean isVisible);

    /**
     * 显示隐藏star
     */
    void setStarVisible(Boolean isVisible);

    /**
     * 显示隐藏recycleView
     */
    void setRecycleViewVisible(boolean isVisible);


    void initListener();

    /**
     * 设置适配器
     */
    void setAdapter(DefaultAdapter adapter);

    /**
     * 提示用户
     */
    void showMessage(String message);

    /**
     * 是否为观众
     */
    void isAudience(boolean isAudience);

    /**
     * 设置当前主播关注状态
     */
    void setFollowStatus(boolean isFollow);

    /**
     * 显示因为网络连接异常弹出直播的提示
     */
    void showExceptionPrompt(boolean isVisable);

    Activity getActivity();
}
