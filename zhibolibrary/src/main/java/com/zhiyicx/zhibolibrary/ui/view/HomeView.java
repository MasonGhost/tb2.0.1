package com.zhiyicx.zhibolibrary.ui.view;

import android.content.Intent;

/**
 * Created by zhiyicx on 2016/3/28.
 */
public interface HomeView extends BaseView{



    /**
     * 跳转到直播间
     */
    void launchLiveRoom(Intent intent);

    /**
     * 跳转到直播结束页面
     */
    void launchEndStreamActivity(Intent intent);

    /**
     * 跳转页面
     * @param page
     */
    void launchPage(int page);

}
