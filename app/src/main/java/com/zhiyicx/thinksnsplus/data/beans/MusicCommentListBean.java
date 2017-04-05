package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/03/16
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class MusicCommentListBean extends BaseListBean {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    /**
     * id : 4
     * created_at : 2017-03-15 07:56:22
     * updated_at : 2017-03-15 07:56:22
     * comment_content : 213123
     * user_id : 1
     * reply_to_user_id : 0
     * music_id : 1
     * special_id : 0
     */
    @Id(autoincrement = true)
    Long _id;
    private int id = -1;
    private String created_at;
    private String updated_at;
    private String comment_content;
    private int user_id;
    private int reply_to_user_id;
    private int music_id;
    private int special_id;
    private int state = SEND_SUCCESS;

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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public int getMusic_id() {
        return music_id;
    }

    public void setMusic_id(int music_id) {
        this.music_id = music_id;
    }

    public int getSpecial_id() {
        return special_id;
    }

    public void setSpecial_id(int special_id) {
        this.special_id = special_id;
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
        dest.writeString(this.updated_at);
        dest.writeString(this.comment_content);
        dest.writeInt(this.user_id);
        dest.writeInt(this.reply_to_user_id);
        dest.writeInt(this.music_id);
        dest.writeInt(this.special_id);
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public MusicCommentListBean() {
    }

    protected MusicCommentListBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.comment_content = in.readString();
        this.user_id = in.readInt();
        this.reply_to_user_id = in.readInt();
        this.music_id = in.readInt();
        this.special_id = in.readInt();
    }

    @Generated(hash = 1918542083)
    public MusicCommentListBean(Long _id, int id, String created_at, String updated_at,
                                String comment_content, int user_id, int reply_to_user_id, int
                                            music_id, int special_id,
                                int state) {
        this._id = _id;
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.comment_content = comment_content;
        this.user_id = user_id;
        this.reply_to_user_id = reply_to_user_id;
        this.music_id = music_id;
        this.special_id = special_id;
        this.state = state;
    }

    public static final Creator<MusicCommentListBean> CREATOR = new Creator<MusicCommentListBean>
            () {
        @Override
        public MusicCommentListBean createFromParcel(Parcel source) {
            return new MusicCommentListBean(source);
        }

        @Override
        public MusicCommentListBean[] newArray(int size) {
            return new MusicCommentListBean[size];
        }
    };
}
