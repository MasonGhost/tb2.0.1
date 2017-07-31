package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Author Jliuer
 * @Date 2017/07/31/17:14
 * @Email Jliuer@aliyun.com
 * @Description 动态列表模拟数据广告
 */
public class DynamicListAdvert {

    /**
     * avatar : 头像图|string
     * name : 用户名|string
     * content : 内容|string
     * image : 图片|string
     * time : 时间|date
     */

    private String avatar;
    private String name;
    private String content;
    private String image;
    private String time;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
