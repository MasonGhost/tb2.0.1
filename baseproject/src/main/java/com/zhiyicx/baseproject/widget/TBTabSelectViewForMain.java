package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.R;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/1
 * @contact master.jungle68@gmail.com
 */
public class TBTabSelectViewForMain extends TabSelectView {
    public TBTabSelectViewForMain(Context context) {
        super(context);
    }

    public TBTabSelectViewForMain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TBTabSelectViewForMain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayout() {
        return R.layout.toolbar_for_viewpager_tb;
    }
}
