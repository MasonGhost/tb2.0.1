package com.zhiyicx.zhibolibrary.ui.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class SpaceItemLinearBaseDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int bottom;
    private int left;
    private int right;

    public SpaceItemLinearBaseDecoration(int top, int bottom, int left, int right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = top;
        outRect.bottom = bottom;
        outRect.left = left;
        outRect.right =right;

    }

}
