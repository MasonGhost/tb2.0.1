package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSWebFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;

import java.util.List;


/**
 * @Describe 关于我们
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class CustomWEBFragment extends TSWebFragment {
    public static final String BUNDLE_PARAMS_WEB_URL = "web_url";
    public static final String BUNDLE_PARAMS_WEB_TITLE = "web_title";

    private String mUrl = ApiConfig.URL_ABOUT_US;
    private String mTitle = "";

    public CustomWEBFragment() {
        // Required empty public constructor
    }

    public static CustomWEBFragment newInstance(Bundle bundle) {
        CustomWEBFragment fragment = new CustomWEBFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getString(R.string.about_us);
        if (getArguments() != null) {
            mUrl = getArguments().getString(BUNDLE_PARAMS_WEB_URL);
            mTitle = getArguments().getString(BUNDLE_PARAMS_WEB_TITLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUrl(mUrl);
    }

    @Override
    protected String setCenterTitle() {
        return mTitle;
    }


    @Override
    protected void onWebImageClick(String clickUrl, List<String> images) {

    }

    @Override
    protected void onWebImageLongClick(String longClickUrl) {

    }
}
