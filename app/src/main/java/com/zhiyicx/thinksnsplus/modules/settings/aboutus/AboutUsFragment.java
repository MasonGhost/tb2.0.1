package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSWebFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;


/**
 * @Describe 关于我们
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class AboutUsFragment extends TSWebFragment {

    private String mUrl = ApiConfig.URL_ABOUT_US;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUrl(mUrl);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.about_us);
    }


}
