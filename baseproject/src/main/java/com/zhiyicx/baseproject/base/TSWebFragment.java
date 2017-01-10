package com.zhiyicx.baseproject.base;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @Describe H5 基类
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSWebFragment extends TSFragment {
    protected WebView mWebView;

    @Override
    protected void initData() {
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
//        mWebView.setHorizontalScrollBarEnabled(false);    //设置滑动条水平不显示
//        mWebView.setVerticalScrollBarEnabled(false);    //设置滑动条垂直不显示
//        String url = intent.getStringExtra("url");

    }

    /***
     * 加载网页
     * @param url 网页地址
     */
    protected void loadUrl(String url) {
        mWebView.loadUrl(url);
    }
//
//    /**
//     * 覆盖系统的回退键
//     *
//     * @param keyCode
//     * @param event
//     * @return
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && wv_about_us.canGoBack()) {
//            wv_about_us.goBack();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}
