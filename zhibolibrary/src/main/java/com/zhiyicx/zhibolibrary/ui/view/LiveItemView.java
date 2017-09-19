package com.zhiyicx.zhibolibrary.ui.view;

import com.zhiyicx.zhibolibrary.ui.adapter.MoreAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;

import java.util.Map;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public interface LiveItemView extends BaseView{

    /**
     * 显示上拉加载
     */
    void showRefreshing();

    /**
     * 显示上拉加载
     */
    void hideRefreshing();


    /**
     * 子类实现，区分列表的类型
     *
     * @return
     */
    String getOrder();

    /**
     * 设置表格适配器
     *
     * @param adapter
     */
    void setAdapter(MoreAdapter adapter);


    /**
     * 设置线性适配器
     *
     * @param adapter
     */
    void setMoreLineAdapter(MoreLinearAdapter adapter);


    /**
     * 控制是否筛选
     *
     * @return
     */
    public boolean isFilter();

    /**
     * 设置是否筛选
     *
     * @param isFilter
     */
    public void setIsFilter(boolean isFilter);

    /**
     * 设置筛选所要的参数
     *
     * @return
     */
    public Map<String, Object> getFilterValue();

    /**
     * 获取回放所要的order
     *
     * @return
     */
    String getVideoOreder();

    /**
     * 获取uid
     */
    String getUid();
    /**
     * 获取usid
     */
    String getUsid();

    /**
     * 设置uid
     */
    void setUid(String uid);

    /**
     * 显示没有关注的占位符
     */
    void showNotFollowPH();

    /**
     * 显示什么都没有的占位符
     */
    void shwoNothingPH();

    /**
     * 显示筛选时没有数据的占位符
     */
    void showFilterNothingPH();

    /**
     * 显示网络状况不佳的占位符
     */
    void showNetBadPH();

    /**
     * 隐藏占位符
     */
    void hidePlaceHolder();
}

