package com.zhiyicx.baseproject.base;

import android.graphics.Bitmap;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.FileUtils;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe H5 基类
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSWebFragment extends TSFragment {
    protected WebView mWebView;
    protected TextView mCloseView;
    private ProgressBar mProgressBar;
    private boolean mIsNeedProgress = true;// 是否需要进度条


    WebViewClient webViewClient = new WebViewClient() {

        /**
         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    };
    WebChromeClient webChromeClient = new WebChromeClient() {

        //=========HTML5定位==========================================================
        //需要先加入权限
        //<uses-permission android:name="android.permission.INTERNET"/>
        //<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
        //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

        /**
         * 网站图标回调
         * @param view
         * @param icon
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * HTML5定位
         */
        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        /**
         * HTML5定位
         * @param origin
         * @param callback
         */
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);//注意个函数，第二个参数就是是否同意定位权限，第三个是是否希望内核记住
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        /**
         * 多窗口的问题
         * @param view
         * @param isDialog
         * @param isUserGesture
         * @param resultMsg
         * @return
         */
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }

        /**
         * 进度条
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        private void setProgress(int newProgress) {
            if (!mIsNeedProgress) {
                return;
            }
            if (newProgress == getResources().getInteger(R.integer.progressbar_max)) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (View.GONE == mProgressBar.getVisibility()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
        }
    };

    @Override
    protected int getToolBarLayoutId() {
        return R.layout.toolbar_for_web;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragme_ts_web;
    }

    @Override
    protected void initDefaultToolBar(View toolBarContainer) {
        super.initDefaultToolBar(toolBarContainer);
        mCloseView = (TextView) toolBarContainer.findViewById(R.id.tv_toolbar_left_right);
        mCloseView.setVisibility(View.INVISIBLE);
        mCloseView.setTextColor(ContextCompat.getColor(getContext(),android.R.color.black));
        mCloseView.setText(getString(R.string.close));
        RxView.clicks(mCloseView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            mCloseView.setVisibility(View.VISIBLE);
        } else {
            super.setLeftClick();
        }
    }

    @Override
    protected void initView(View rootView) {
        mWebView = (WebView) rootView.findViewById(R.id.wv_about_us);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_bar);
    }


    @Override
    protected void initData() {
        initWebViewData();
    }

    private void initWebViewData() {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);

        //调用JS方法.安卓版本大于17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);

        saveData(mWebSettings);

        newWin(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
    }

    /***
     * 加载网页
     *
     * @param url 网页地址
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 是否需要进度条
     *
     * @param needProgress
     */
    public void setNeedProgress(boolean needProgress) {
        mIsNeedProgress = needProgress;
    }

    /**
     * @return
     */
    public boolean isNeedProgress() {
        return mIsNeedProgress;
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getCurrentProgress() {
        return mProgressBar.getProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers(); //小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    /**
     * 多窗口的问题
     */
    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    /**
     * HTML5数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = FileUtils.getCacheFilePath(getContext());
        mWebSettings.setAppCachePath(appCachePath);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }

    /**
     * 覆盖系统的回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            mCloseView.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}
