package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.zhiyicx.baseproject.R;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class TBDynamicListMenuView extends DynamicListMenuView {

    public TBDynamicListMenuView(Context context) {
        super(context);
    }

    public TBDynamicListMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_dynamic_list_menu_tb, this);
    }
}
