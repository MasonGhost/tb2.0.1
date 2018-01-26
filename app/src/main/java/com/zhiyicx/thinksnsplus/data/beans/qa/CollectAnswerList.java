package com.zhiyicx.thinksnsplus.data.beans.qa;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;

/**
 * @Author Jliuer
 * @Date 2018/01/04/10:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CollectAnswerList extends BaseListBean {

    /**
     * id : 7
     * user_id : 18
     * collectible_id : 28
     * collectible_type : question-answers
     * created_at : 2017-09-28 08:31:46
     * updated_at : 2017-09-28 08:31:46
     * collectible : {"id":28,"question_id":25,"user_id":93,"body":"@![image](2606)这个好用","anonymity":0,"adoption":1,"invited":0,"comments_count":1,"rewards_amount":100,"rewarder_count":1,"likes_count":1,"views_count":20,"created_at":"2017-09-25 10:05:24","updated_at":"2017-10-31 08:26:36","deleted_at":null,"text_body":null}
     */

    private Long id;
    private long user_id;
    private int collectible_id;
    private String collectible_type;
    private String created_at;
    private String updated_at;
    private AnswerInfoBean collectible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getCollectible_id() {
        return collectible_id;
    }

    public void setCollectible_id(int collectible_id) {
        this.collectible_id = collectible_id;
    }

    public String getCollectible_type() {
        return collectible_type;
    }

    public void setCollectible_type(String collectible_type) {
        this.collectible_type = collectible_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public AnswerInfoBean getCollectible() {
        return collectible;
    }

    public void setCollectible(AnswerInfoBean collectible) {
        this.collectible = collectible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.user_id);
        dest.writeInt(this.collectible_id);
        dest.writeString(this.collectible_type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.collectible, flags);
    }

    public CollectAnswerList() {
    }

    protected CollectAnswerList(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.collectible_id = in.readInt();
        this.collectible_type = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.collectible = in.readParcelable(AnswerInfoBean.class.getClassLoader());
    }

    public static final Creator<CollectAnswerList> CREATOR = new Creator<CollectAnswerList>() {
        @Override
        public CollectAnswerList createFromParcel(Parcel source) {
            return new CollectAnswerList(source);
        }

        @Override
        public CollectAnswerList[] newArray(int size) {
            return new CollectAnswerList[size];
        }
    };
}
