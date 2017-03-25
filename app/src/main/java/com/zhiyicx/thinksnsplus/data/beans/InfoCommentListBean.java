package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoCommentListBean extends BaseListBean {

    /**
     * id : 3
     * created_at : 2017-03-13 16:35:33
     * comment_content : 爱我的
     * user_id : 1
     * reply_to_user_id : 0
     */

    private int id;
    private String created_at;
    private String comment_content;
    private int user_id;
    private int reply_to_user_id;
    private long comment_mark;

    public long getComment_mark() {
        return comment_mark;
    }

    public void setComment_mark(long comment_mark) {
        this.comment_mark = comment_mark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReply_to_user_id() {
        return reply_to_user_id;
    }

    public void setReply_to_user_id(int reply_to_user_id) {
        this.reply_to_user_id = reply_to_user_id;
    }

    public InfoCommentListBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.comment_content);
        dest.writeInt(this.user_id);
        dest.writeInt(this.reply_to_user_id);
        dest.writeLong(this.comment_mark);
    }

    protected InfoCommentListBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.created_at = in.readString();
        this.comment_content = in.readString();
        this.user_id = in.readInt();
        this.reply_to_user_id = in.readInt();
        this.comment_mark = in.readLong();
    }

    public static final Creator<InfoCommentListBean> CREATOR = new Creator<InfoCommentListBean>() {
        @Override
        public InfoCommentListBean createFromParcel(Parcel source) {
            return new InfoCommentListBean(source);
        }

        @Override
        public InfoCommentListBean[] newArray(int size) {
            return new InfoCommentListBean[size];
        }
    };
}
