package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class InfoPublishBean implements Parcelable {
    /**
     * {@linnk https://github.com/slimkit/plus-component-news/blob/master/docs/contribute.md}
     */

    private String title;
    private String subject;
    private String content;
    private long categoryId;
    private List<UserTagBean> tags;
    private String from;
    private String author;
    private int image;
    private int amout;

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
        if (TextUtils.isEmpty(subject) && !TextUtils.isEmpty(content)) {
            if (content.length() > 200) {
                subject = content.substring(0, 200);
            } else {
                subject = content;
            }

        }
        return subject;
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
        return tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "InfoPublishBean{" +
                "title='" + title + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", categoryId=" + categoryId +
                ", tags=" + tags +
                ", from='" + from + '\'' +
                ", author='" + author + '\'' +
                ", image=" + image +
                '}';
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
        dest.writeTypedList(this.tags);
        dest.writeString(this.from);
        dest.writeString(this.author);
        dest.writeInt(this.image);
        dest.writeInt(this.amout);
    }

    public InfoPublishBean() {
    }

    protected InfoPublishBean(Parcel in) {
        this.title = in.readString();
        this.subject = in.readString();
        this.content = in.readString();
        this.categoryId = in.readLong();
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
        this.from = in.readString();
        this.author = in.readString();
        this.image = in.readInt();
        this.amout = in.readInt();
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
