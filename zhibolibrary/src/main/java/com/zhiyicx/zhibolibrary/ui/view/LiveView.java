package com.zhiyicx.zhibolibrary.ui.view;

import java.util.Map;

/**
 * Created by zhiyicx on 2016/4/7.
 */
public interface LiveView extends BaseView{


    /**
     * 设置筛选状态
     */
    void setFilterSatus(boolean isFilter);

    /**
     * 显示筛选页面
     */
    void showFilter();

    /**
     * 隐藏筛选页面
     */
    void hideFilter();
    /**
     * 隐藏筛选页面不带动画
     */
    void hideFilterNotAni();

    /**
     * 显示动画
     */
    void showAnimation();

    /**
     * 隐藏动画
     */
    void hideAnimation();

    /**
     * 开始过滤
     *
     * @param map
     */
     void startFilter(Map<String, Object> map);
}

