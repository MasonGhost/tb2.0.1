package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;

/**
 * Created by jess on 16/4/26.
 */
public interface GoldHistoryView extends ListBaseView{
    /**
     * 设置适配器
     *
     * @param adapter
     */
    void setAdapter(MoreLinearAdapter adapter);


    String getType();

    /**
     * 显示占位符
     */
    void showPlaceHolder();

    /**
     * 隐藏占位符
     */
    void hidePlaceHolder();

}

