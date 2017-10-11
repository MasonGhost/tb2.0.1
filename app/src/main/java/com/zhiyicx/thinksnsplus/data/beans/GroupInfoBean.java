package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.DaoException;

/**
 * @author Catherine
 * @describe 圈子bean
 * @date 2017/7/17
 * @contact email:648129313@qq.com
 */
@Entity
public class GroupInfoBean extends BaseListBean implements Serializable {

    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    private long id;
    private String title;
    private int is_audit;
    private int posts_count;
    private int members_count;
    private String created_at;
    private String intro;
    private int is_member;
    @Convert(converter = DataConverter.class, columnType = String.class)
    private GroupCoverBean avatar; // 圈子头像
    @Convert(converter = DataConverter.class, columnType = String.class)
    private GroupCoverBean cover; // 圈子背景图
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "group_id")})
    private List<GroupManagerBean> managers;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "group_id")})
    private List<GroupManagerBean> members;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(int is_audit) {
        this.is_audit = is_audit;
    }

    public int getPosts_count() {
        return posts_count;
    }

    public void setPosts_count(int posts_count) {
        this.posts_count = posts_count;
    }

    public int getMembers_count() {
        return members_count;
    }

    public void setMembers_count(int members_count) {
        this.members_count = members_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getIs_member() {
        return is_member;
    }

    public void setIs_member(int is_member) {
        this.is_member = is_member;
    }

    public GroupCoverBean getAvatar() {
        return avatar;
    }

    public void setAvatar(GroupCoverBean avatar) {
        this.avatar = avatar;
    }

    public GroupCoverBean getCover() {
        return cover;
    }

    public void setCover(GroupCoverBean cover) {
        this.cover = cover;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setManagers(List<GroupManagerBean> managers) {
        this.managers = managers;
    }

    @Override
    public Long getMaxId() {
        return id;
    }

    @Override
    public String toString() {
        return "GroupInfoBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", is_audit=" + is_audit +
                ", posts_count=" + posts_count +
                ", members_count=" + members_count +
                ", created_at='" + created_at + '\'' +
                ", intro='" + intro + '\'' +
                ", is_member=" + is_member +
                ", avatar=" + avatar +
                ", cover=" + cover +
                ", managers=" + managers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupInfoBean that = (GroupInfoBean) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static class GroupCoverBean implements Serializable, Parcelable {

        @Transient
        private static final long serialVersionUID = 1L;
        private int raw;
        private String size = "";
        @SerializedName("id")
        private long file_id;

        public int getRaw() {
            return raw;
        }

        public void setRaw(int raw) {
            this.raw = raw;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public long getFile_id() {
            return file_id;
        }

        public void setFile_id(long file_id) {
            this.file_id = file_id;
        }

        @Override
        public String toString() {
            return "GroupCoverBean{" +
                    "raw=" + raw +
                    ", size='" + size + '\'' +
                    ", file_id=" + file_id +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.raw);
            dest.writeString(this.size);
            dest.writeLong(this.file_id);
        }

        public GroupCoverBean() {
        }

        protected GroupCoverBean(Parcel in) {
            this.raw = in.readInt();
            this.size = in.readString();
            this.file_id = in.readLong();
        }

        public static final Creator<GroupCoverBean> CREATOR = new Creator<GroupCoverBean>() {
            @Override
            public GroupCoverBean createFromParcel(Parcel source) {
                return new GroupCoverBean(source);
            }

            @Override
            public GroupCoverBean[] newArray(int size) {
                return new GroupCoverBean[size];
            }
        };
    }

    public static class DataConverter implements PropertyConverter<GroupCoverBean, String> {
        @Override
        public GroupCoverBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(GroupCoverBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1414571368)
    public synchronized void resetManagers() {
        managers = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 88310289)
    public List<GroupManagerBean> getManagers() {
        if (managers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupManagerBeanDao targetDao = daoSession.getGroupManagerBeanDao();
            List<GroupManagerBean> managersNew = targetDao._queryGroupInfoBean_Managers(id);
            synchronized (this) {
                if (managers == null) {
                    managers = managersNew;
                }
            }
        }
        return managers;
    }

    @Generated(hash = 1324884142)
    public GroupInfoBean(long id, String title, int is_audit, int posts_count, int members_count,
                         String created_at, String intro, int is_member, GroupCoverBean avatar,
                         GroupCoverBean cover) {
        this.id = id;
        this.title = title;
        this.is_audit = is_audit;
        this.posts_count = posts_count;
        this.members_count = members_count;
        this.created_at = created_at;
        this.intro = intro;
        this.is_member = is_member;
        this.avatar = avatar;
        this.cover = cover;
    }

    @Generated(hash = 1490267550)
    public GroupInfoBean() {
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2020276714)
    private transient GroupInfoBeanDao myDao;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.is_audit);
        dest.writeInt(this.posts_count);
        dest.writeInt(this.members_count);
        dest.writeString(this.created_at);
        dest.writeString(this.intro);
        dest.writeInt(this.is_member);
        dest.writeParcelable(this.avatar, flags);
        dest.writeParcelable(this.cover, flags);
        dest.writeTypedList(this.managers);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1108531735)
    public List<GroupManagerBean> getMembers() {
        if (members == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupManagerBeanDao targetDao = daoSession.getGroupManagerBeanDao();
            List<GroupManagerBean> membersNew = targetDao._queryGroupInfoBean_Members(id);
            synchronized (this) {
                if (members == null) {
                    members = membersNew;
                }
            }
        }
        return members;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1358688666)
    public synchronized void resetMembers() {
        members = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1885503356)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupInfoBeanDao() : null;
    }

    protected GroupInfoBean(Parcel in) {
        super(in);
        this.id = in.readLong();
        this.title = in.readString();
        this.is_audit = in.readInt();
        this.posts_count = in.readInt();
        this.members_count = in.readInt();
        this.created_at = in.readString();
        this.intro = in.readString();
        this.is_member = in.readInt();
        this.avatar = in.readParcelable(GroupCoverBean.class.getClassLoader());
        this.cover = in.readParcelable(GroupCoverBean.class.getClassLoader());
        this.managers = in.createTypedArrayList(GroupManagerBean.CREATOR);
    }

    public static final Creator<GroupInfoBean> CREATOR = new Creator<GroupInfoBean>() {
        @Override
        public GroupInfoBean createFromParcel(Parcel source) {
            return new GroupInfoBean(source);
        }

        @Override
        public GroupInfoBean[] newArray(int size) {
            return new GroupInfoBean[size];
        }
    };
}
