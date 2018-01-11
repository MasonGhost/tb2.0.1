package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.IntegerListConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/12/19/14:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class PostDraftBean extends BaseDraftBean implements Parcelable, Serializable {

    private static final long serialVersionUID = -703234318472876036L;
    @Id
    private Long mark;
    private String html;
    private String title;
    private String content;
    private String create_at;
    private String updated_at;
    @Convert(converter = CircleInfoConvert.class, columnType = String.class)
    private CircleInfo circleInfo;

    /**
     * true 圈外发帖
     */
    private boolean isOutCircle;

    /**
     * true 同步至动态
     */
    private boolean hasSynToDynamic;

    /**
     * 圈子id
     */
    private Long id;

    /**
     * 记录上传成功的照片 键值对：时间戳(唯一) -- 图片地址
     */
    @Convert(converter = HashMapConvert.class,columnType = String.class)
    private HashMap<Long, String> mInsertedImages;

    /**
     * 记录上传失败的照片 同上
     */
    @Convert(converter = HashMapConvert.class,columnType = String.class)
    private HashMap<Long, String> mFailedImages;

    /**
     * 上传图片成功后返回的id
     */
    @Convert(columnType = String.class,converter = IntegerListConvert.class)
    private List<Integer> mImages;

    public HashMap<Long, String> getInsertedImages() {
        return mInsertedImages;
    }

    public void setInsertedImages(HashMap<Long, String> insertedImages) {
        mInsertedImages = insertedImages;
    }

    public HashMap<Long, String> getFailedImages() {
        return mFailedImages;
    }

    public void setFailedImages(HashMap<Long, String> failedImages) {
        mFailedImages = failedImages;
    }

    public List<Integer> getImages() {
        return mImages;
    }

    public void setImages(List<Integer> images) {
        mImages = images;
    }

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

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean getOutCircle() {
        return isOutCircle;
    }

    public void setOutCircle(boolean outCircle) {
        isOutCircle = outCircle;
    }

    public boolean hasSynToDynamic() {
        return hasSynToDynamic;
    }

    public void setHasSynToDynamic(boolean hasSynToDynamic) {
        this.hasSynToDynamic = hasSynToDynamic;
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

    public boolean getHasSynToDynamic() {
        return this.hasSynToDynamic;
    }

    public PostDraftBean() {
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
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.create_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.circleInfo, flags);
        dest.writeByte(this.isOutCircle ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasSynToDynamic ? (byte) 1 : (byte) 0);
        dest.writeValue(this.id);
        dest.writeSerializable(this.mInsertedImages);
        dest.writeSerializable(this.mFailedImages);
        dest.writeList(this.mImages);
    }

    public HashMap<Long, String> getMInsertedImages() {
        return this.mInsertedImages;
    }

    public void setMInsertedImages(HashMap<Long, String> mInsertedImages) {
        this.mInsertedImages = mInsertedImages;
    }

    public HashMap<Long, String> getMFailedImages() {
        return this.mFailedImages;
    }

    public void setMFailedImages(HashMap<Long, String> mFailedImages) {
        this.mFailedImages = mFailedImages;
    }

    public List<Integer> getMImages() {
        return this.mImages;
    }

    public void setMImages(List<Integer> mImages) {
        this.mImages = mImages;
    }

    protected PostDraftBean(Parcel in) {
        super(in);
        this.mark = (Long) in.readValue(Long.class.getClassLoader());
        this.html = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.create_at = in.readString();
        this.updated_at = in.readString();
        this.circleInfo = in.readParcelable(CircleInfo.class.getClassLoader());
        this.isOutCircle = in.readByte() != 0;
        this.hasSynToDynamic = in.readByte() != 0;
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mInsertedImages = (HashMap<Long, String>) in.readSerializable();
        this.mFailedImages = (HashMap<Long, String>) in.readSerializable();
        this.mImages = new ArrayList<Integer>();
        in.readList(this.mImages, Integer.class.getClassLoader());
    }

    @Generated(hash = 1713560363)
    public PostDraftBean(Long mark, String html, String title, String content,
            String create_at, String updated_at, CircleInfo circleInfo, boolean isOutCircle,
            boolean hasSynToDynamic, Long id, HashMap<Long, String> mInsertedImages,
            HashMap<Long, String> mFailedImages, List<Integer> mImages) {
        this.mark = mark;
        this.html = html;
        this.title = title;
        this.content = content;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.circleInfo = circleInfo;
        this.isOutCircle = isOutCircle;
        this.hasSynToDynamic = hasSynToDynamic;
        this.id = id;
        this.mInsertedImages = mInsertedImages;
        this.mFailedImages = mFailedImages;
        this.mImages = mImages;
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

    public static class HashMapConvert extends BaseConvert<HashMap<Long, String>>{}
}
