package com.zhiyicx.baseproject.base;

import android.widget.Toolbar;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.base.BaseActivity;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public abstract class TSActivity extends BaseActivity {
    Toolbar mToolbar;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ts;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
