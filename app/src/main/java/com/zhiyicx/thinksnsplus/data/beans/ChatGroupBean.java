package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Catherine
 * @describe 群组会话的bean
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */

public class ChatGroupBean extends BaseListBean implements Parcelable, Serializable {

    private static final long serialVersionUID = -8073135988935750687L;

    /**{
     "id": "38389154906114",
     "name": "二三三、帅炸天",
     "description": "暂无",
     "membersonly": true,
     "allowinvites": false,
     "maxusers": 200,
     "owner": "76",
     "created": 1516009151820,
     "custom": "",
     "affiliations_count": 3,
     "affiliations": [],
     "public": false
     */

    /**
     * 创建群组用的
     */
    private String im_group_id;
    /**以下为获取群组的信息*/
    private String id;
    private String name;
    private String description;
    private boolean membersonly;
    private boolean allowinvites;
    private int maxusers;
    private long owner;
    private String created;
    private int affiliations_count;
    private List<UserInfoBean> affiliations;

    public String getIm_group_id() {
        return im_group_id;
    }

    public void setIm_group_id(String im_group_id) {
        this.im_group_id = im_group_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMembersonly() {
        return membersonly;
    }

    public void setMembersonly(boolean membersonly) {
        this.membersonly = membersonly;
    }

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getAffiliations_count() {
        return affiliations_count;
    }

    public void setAffiliations_count(int affiliations_count) {
        this.affiliations_count = affiliations_count;
    }

    public List<UserInfoBean> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<UserInfoBean> affiliations) {
        this.affiliations = affiliations;
    }

    public ChatGroupBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.im_group_id);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.membersonly ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowinvites ? (byte) 1 : (byte) 0);
        dest.writeInt(this.maxusers);
        dest.writeLong(this.owner);
        dest.writeString(this.created);
        dest.writeInt(this.affiliations_count);
        dest.writeTypedList(this.affiliations);
    }

    protected ChatGroupBean(Parcel in) {
        super(in);
        this.im_group_id = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.membersonly = in.readByte() != 0;
        this.allowinvites = in.readByte() != 0;
        this.maxusers = in.readInt();
        this.owner = in.readLong();
        this.created = in.readString();
        this.affiliations_count = in.readInt();
        this.affiliations = in.createTypedArrayList(UserInfoBean.CREATOR);
    }

    public static final Creator<ChatGroupBean> CREATOR = new Creator<ChatGroupBean>() {
        @Override
        public ChatGroupBean createFromParcel(Parcel source) {
            return new ChatGroupBean(source);
        }

        @Override
        public ChatGroupBean[] newArray(int size) {
            return new ChatGroupBean[size];
        }
    };

    @Override
    public String toString() {
        return "ChatGroupBean{" +
                "im_group_id='" + im_group_id + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", membersonly=" + membersonly +
                ", allowinvites=" + allowinvites +
                ", maxusers=" + maxusers +
                ", owner=" + owner +
                ", created='" + created + '\'' +
                ", affiliations_count=" + affiliations_count +
                ", affiliations=" + affiliations +
                '}';
    }
}
