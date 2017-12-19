package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/12/19/14:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class PostDraftBean extends BaseDraftBean implements Parcelable {

    @Id
    private Long mark;
    private String html;
    @Convert(converter = CircleInfoConvert.class, columnType = String.class)
    private CircleInfo circleInfo;

    /**
     * true 圈外发帖
     */
    private boolean isOutCircle;

    /**
     * 圈子id
     */
    private Long id;

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public boolean getOutCircle() {
        return isOutCircle;
    }

    public void setOutCircle(boolean outCircle) {
        isOutCircle = outCircle;
    }

    public boolean getIsOutCircle() {
        return this.isOutCircle;
    }

    public void setIsOutCircle(boolean isOutCircle) {
        this.isOutCircle = isOutCircle;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CircleInfo getCircleInfo() {
        return circleInfo;
    }

    public void setCircleInfo(CircleInfo circleInfo) {
        this.circleInfo = circleInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.mark);
        dest.writeString(this.html);
        dest.writeParcelable(this.circleInfo, flags);
        dest.writeByte(this.isOutCircle ? (byte) 1 : (byte) 0);
        dest.writeValue(this.id);
    }

    public PostDraftBean() {
    }

    protected PostDraftBean(Parcel in) {
        super(in);
        this.mark = (Long) in.readValue(Long.class.getClassLoader());
        this.html = in.readString();
        this.circleInfo = in.readParcelable(CircleInfo.class.getClassLoader());
        this.isOutCircle = in.readByte() != 0;
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 866024632)
    public PostDraftBean(Long mark, String html, CircleInfo circleInfo, boolean isOutCircle,
            Long id) {
        this.mark = mark;
        this.html = html;
        this.circleInfo = circleInfo;
        this.isOutCircle = isOutCircle;
        this.id = id;
    }

    public static final Creator<PostDraftBean> CREATOR = new Creator<PostDraftBean>() {
        @Override
        public PostDraftBean createFromParcel(Parcel source) {
            return new PostDraftBean(source);
        }

        @Override
        public PostDraftBean[] newArray(int size) {
            return new PostDraftBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostDraftBean that = (PostDraftBean) o;

        if (mark != null ? !mark.equals(that.mark) : that.mark != null) {
            return false;
        }
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = mark != null ? mark.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public static class CircleInfoConvert extends BaseConvert<CircleInfo> {
    }
}
