package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailHeader {
    private static final String TAG = "InfoDetailHeader";
    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    private View mInfoDetailHeader;
    private Context mContext;

    private WebView mWebView;

    private boolean mIsLoadError;// 加载错误

    private List<String> mImageList = new ArrayList<>();// 网页内图片地址
    private String mLongClickUrl;// 长按图片的地址


    private OnWebEventListener mOnWebEventListener;


    WebViewClient mWebViewClient = new WebViewClient() {
        /**
         * 多页面在同一个 WebView 中打开，就是不新建 activity 或者调用系统浏览器打开
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl(request.getUrl().toString());
            return true;
        }

        /**
         * 网页开始加载
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mOnWebEventListener !=null){
                mOnWebEventListener.onLoadStart();
            }
            mIsLoadError = false;
            mWebView.setVisibility(View.INVISIBLE);// 当加载网页的时候将网页进行隐藏
        }

        /**
         *   网页加载结束
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mIsLoadError) {

            } else {
                mWebView.setVisibility(View.VISIBLE);
                // web 页面加载完成，添加监听图片的点击 js 函数
                setImageClickListner(view);
                //解析 HTML
                parseHTML(view);
            }
            if (mOnWebEventListener !=null){
                mOnWebEventListener.onLoadFinish();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
           LogUtils.d(TAG,"errorCode = " + errorCode);
            mIsLoadError = true;
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    };

    /**
     * js 通信接口处理图片单击，定义供 JavaScript 调用的交互接口
     */

    private class JavascriptInterfaceForImageClick {
        private Context context;

        public JavascriptInterfaceForImageClick(Context context) {
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
            if (mOnWebEventListener != null) {
                mOnWebEventListener.onWebImageClick(url, mImageList);
            }
        }
    }

    /**
     * 这个接口就是给 JavaScript 调用的,调用结果就是返回 HTML 文本，
     * 然后 getAllImageUrlFromHtml(HTML)
     * 从 HTML 文件中提取页面所有图片对应的地址对象
     **/
    private class JavaScriptForHandleHtml {
        /**
         * 获取 WebView 加载对应的 HTML 文本
         *
         * @param html WebView 加载对应的 HTML 文本
         */
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            // 从 HTML 文件中提取页面所有图片对应的地址对象
            getAllImageUrlFromHtml(html);
        }

    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {

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
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                      Message resultMsg) {
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
            setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        private void setProgress(int newProgress) {
          LogUtils.d(TAG,"newProgress = " + newProgress);
        }

        /**
         * 当WebView加载之后，返回 HTML 页面的标题 Title
         *
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            // 判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
            if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                mIsLoadError = true;
            }


        }
    };

    public InfoDetailHeader(Context context) {
        mContext = context;
        mInfoDetailHeader = LayoutInflater.from(context).inflate(
                R.layout.item_info_comment_web, null);

        mWebView = (WebView) mInfoDetailHeader.findViewById(R.id.info_detail_content);
        initWebViewData();
        mWebView.loadUrl("http://www.baidu.com");
    }

    public View getInfoDetailHeader() {
        return mInfoDetailHeader;
    }

    private void initWebViewData() {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        // 支持自动加载图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebSettings.setLoadsImagesAutomatically(true);
        } else {
            mWebSettings.setLoadsImagesAutomatically(false);
        }
        mWebSettings.setAllowFileAccess(true);

        //调用 JS 方法.安卓版本大于 17,加上注解 @JavascriptInterface
        mWebSettings.setJavaScriptEnabled(true);

        // 载入 js
        mWebView.addJavascriptInterface(new JavascriptInterfaceForImageClick(context),
                "imageListener");
        // 获取 html
        mWebView.addJavascriptInterface(new JavaScriptForHandleHtml(), "handleHtml");

        saveData(mWebSettings);
        newWin(mWebSettings);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
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

    public List<String> getAllImageUrlFromHtml(String html) {
        Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
        List<String> listImgUrl = new ArrayList<>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        //从图片对应的地址对象中解析出 src 标签对应的内容
        return getAllImageUrlFormSrcObject(listImgUrl);
    }

    /***
     * 从图片对应的地址对象中解析出 src 标签对应的内容,即 url
     * 例如 "http://sc1.hao123img.com/data/f44d0aab7bc35b8767de3c48706d429e"
     *
     * @param listImageUrl 图片地址对象，
     *                     例如 <mIvError src="http://sc1.hao123img
     *                     .com/data/f44d0aab7bc35b8767de3c48706d429e" />
     */
    private List<String> getAllImageUrlFormSrcObject(List<String> listImageUrl) {
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
            while (matcher.find()) {
                mImageList.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return mImageList;
    }

    /**
     * 解析 HTML 该方法在 setWebViewClient 的 onPageFinished 方法中进行调用
     *
     * @param view
     */
    private void parseHTML(WebView view) {
        view.loadUrl("javascript:window.handleHtml.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 js 函数监听，这段 js 函数的功能就是，遍历所有的图片，并添加 onclick 函数，实现点击事件，
     * 函数的功能是在图片点击的时候调用本地 java 接口并传递 url 过去
     */
    private void setImageClickListner(WebView view) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        view.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"mIvError\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.startShowImageActivity(this.src);  " +
                "    }  " +
                "}" +
                "})()");
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
     * HTML5 数据存储
     */
    private void saveData(WebSettings mWebSettings) {
        //有时候网页需要自己保存一些关键数据,Android WebView 需要自己设置
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        if (NetUtils.netIsConnected(mContext)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//网络不可用时只使用缓存
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//网络不可用时只使用缓存
        }
        String appCachePath = FileUtils.getCacheFilePath(mContext);
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
                if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult
                        .SRC_IMAGE_ANCHOR_TYPE) {
                    mLongClickUrl = result.getExtra();
                    if (mOnWebEventListener != null) {
                        mOnWebEventListener.onWebImageLongClick(mLongClickUrl);
                    }

                }
            }
        }
    }

    public void setOnWebEventListener(OnWebEventListener onWebEventListener) {
        mOnWebEventListener = onWebEventListener;
    }

    public interface OnWebEventListener {
        void onWebImageLongClick(String mLongClickUrl);

        void onWebImageClick(String url, List<String> mImageList);

        void onLoadFinish();

        void onLoadStart();
    }
}
