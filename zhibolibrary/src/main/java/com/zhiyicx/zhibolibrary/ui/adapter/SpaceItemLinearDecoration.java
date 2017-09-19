package com.zhiyicx.zhibolibrary.ui.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class SpaceItemLinearDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceItemLinearDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;

    }

}
