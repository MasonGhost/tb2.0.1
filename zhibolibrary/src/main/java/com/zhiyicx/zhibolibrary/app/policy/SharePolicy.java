package com.zhiyicx.zhibolibrary.app.policy;

import com.zhiyicx.zhibolibrary.model.entity.ShareContent;

/**
 * Created by jess on 16/5/25.
 */
public interface SharePolicy {
    /**
     * 分享朋友圈
     */
    void shareMoment();

    /**
     * 分享微信
     */
    void shareWechat();

    /**
     * 分享微博
     */
    void shareWeibo();

    /**
     * 分享qq
     */
    void shareQQ();

    /**
     * 分享qq空间
     */
    void shareZone();

    /**
     * 显示分享的弹框
     */
    void showShare();

    /**
     * 分享内容
     * @param shareContent
     */
    void setShareContent(ShareContent shareContent);
}
