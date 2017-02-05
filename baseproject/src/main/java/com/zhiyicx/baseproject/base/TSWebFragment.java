package com.zhiyicx.baseproject.base;

import android.content.Context;
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
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;

import static com.umeng.socialize.utils.DeviceConfig.context;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe H5 基类
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public abstract class TSWebFragment extends TSFragment {


    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    protected WebView mWebView;
    protected TextView mCloseView;
    private ProgressBar mProgressBar;
    private boolean mIsNeedProgress = true;// 是否需要进度条
    private List<String> listImgSrc = new ArrayList<>();// 网页内图片地址
    private String longClickUrl;// 长按图片的地址

    WebViewClient webViewClient = new WebViewClient() {

        /**
         * 多页面在同一个 WebView 中打开，就是不新建 activity 或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        // 网页加载结束
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // web 页面加载完成，添加监听图片的点击 js 函数
            setImageClickListner(view);
            //解析 HTML
            parseHTML(view);
            System.out.println("url = " + url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(getActivity(), "请检查您的网络设置", Toast.LENGTH_SHORT).show();
        }
    };

    // js 通信接口，定义供 JavaScript 调用的交互接口
    private class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }

        /**
         * 点击图片启动新的 ShowImageFromWebActivity，并传入点击图片对应的 url 和页面所有图片
         * 对应的 url
         *
         * @param url 点击图片对应的 url
         */
        @android.webkit.JavascriptInterface
        public void startShowImageActivity(String url) {
//            Intent intent = new Intent();
//            intent.putExtra(Constant.IMAGE_URL, url);
//            intent.putStringArrayListExtra(Constant.IMAGE_URL_ALL, (ArrayList<String>) listImgSrc);
//            intent.setClass(context, ShowImageFromWebActivity.class);
//            context.startActivity(intent);
            Toast.makeText(getActivity(), "单击图片", Toast.LENGTH_SHORT).show();
            System.out.println("listImgSrc = " + listImgSrc);
        }
    }

    /**
     * 这个接口就是给 JavaScript 调用的,调用结果就是返回 HTML 文本，
     * 然后 getAllImageUrlFromHtml(HTML)
     * 从 HTML 文件中提取页面所有图片对应的地址对象
     **/
    private class InJavaScriptLocalObj {
        /**
         * 获取 WebView 加载对应的 HTML 文本
         *
         * @param html WebView 加载对应的 HTML 文本
         */
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            //从 HTML 文件中提取页面所有图片对应的地址对象
            getAllImageUrlFromHtml(html);
        }

    }

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
            System.out.println("newProgress = " + newProgress);
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
        mCloseView.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
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

        //载入js
        mWebView.addJavascriptInterface(new MyJavascriptInterface(context), "imageListener");
        //获取 html
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        saveData(mWebSettings);
        newWin(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setWebImageLongClickListener(v);
                return false;
            }
        });
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

    /**
     * 响应长按点击事件
     *
     * @param v
     */
    private void setWebImageLongClickListener(View v) {
        if (v instanceof WebView) {
            WebView.HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    longClickUrl = result.getExtra();
                    Toast.makeText(getActivity(), "长按图片", Toast.LENGTH_SHORT).show();
                    System.out.println("longClickUrl = " + longClickUrl);
                }
            }
        }
    }

    /**
     * 解析 HTML 该方法在 setWebViewClient 的 onPageFinished 方法中进行调用
     *
     * @param view
     */
    private void parseHTML(WebView view) {
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 js 函数监听，这段 js 函数的功能就是，遍历所有的图片，并添加 onclick 函数，实现点击事件，
     * 函数的功能是在图片点击的时候调用本地java接口并传递 url 过去
     */
    private void setImageClickListner(WebView view) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        view.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.startShowImageActivity(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /***
     * 获取页面所有图片对应的地址对象，
     * 例如 <img src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     *
     * @param html WebView 加载的 html 文本
     * @return
     */
    private List<String> getAllImageUrlFromHtml(String html) {
        Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
        List<String> listImgUrl = new ArrayList<String>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        //从图片对应的地址对象中解析出 src 标签对应的内容
        getAllImageUrlFormSrcObject(listImgUrl);
        return listImgUrl;
    }

    /***
     * 从图片对应的地址对象中解析出 src 标签对应的内容,即 url
     * 例如 "http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e"
     *
     * @param listImageUrl 图片地址对象，
     *                     例如 <img src="http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e" />
     */
    private List<String> getAllImageUrlFormSrcObject(List<String> listImageUrl) {
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
            while (matcher.find()) {
                listImgSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return listImgSrc;
    }


}
