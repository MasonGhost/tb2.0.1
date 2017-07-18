package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.functions.Action1;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;
import static com.zhiyicx.baseproject.utils.ImageUtils.DEFAULT_IMAGE_ID;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoCommentAdapter extends MultiItemTypeAdapter<InfoCommentListBean> {

    private OnWebEventListener mOnWebEventListener;
    private List<String> mImageList = new ArrayList<>();// 网页内图片地址

    // 获取img标签正则
    private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
    // 获取src路径的正则
    private static final String IMAGE_URL_CONTENT = "http:\"?(.*?)(\"|>|\\s+)";

    private String mLongClickUrl;// 长按图片的地址
    private boolean mIsNeedProgress = false;// 是否需要进度条
    private boolean mIsLoadError;// 加载错误
    private ProgressBar mProgressBar;

    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
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
            setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        private void setProgress(int newProgress) {
            if (!mIsNeedProgress) {
                return;
            }
            if (newProgress == mContext.getResources().getInteger(com.zhiyicx.baseproject.R.integer
                    .progressbar_max)) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                if (View.GONE == mProgressBar.getVisibility()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
                mProgressBar.setProgress(newProgress);
            }
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

    public InfoCommentAdapter(Context context, List<InfoCommentListBean> datas) {
        super(context, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resId;
        if (viewType == 0) {
            resId = R.layout.item_info_comment_web;
        } else if (viewType == 2) {
            resId = R.layout.item_dynamic_detail_comment;
        } else {
            resId = R.layout.item_dynamic_detail_comment_empty;
        }
        final ViewHolder holder = ViewHolder.createViewHolder(mContext, parent,
                resId);
        if (viewType != 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnWebEventListener.onItemClick(v, holder, holder.getAdapterPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0) {
            viewType = 0;
        } else if (TextUtils.isEmpty(mDatas.get(position).getComment_content())) {
            viewType = 1;
        } else {
            viewType = 2;
        }
        return viewType;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InfoCommentListBean infoCommentListBean = mDatas.get(position);
        if (position == 0) {
            WebView web = holder.getView(R.id.info_detail_content);
            initWebViewData(web);
            web.loadUrl(String.format(APP_DOMAIN + APP_PATH_INFO_DETAILS_FORMAT, mDatas.get
                    (position).getId()));
            holder.setText(R.id.tv_comment_count,
                    mContext.getResources().getString(R.string.dynamic_comment_count,
                            mDatas.size() - 1));// 减去第一条，头部web
        } else if (TextUtils.isEmpty(mDatas.get(position).getComment_content())) {
            EmptyView emptyView = holder.getView(R.id.comment_emptyview);
            emptyView.setNeedTextTip(false);
            emptyView.setErrorType(EmptyView.STATE_NODATA_ENABLE_CLICK);
        } else {
            int storegeId;
            String userIconUrl;
            try {
                storegeId = Integer.parseInt(infoCommentListBean.getFromUserInfoBean().getAvatar());
                userIconUrl = ImageUtils.imagePathConvertV2(storegeId
                        , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                        , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                        , ImageZipConfig.IMAGE_26_ZIP);
            } catch (Exception e) {
                userIconUrl = infoCommentListBean.getFromUserInfoBean().getAvatar();
            }
            AppApplication.AppComponentHolder.getAppComponent()
                    .imageLoader()
                    .loadImage(holder.getConvertView().getContext(), GlideImageConfig.builder()
                            .url(userIconUrl)
                            .placeholder(R.drawable.shape_default_image_circle)
                            .transformation(new GlideCircleTransform(holder.getConvertView()
                                    .getContext()))
                            .errorPic(R.drawable.shape_default_image_circle)
                            .imagerView(holder.getView(R.id.iv_headpic))
                            .build()
                    );
            holder.setText(R.id.tv_name, infoCommentListBean.getFromUserInfoBean().getName());
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(infoCommentListBean
                    .getCreated_at()));
            holder.setText(R.id.tv_content, setShowText(infoCommentListBean, position));
        }
    }

    private void initWebViewData(WebView mWebView) {
        WebSettings mWebSettings = mWebView.getSettings();
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

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });
    }

    protected String setShowText(InfoCommentListBean infoCommentListBean, int position) {
        return handleName(infoCommentListBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final InfoCommentListBean
            dynamicCommentBean, int position) {
        List<Link> links = new ArrayList<>();
        return links;
    }

    /**
     * 处理名字的颜色与点击
     *
     * @param infoCommentListBean
     * @return
     */
    private String handleName(InfoCommentListBean infoCommentListBean) {
        String content = "";
        if (infoCommentListBean.getReply_to_user_id() != 0) { // 当没有回复者时，就是回复评论
            content += " 回复 " + infoCommentListBean.getToUserInfoBean().getName() + ": " +
                    infoCommentListBean.getComment_content();
        } else {
            content = infoCommentListBean.getComment_content();
        }
        return content;
    }

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener
                                                       onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public interface OnWebEventListener {
        void onWebImageLongClick(String mLongClickUrl);

        void onWebImageClick(String url, List<String> mImageList);

        void onLoadFinish();

        void onLoadStart();

        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }
}
