package com.zhiyicx.zhibolibrary.ui.view;

import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by zhiyicx on 2016/3/14.
 */
public interface SplashView extends BaseView{
    /**
     * 跳转
     */
    void goTo();

    /**
     * 跳转主界面
     */
    void goHome();

    /**
     * 请求通用域名失败提示用户
     */
    void showWarn(String message, DialogInterface.OnClickListener action);



    /**
     * 开启服务
     */
    void launchService(Intent intent);

}
