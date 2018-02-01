package com.zhiyicx.thinksnsplus.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.zhiyicx.baseproject.base.TSFragment;

/**
 * @author Jliuer
 * @Date 18/02/01 11:46
 * @Email Jliuer@aliyun.com
 * @Description 环信 fragment 基类，继承 ts+ 基类
 */
public abstract class TSEaseBaseFragment extends TSFragment{
    protected InputMethodManager inputMethodManager;

    @Override
    protected void initView(View rootView) {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        initEMView(rootView);
    }

    @Override
    protected void initData() {
        setUpView();
    }
    
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    
    protected abstract void initEMView(View rootView);
    
    protected abstract void setUpView();


}
