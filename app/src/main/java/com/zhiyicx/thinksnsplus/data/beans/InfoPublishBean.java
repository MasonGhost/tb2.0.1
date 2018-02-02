package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class InfoPublishBean implements Parcelable {

    public static final String DEFALUT_SUBJECT = ">**[摘要]** ";

    /**
     * {@linnk https://slimkit.github.io/docs/api-v2-news-contribute.html}
     * title	String	必须，标题，最长 20 个字。
     * subject	String	主题，副标题，概述，最长 200 个字。
     * content	String	必须，内容。
     * image	Integer	缩略图。
     * tags	string,array	必须 标签id，多个id以逗号隔开或传入数组形式
     * from	String	资讯来源。
     * author	String	作者
     * text_content	string	纯文本字段
     */


    private String title;
    private String subject;
    private String content;
    private long categoryId;
    @Expose
    private List<UserTagBean> ids;
    private String tags;
    @Expose
    private String categoryName;
    private String from;
    private String author;
    private Long image;
    @Expose
    private int cover;
    @Expose
    private int news_id;
    private int amout;
    @Expose
    private boolean isRefuse;
    private String text_content;


    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public boolean isRefuse() {
        return isRefuse;
    }

    public void setRefuse(boolean refuse) {
        isRefuse = refuse;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIds() {
        return tags;
    }

    public void setIds(String ids) {
        this.tags = ids;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public List<UserTagBean> getTags() {
        return ids;
    }

    public void setTags(List<UserTagBean> tags) {
        if (tags == null) {
            return;
        }
        this.ids = tags;
        StringBuilder builder = new StringBuilder();
        for (UserTagBean tag : tags) {
            builder.append(tag.getId());
            builder.append(",");
        }
        setIds(builder.toString());
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getImage() {
        return image;
    }

    public void setImage(Long image) {
        this.image = image;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.subject);
        dest.writeString(this.content);
        dest.writeLong(this.categoryId);
        dest.writeTypedList(this.ids);
        dest.writeString(this.tags);
        dest.writeString(this.categoryName);
        dest.writeString(this.from);
        dest.writeString(this.author);
        dest.writeValue(this.image);
        dest.writeInt(this.cover);
        dest.writeInt(this.news_id);
        dest.writeInt(this.amout);
        dest.writeByte(this.isRefuse ? (byte) 1 : (byte) 0);
        dest.writeString(this.text_content);
    }

    public InfoPublishBean() {
    }

    protected InfoPublishBean(Parcel in) {
        this.title = in.readString();
        this.subject = in.readString();
        this.content = in.readString();
        this.categoryId = in.readLong();
        this.ids = in.createTypedArrayList(UserTagBean.CREATOR);
        this.tags = in.readString();
        this.categoryName = in.readString();
        this.from = in.readString();
        this.author = in.readString();
        this.image = (Long) in.readValue(Long.class.getClassLoader());
        this.cover = in.readInt();
        this.news_id = in.readInt();
        this.amout = in.readInt();
        this.isRefuse = in.readByte() != 0;
        this.text_content = in.readString();
    }

    public static final Creator<InfoPublishBean> CREATOR = new Creator<InfoPublishBean>() {
        @Override
        public InfoPublishBean createFromParcel(Parcel source) {
            return new InfoPublishBean(source);
        }

        @Override
        public InfoPublishBean[] newArray(int size) {
            return new InfoPublishBean[size];
        }
    };
}
