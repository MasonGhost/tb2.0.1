package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;

/**
 * Created by jess on 16/4/24.
 */
public interface GoldRankView extends ListBaseView {
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
     * 显示上拉加载
     */
    void showRefreshing();

    /**
     * 显示上拉加载
     */
    void hideRefreshing();

    /**
     * 隐藏占位符
     */
    void hidePlaceHolder();


}
