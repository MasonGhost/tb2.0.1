package com.zhiyicx.thinksnsplus.modules.develop;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Describe 开发提示页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class TSDevelopFragment extends TSFragment {
    public static final String BUNDLE_TITLE = "TITLE";
    public static final String BUNDLE_CENTER_IMAGE = "CENTER_IMAGE";

    private ImageView mIvTip;


    public static TSDevelopFragment newInstance(String title, int centerResId) {
        TSDevelopFragment fragment = new TSDevelopFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_TITLE, title);
        args.putInt(BUNDLE_CENTER_IMAGE, centerResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mIvTip = (ImageView) rootView.findViewById(R.id.iv_tip);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            setCenterText(getArguments().getString(BUNDLE_TITLE));
            mIvTip.setImageResource(getArguments().getInt(BUNDLE_CENTER_IMAGE));
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_ts_develop;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }


}
