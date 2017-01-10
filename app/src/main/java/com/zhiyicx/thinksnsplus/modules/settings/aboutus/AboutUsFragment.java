package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;

import com.zhiyicx.baseproject.base.TSWebFragment;
import com.zhiyicx.thinksnsplus.R;


/**
 * @Describe 关于我们
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class AboutUsFragment extends TSWebFragment {

    private String mUrl;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    public static AboutUsFragment newInstance(String mUrl) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(AboutUsActivity.KEY_URL, mUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(AboutUsActivity.KEY_URL);
            loadUrl(mUrl);
        }else {
            throw new IllegalArgumentException("url must be setted");
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting);
    }

    @Override
    protected void initView(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.wv_about_us);
    }

    /**
     * 覆盖系统的回退键
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }
}
