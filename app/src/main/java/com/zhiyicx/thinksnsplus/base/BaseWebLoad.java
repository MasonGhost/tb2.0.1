package com.zhiyicx.thinksnsplus.base;

import android.os.Message;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/10/25
 * @Contact master.jungle68@gmail.com
 */
public class BaseWebLoad {

    protected OnWebLoadListener mWebLoadListener;

    public void setWebLoadListener(OnWebLoadListener webLoadListener) {
        mWebLoadListener = webLoadListener;
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {

        /**
         * 多窗口的问题
         *
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
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100 && mWebLoadListener != null) {
                mWebLoadListener.onLoadFinish();

            }
        }

    };

    protected void destryWeb(WebView webView) {
        if (webView != null) {
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
        }
    }

    public interface OnWebLoadListener {
        void onLoadFinish();

    }
}
