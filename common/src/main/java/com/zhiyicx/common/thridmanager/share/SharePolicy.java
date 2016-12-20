package com.zhiyicx.common.thridmanager.share;


/**
 * @Describe  分享方针
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact master.jungle68@qq.com
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
     * 设置分享内容
     * @param shareContent
     */
    void setShareContent(ShareContent shareContent);
}
