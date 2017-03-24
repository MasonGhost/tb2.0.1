package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailContentItem implements ItemViewDelegate<InfoCommentListBean> {
    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
                        InfoCommentListBean lastT, int position) {
        String url = "https://mp.weixin.qq.com/s?__biz=MjM5MDczMjM2MA==&mid=" +
                "2652388593&idx=1&sn=7c08949e42d61de2f022ffcee552738a" +
                "&chksm=bdacc5d68adb4cc0a038f92efca95acf17ef1facbc86553" +
                "441e8f47dbb0316104b80b6379485&scene=0&key=bda634fb2c7300a3b6" +
                "c583ff2fe7827c6ddb195c74ff4c744b765cd8a48e9f4e67360531050b8b7" +
                "addce3f97d0b9440e5a0b6bc1948c635320267447fc4b8075deffdf61e" +
                "a7ecc241bf9a9f120f378cd&ascene=0&uin=OTYwOTY3Njgw&devicetype" +
                "=iMac+MacBookPro11%2C4+OSX+OSX+10.11.6+build(15G31)&version=1" +
                "2010210&nettype=WIFI&fontScale=100&pass_ticket=kCRObwEpa%2BTF" +
                "24xhAVuiq%2FBQ2Ki1t8IcSMer1q5hQg2vFO41c4RQRrTB236TDGFU";
//                String url = "http://www.baidu.com";
//        WebView mWebView = holder.getView(R.id.item_info_comment_content);
//        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup
//                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mWebView.setLayoutParams(layoutParams);
//        WebSettings settings = mWebView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setDomStorageEnabled(true);
//        mWebView.loadUrl(url);
    }
}
