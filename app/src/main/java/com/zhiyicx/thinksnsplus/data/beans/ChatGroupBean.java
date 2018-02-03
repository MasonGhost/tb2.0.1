package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoListBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Catherine
 * @describe 群组会话的bean
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */
@Entity
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
     "group_face":
     */


    /**
     * 更新群信息返回的内容
     "groupname": "呱呱",
     "desc": "暂无",
     "public": false,
     "maxusers": "200",
     "members_only": true,
     "allowinvites": false,
     "group_face": "",
     "im_group_id": "39098857357316"
     *
     * */

    
    @Id(autoincrement = true)
    private Long key;
    @SerializedName(value = "id", alternate = {"im_group_id"})
    @Unique
    private String id;
    @SerializedName(value = "name", alternate = {"groupname"})
    private String name;
    @SerializedName(value = "description", alternate = {"desc"})
    private String description;
    /**
     * 群信息返回的权限
     */
    @SerializedName(value = "membersonly", alternate = {"members_only"})
    private boolean membersonly;
    private boolean allowinvites;
    private int maxusers;
    private long owner;
    private String created;
    private String group_face;
    private int affiliations_count;
    @Convert(columnType = String.class,converter = UserInfoListBeanConvert.class)
    private List<UserInfoBean> affiliations;
    @SerializedName("public")
    private boolean isPublic;

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

    public String getGroup_face() {
        return group_face;
    }

    public void setGroup_face(String group_face) {
        this.group_face = group_face;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ChatGroupBean() {
    }

    @Override
    public String toString() {
        return "ChatGroupBean{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", membersonly=" + membersonly +
                ", allowinvites=" + allowinvites +
                ", maxusers=" + maxusers +
                ", owner=" + owner +
                ", created='" + created + '\'' +
                ", group_face='" + group_face + '\'' +
                ", affiliations_count=" + affiliations_count +
                ", affiliations=" + affiliations +
                ", isPublic=" + isPublic +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.membersonly ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowinvites ? (byte) 1 : (byte) 0);
        dest.writeInt(this.maxusers);
        dest.writeLong(this.owner);
        dest.writeString(this.created);
        dest.writeString(this.group_face);
        dest.writeInt(this.affiliations_count);
        dest.writeTypedList(this.affiliations);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
    }

    public Long getKey() {
        return this.key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public boolean getMembersonly() {
        return this.membersonly;
    }

    public boolean getAllowinvites() {
        return this.allowinvites;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    protected ChatGroupBean(Parcel in) {
        super(in);
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.membersonly = in.readByte() != 0;
        this.allowinvites = in.readByte() != 0;
        this.maxusers = in.readInt();
        this.owner = in.readLong();
        this.created = in.readString();
        this.group_face = in.readString();
        this.affiliations_count = in.readInt();
        this.affiliations = in.createTypedArrayList(UserInfoBean.CREATOR);
        this.isPublic = in.readByte() != 0;
    }

    @Generated(hash = 2060602613)
    public ChatGroupBean(Long key, String id, String name, String description,
            boolean membersonly, boolean allowinvites, int maxusers, long owner,
            String created, String group_face, int affiliations_count,
            List<UserInfoBean> affiliations, boolean isPublic) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.description = description;
        this.membersonly = membersonly;
        this.allowinvites = allowinvites;
        this.maxusers = maxusers;
        this.owner = owner;
        this.created = created;
        this.group_face = group_face;
        this.affiliations_count = affiliations_count;
        this.affiliations = affiliations;
        this.isPublic = isPublic;
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
}
