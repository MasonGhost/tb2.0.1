package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public interface SearchTabView extends BaseView {


    /**
     * 显示上拉加载
     */
    void showRefreshing();

    /**
     * 显示上拉加载
     */
    void hideRefreshing();


    /**
     * 设置适配器
     *
     * @param adapter
     */
    void setAdapter(MoreLinearAdapter adapter);


    /**
     * 获得type
     */
    String getType();

    /**
     * 显示没搜索到信息的占位符
     */
    void showPlaceHolder();

    /**
     * 显示网络状况不佳的占位符
     */
    void showNetBadPH();

    /**
     * 隐藏没搜索到信息的占位符
     */
    void hidePlaceHolder();
}
