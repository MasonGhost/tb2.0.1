package com.zhiyicx.common.thridmanager.share;


import android.app.Activity;

/**
 * @Describe  分享方针
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact master.jungle68@gmail.com
 */
public interface SharePolicy {
    /**
     * 分享朋友圈
     */
    void shareMoment(Activity activity, OnShareCallbackListener l);

    /**
     * 分享微信
     */
    void shareWechat(Activity activity, OnShareCallbackListener l);

    /**
     * 分享微博
     */
    void shareWeibo(Activity activity, OnShareCallbackListener l);

    /**
     * 分享qq
     */
    void shareQQ(Activity activity, OnShareCallbackListener l);

    /**
     * 分享qq空间
     */
    void shareZone(Activity activity, OnShareCallbackListener l);

    /**
     * 复制链接
     */
    void copyLink(Activity activity);

    /**
     * 显示分享的弹框
     */
    void showShare(Activity activity);

    /**
     * 设置分享内容
     * @param shareContent
     */
    void setShareContent(ShareContent shareContent);
}
