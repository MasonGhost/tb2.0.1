package com.zhiyicx.zhibolibrary.ui.view;

import android.graphics.drawable.Drawable;

/**
 * Created by jess on 16/4/19.
 */
public interface UserInfoView extends BaseView{


    /**
     * 设置名字
     */
    public void setName(String name);

    /**
     * 设置性别
     */
    public void setGender(int gender);

    /**
     * 设置介绍
     */
    public void setIntroduce(String intro);

    /**
     * 设置城市
     */
    public void setCity(String city);

    /**
     * 设置头像
     */
    public void setIcon(String url);

    /**
     * 设置完成按钮是否可用
     */
    public void setFinishEnable(boolean isEnable);

    /**
     * 设置编辑状态
     */
    public void isEdit(boolean isEdit);

    /**
     * 记录地区的滚轮位置
     */
    public void saveAreaPoint();

    /**
     * 打开摄像机
     */
    public void launchCamera();

    /**
     * 设置头像图片
     */
    public void setIcon(Drawable drawable);

    /**
     * 显示dialog progress
     */
    public void showProgress();

    /**
     * 隐藏dialog progress
     *
     */
    public void hideProgress();

}
