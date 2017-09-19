package com.zhiyicx.zhibolibrary.app.policy.impl;

import android.content.Context;
import android.widget.Toast;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.policy.SharePolicy;
import com.zhiyicx.zhibolibrary.model.entity.ShareContent;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by jess on 16/6/7.
 */
public class SharePolicyImpl implements SharePolicy {


    private ShareContent mShareContent;
    private Context mContext;

    public SharePolicyImpl(Context mContext) {
        this.mContext = mContext;
        ShareSDK.initSDK(mContext.getApplicationContext());
    }

    public SharePolicyImpl(ShareContent mShareContent, Context mContext) {
        this.mShareContent = mShareContent;
        this.mContext = mContext;
        ShareSDK.initSDK(mContext.getApplicationContext());
    }

    public ShareContent getShareContent() {
        return mShareContent;
    }

    /**
     * 设置分享内容
     *
     * @param shareContent
     */
    @Override
    public void setShareContent(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    /**
     * 微信朋友分享
     */
    @Override
    public void shareMoment() {
        if (checkShareContent()) return;

        OnekeyShare share = new OnekeyShare();
        share.disableSSOWhenAuthorize();
        share.setText(mShareContent.getContent());
        // text是分享文本，所有平台都需要这个字段
        share.setTitle(mShareContent.getTitle());
        // url仅在微信（包括好友和朋友圈）中使用
        share.setUrl(mShareContent.getUrl());
        share.setTitleUrl(mShareContent.getUrl());
        share.setImageUrl(mShareContent.getImage());
        share.setPlatform(WechatMoments.NAME);
        share.show(mContext.getApplicationContext());

    }

    /**
     * QQ分享
     */
    @Override
    public void shareQQ() {
        if (checkShareContent()) return;
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(mShareContent.getTitle());
        sp.setText(mShareContent.getContent());
        sp.setTitleUrl(mShareContent.getUrl());
        sp.setImageUrl(mShareContent.getImage());
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.share(sp);
    }

    /**
     * QQ空间分享
     */
    @Override
    public void shareZone() {
        if (checkShareContent()) return;
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle(mShareContent.getTitle());
        sp.setText(mShareContent.getContent());
        sp.setTitleUrl(mShareContent.getUrl()); // 标题的超链接
        sp.setImageUrl(mShareContent.getImage());

        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
        qzone.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        qzone.share(sp);
    }

    /**
     * 微博分享
     */
    @Override
    public void shareWeibo() {
        if (checkShareContent()) return;
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(mShareContent.getContent() + "  " + mShareContent.getUrl());
        sp.setImageUrl(mShareContent.getImage());

        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }

    /**
     * 微信分享
     */
    @Override
    public void shareWechat() {
        if (checkShareContent()) return;
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(mShareContent.getTitle());
        sp.setText(mShareContent.getContent());
        sp.setImageUrl(mShareContent.getImage());
        sp.setUrl(mShareContent.getUrl());
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        }); // 设置分享事件回调
// 执行图文分享
        wechat.share(sp);
    }


    /**
     * 分享弹框
     */
    @Override
    public void showShare() {
        if (checkShareContent()) return;
        ShareSDK.initSDK(mContext.getApplicationContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mShareContent.getTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mShareContent.getUrl());
        // text是分享文本，所有平台都需要这个字段

        oks.setText(mShareContent.getContent());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setImageUrl(mShareContent.getImage());
        oks.setUrl(mShareContent.getUrl());
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mContext.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.zhiyicx.com");
        // 启动分享GUI
        oks.show(mContext);


    }

    /**
     * 检测分享内容是否为空
     *
     * @return
     */
    private boolean checkShareContent() {
        if (mShareContent == null) {
            Toast.makeText(mContext, "分享内容为空!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
