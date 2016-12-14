package com.zhiyicx.common.mvp.i;

import android.content.Intent;

/**
 * @Describe view 公用接口
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public interface IBaseView {
    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent);

    /**
     * 杀死自己
     */
    void killMyself();
}
