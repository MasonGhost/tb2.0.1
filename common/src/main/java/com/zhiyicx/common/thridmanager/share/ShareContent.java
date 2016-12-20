package com.zhiyicx.common.thridmanager.share;


import java.io.Serializable;


/**
 * @Describe 分享内容
 * @Author Jungle68
 * @Date 2016/12/20
 * @Contact master.jungle68@qq.com
 */

public class ShareContent implements Serializable {

    /**
     * title : [uname]-正在直播
     * content : 我在智播直播啦!来一起看我直播吧
     * url : http://zhibo.zhiyicx.com/index/index/show?user_id=[uid]
     * image :
     */

    public String title;
    public String content;
    public String url;
    public String image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ShareContent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    /**
     * 通过用户信息获取分享数据
     *
     * @return
     */
    public static ShareContent getShareContentByUserInfo() {

        ShareContent shareContent = new ShareContent();
//        if (userInfo.uname != null) {
//            shareContent.title = ZhiboApplication.getShareContent().getTitle();
//            shareContent.title = shareContent.title.replace(STR_SHARE_NAME, userInfo.uname);
//        }
//        if (userInfo.usid != null) {
//            shareContent.url = ZhiboApplication.getShareContent().getUrl();
//            shareContent.url = shareContent.url.replace(STR_SHARE_USID, userInfo.usid);
//        }
//        if (userInfo.usid.equals(ZhiboApplication.getUserInfo().usid))
//            shareContent.content = STR_SHARE_ME + ZhiboApplication.getShareContent().getContent();
//        else
//            shareContent.content = userInfo.uname + ZhiboApplication.getShareContent().getContent();
//        shareContent.image = userInfo.avatar.origin;

        return shareContent;
    }
}
