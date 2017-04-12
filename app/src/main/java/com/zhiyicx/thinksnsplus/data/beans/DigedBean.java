package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * @Describe   {@see https://github.com/zhiyicx/plus-component-feed/blob/master/documents/%E6%88%91%E6%94%B6%E5%88%B0%E7%9A%84%E8%B5%9E%E5%88%97%E8%A1%A8.md}
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DigedBean extends BaseListBean{
    /**
     * id : 4
     * component : feed
     * digg_table : feed_diggs
     * digg_id : 5
     * source_table : feeds
     * source_id : 17
     * user_id : 1
     * to_user_id : 1
     * created_at : 2017-04-11 02:41:42
     * updated_at : 2017-04-11 02:41:42
     */
    @Id
    private Long id; // 数据体 id
    private String component; // 数据所属扩展包名 目前可能的参数有 feed
    private String digg_table; // 点赞记录所属数据表 目前可能的参数有 feed_diggs
    private long digg_id; // 关联点赞 id
    private String source_table; // 所属资源所在表 目前可能参数有 feeds
    private long source_id; // 关联资源 id
    private long user_id; // 点赞者 id
    @ToOne(joinProperty ="user_id")
    private UserInfoBean digUserInfo;
    private long to_user_id; // 资源作者 id
    @ToOne(joinProperty ="to_user_id")
    private UserInfoBean digedUserInfo;
    private String created_at;
    private String updated_at;

    private int source_cover; // 封面 id
    private String source_content; // 资源描述
    private String comment_content; // 评论类容


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getDigg_table() {
        return digg_table;
    }

    public void setDigg_table(String digg_table) {
        this.digg_table = digg_table;
    }

    public long getDigg_id() {
        return digg_id;
    }

    public void setDigg_id(int digg_id) {
        this.digg_id = digg_id;
    }

    public String getSource_table() {
        return source_table;
    }

    public void setSource_table(String source_table) {
        this.source_table = source_table;
    }

    public long getSource_id() {
        return source_id;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(int to_user_id) {
        this.to_user_id = to_user_id;
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

    public UserInfoBean getDigUserInfo() {
        return digUserInfo;
    }

    public void setDigUserInfo(UserInfoBean digUserInfo) {
        this.digUserInfo = digUserInfo;
    }

    public UserInfoBean getDigedUserInfo() {
        return digedUserInfo;
    }

    public void setDigedUserInfo(UserInfoBean digedUserInfo) {
        this.digedUserInfo = digedUserInfo;
    }

    public int getSource_cover() {
        return source_cover;
    }

    public void setSource_cover(int source_cover) {
        this.source_cover = source_cover;
    }

    public String getSource_content() {
        return source_content;
    }

    public void setSource_content(String source_content) {
        this.source_content = source_content;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    @Override
    public String toString() {
        return "DigedBean{" +
                "id=" + id +
                ", component='" + component + '\'' +
                ", digg_table='" + digg_table + '\'' +
                ", digg_id=" + digg_id +
                ", source_table='" + source_table + '\'' +
                ", source_id=" + source_id +
                ", user_id=" + user_id +
                ", to_user_id=" + to_user_id +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.component);
        dest.writeString(this.digg_table);
        dest.writeLong(this.digg_id);
        dest.writeString(this.source_table);
        dest.writeLong(this.source_id);
        dest.writeLong(this.user_id);
        dest.writeParcelable(this.digUserInfo, flags);
        dest.writeLong(this.to_user_id);
        dest.writeParcelable(this.digedUserInfo, flags);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeInt(this.source_cover);
        dest.writeString(this.source_content);
        dest.writeString(this.comment_content);
    }

    public DigedBean() {
    }

    protected DigedBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.component = in.readString();
        this.digg_table = in.readString();
        this.digg_id = in.readInt();
        this.source_table = in.readString();
        this.source_id = in.readInt();
        this.user_id = in.readInt();
        this.digUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.to_user_id = in.readInt();
        this.digedUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.source_cover = in.readInt();
        this.source_content = in.readString();
        this.comment_content = in.readString();
    }

    public static final Creator<DigedBean> CREATOR = new Creator<DigedBean>() {
        @Override
        public DigedBean createFromParcel(Parcel source) {
            return new DigedBean(source);
        }

        @Override
        public DigedBean[] newArray(int size) {
            return new DigedBean[size];
        }
    };

    @Override
    public Long getMaxId() {
        return this.id;
    }
}
