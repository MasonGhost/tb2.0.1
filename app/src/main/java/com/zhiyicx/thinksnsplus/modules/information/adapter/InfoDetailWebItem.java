package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class InfoDetailWebItem implements ItemViewDelegate<InfoCommentListBean> {


    private OnWebEventListener mOnWebEventListener;
    private List<String> mImageList = new ArrayList<>();// 网页内图片地址
    private Context mContext;

    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    private String mLongClickUrl;// 长按图片的地址
    private boolean mIsNeedProgress = false;// 是否需要进度条
    private boolean mIsLoadError;// 加载错误


    WebViewClient mWebViewClient = new WebViewClient() {
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

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mOnWebEventListener != null) {
                mOnWebEventListener.onLoadStart();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mIsLoadError) {

            } else {
                // web 页面加载完成，添加监听图片的点击 js 函数
                setImageClickListner(view);
                //解析 HTML
                parseHTML(view);
            }
            if (mOnWebEventListener != null) {
                mOnWebEventListener.onLoadFinish();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
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

        @android.webkit.JavascriptInterface
        public void startShowImageActivity(String url) {
            ToastUtils.showToast(url);
            if (mOnWebEventListener != null) {
                mOnWebEventListener.onWebImageClick(url, mImageList);
            }
        }
    }

    private class JavaScriptForHandleHtml {

        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            getAllImageUrlFromHtml(html);
        }

    }

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture,
                                      Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            // 判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
            if (!TextUtils.isEmpty(title) && title.toLowerCase().contains("error")) {
                mIsLoadError = true;
            }
        }
    };

    public List<String> getAllImageUrlFromHtml(String html) {
        Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
        List<String> listImgUrl = new ArrayList<>();
        while (matcher.find()) {
            listImgUrl.add(matcher.group());
        }
        //从图片对应的地址对象中解析出 src 标签对应的内容
        return getAllImageUrlFormSrcObject(listImgUrl);
    }

    private List<String> getAllImageUrlFormSrcObject(List<String> listImageUrl) {
        for (String image : listImageUrl) {
            Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
            while (matcher.find()) {
                mImageList.add(matcher.group().substring(0, matcher.group().length() - 1));
            }
        }
        return mImageList;
    }

    private void parseHTML(WebView view) {
        view.loadUrl("javascript:window.handleHtml.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

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

    private void newWin(WebSettings mWebSettings) {
        //html中的_bank标签就是新建窗口打开，有时会打不开，需要加以下
        //然后 复写 WebChromeClient的onCreateWindow方法
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

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

    public InfoDetailWebItem(Context context, OnWebEventListener onWebEventListener) {
        mOnWebEventListener = onWebEventListener;
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_info_comment_web;
    }

    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return position == 0;
    }

    @Override
    public void convert(ViewHolder holder, InfoCommentListBean infoCommentListBean,
                        InfoCommentListBean lastT, int position,int itemCounts) {
        MarkdownView web = holder.getView(R.id.info_detail_content);
//        initWebViewData(web);
        initMarkDownView(web);
//        String url = String.format(APP_DOMAIN + APP_PATH_INFO_DETAILS_FORMAT,
//                infoCommentListBean.getId());
//        web.loadUrl(url);
//        LogUtils.d("convertUrl:::" + url);
        dealInfoHeader(holder);
        dealInfoDigList(holder);
        dealCommentCount(holder);
        dealRewards(holder);
        dealInfoWebContent(web);
    }

    private void initMarkDownView(MarkdownView web) {
        InternalStyleSheet css = new Github();
        web.addStyleSheet(css);
    }

    protected abstract void dealRewards(ViewHolder holder);

    public abstract void dealCommentCount(ViewHolder holder);

    public abstract void dealInfoHeader(ViewHolder holder);

    public abstract void dealInfoDigList(ViewHolder holder);

    public abstract void dealInfoWebContent(MarkdownView markdownView);

    private void initWebViewData(WebView mWebView) {
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);

        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSupportZoom(true);
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
        mWebView.addJavascriptInterface(new JavascriptInterfaceForImageClick(mContext),
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
