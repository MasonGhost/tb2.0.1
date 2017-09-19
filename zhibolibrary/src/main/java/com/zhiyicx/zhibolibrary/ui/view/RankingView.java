package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;

/**
 * Created by jess on 16/4/24.
 */
public interface RankingView extends ListBaseView {
    /**
     * 设置适配器
     *
     * @param adapter
     */
    void setAdapter(MoreLinearAdapter adapter);

    /**
     * 获得排序的规则
     */
    String getOrder();

    /**
     * 显示上拉刷新
     */
    void showRefreshing();

    /**
     * 隐藏上拉刷新
     */
    void hideRefreshing();
    /**
     * 隐藏上拉加载
     */
    void hideLoadMore();

    /**
     * 显示占位符
     */
    void showPlaceHolder();


    /**
     * 隐藏占位符
     */
    void hidePlaceHolder();
}
