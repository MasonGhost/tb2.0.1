package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.model.entity.SearchResult;

/**
 * Created by jess on 16/4/23.
 */
public interface UserHomeView extends BaseView {
    /**
     * 获取用户信息
     *
     * @return
     */
    SearchResult getUserInfo();

    /**
     * 设置关注按钮状态
     */
    void setFollowStatus(boolean isFollow);

    /**
     * 设置关注按钮是否可用
     */
    void setFollowEnable(boolean isEnable);
    /**
     * 设置关注状态
     */
    void setFollow(boolean isFollow);
    /**
     * 获取关注状态
     */
    boolean getFollow();


}
