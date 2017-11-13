package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/18/14:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class GroupDynamicListBean extends BaseListBean {
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;

    public static final int IS_DIGG = 1;
    public static final int UN_DIGG = 0;

    public static final int IS_COLLECT = 1;
    public static final int UN_COLLECT = 0;
    /**
     * id : 2
     * title : hehehsdfasdfasdf
     * content : hahahahha
     * group_id : 1
     * views : 3
     * diggs : 0
     * collections : 0
     * comments_count : 4
     * user_id : 2
     * is_audit : 1
     * created_at : 2017-07-18 04:17:19
     * updated_at : 2017-07-18 06:49:18
     * images : [{"raw":"2","size":"1200x800","file_id":3},{"raw":"2","size":"600x1065","file_id":4}]
     * group : {"id":1,"title":"heheh","intro":"hahahahha","is_audit":1,"posts_count":2,"memebers_count":1,"created_at":"2017-07-18 03:51:40","avatar":{"raw":"1","size":"1920x1080","file_id":1},"cover":{"raw":"1","size":"600x600","file_id":2},"members":[{"id":1,"user_id":2,"created_at":"2017-07-18 03:51:40"}],"managers":[{"group_id":1,"user_id":2,"founder":1}]}
     * new_comments : [{"id":4,"user_id":2,"content":"sdfasdqerwerxxxxxxxasdfasdfasdf234234234234234a","reply_to_user_id":0,"created_at":"2017-07-18 06:48:29","to_user_id":2},{"id":3,"user_id":2,"content":"xxxxxxxasdfasdfasdf234234234234234","reply_to_user_id":0,"created_at":"2017-07-18 06:48:24","to_user_id":2},{"id":2,"user_id":2,"content":"xxxxxxxasdfasdfasdf","reply_to_user_id":0,"created_at":"2017-07-18 06:48:21","to_user_id":2},{"id":1,"user_id":2,"content":"xxxxxxx","reply_to_user_id":0,"created_at":"2017-07-18 06:48:12","to_user_id":2}]
     */
    @Id
    private Long id;
    private String title;
    private String content;
    private int group_id;
    private int views;
    private int diggs;

    private boolean has_like;
    private int collections;
    private boolean has_collection;
    private int comments_count;
    private Long user_id;
    @SerializedName(value = "feed_mark",alternate = {"group_post_mark"})
    private Long feed_mark;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean userInfoBean;
    private int is_audit;
    private String created_at;
    private String updated_at;
    @Convert(converter = GroupDynamicImageConvert.class, columnType = String.class)
    private List<ImagesBean> images;
    @Convert(converter = GroupDynamicCommentConvert.class, columnType = String.class)
    @SerializedName(value = "commentslist",alternate = {"comments"})
    private List<GroupDynamicCommentListBean> commentslist;
    @Convert(converter = GroupDynamicLikesConvert.class, columnType = String.class)
    private List<DynamicDigListBean> mGroupDynamicLikeListBeanList;
    private int state = SEND_ING;



    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Long getMaxId() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    @Keep
    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    @Keep
    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
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

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getDiggs() {
        return diggs;
    }

    public void setDiggs(int diggs) {
        this.diggs = diggs;
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(int is_audit) {
        this.is_audit = is_audit;
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

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<GroupDynamicCommentListBean> getNew_comments() {
        return commentslist;
    }

    public void setNew_comments(List<GroupDynamicCommentListBean> new_comments) {
        this.commentslist = new_comments;
    }

    public static class ImagesBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 124L;
        /**
         * raw : 2
         * size : 1200x800
         * file_id : 3
         */

        private String raw;
        private String size;
        private int width;
        private int propPart;
        private int height;
        @SerializedName("id")
        private int file_id;
        private String imgUrl;
        private String type;

        public int getPropPart() {
            return propPart;
        }

        public void setPropPart(int propPart) {
            this.propPart = propPart;
        }

        public String getSize() {
            return size;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getImgMimeType() {
            return type;
        }

        public void setImgMimeType(String type) {
            this.type = type;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public void setSize(String size) {
            this.size = size;
            if (size != null && size.length() > 0) {
                String[] sizes = size.split("x");
                this.width = Integer.parseInt(sizes[0]);
                this.height = Integer.parseInt(sizes[1]);
            }
        }

        public int getWidth() {
            if (size != null && size.length() > 0) {
                String[] sizes = size.split("x");
                return Integer.parseInt(sizes[0]);
            }
            return 100;
        }

        public int getHeight() {
            if (size != null && size.length() > 0) {
                String[] sizes = size.split("x");
                return Integer.parseInt(sizes[1]);
            }
            return 100;
        }

        public int getFile_id() {
            return file_id;
        }

        public void setFile_id(int file_id) {
            this.file_id = file_id;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.raw);
            dest.writeString(this.size);
            dest.writeInt(this.width);
            dest.writeInt(this.propPart);
            dest.writeInt(this.height);
            dest.writeInt(this.file_id);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.raw = in.readString();
            this.size = in.readString();
            this.width = in.readInt();
            this.propPart = in.readInt();
            this.height = in.readInt();
            this.file_id = in.readInt();
        }

        public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }

    public static class GroupDynamicImageConvert extends BaseConvert<List<ImagesBean>> {
    }

    public static class GroupDynamicCommentConvert extends BaseConvert<List<GroupDynamicCommentListBean>> {
    }

    public static class GroupDynamicLikesConvert extends BaseConvert<List<DynamicDigListBean>> {
    }

    public List<GroupDynamicCommentListBean> getCommentslist() {
        return this.commentslist;
    }

    public void setCommentslist(List<GroupDynamicCommentListBean> commentslist) {
        this.commentslist = commentslist;
    }

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public List<DynamicDigListBean> getMGroupDynamicLikeListBeanList() {
        return this.mGroupDynamicLikeListBeanList;
    }

    public void setMGroupDynamicLikeListBeanList(List<DynamicDigListBean> mGroupDynamicLikeListBeanList) {
        this.mGroupDynamicLikeListBeanList = mGroupDynamicLikeListBeanList;
    }

    public boolean getHas_like() {
        return this.has_like;
    }

    public void setHas_like(boolean has_like) {
        this.has_like = has_like;
    }

    public boolean getHas_collection() {
        return this.has_collection;
    }

    public void setHas_collection(boolean has_collection) {
        this.has_collection = has_collection;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeInt(this.group_id);
        dest.writeInt(this.views);
        dest.writeInt(this.diggs);
        dest.writeByte(this.has_like ? (byte) 1 : (byte) 0);
        dest.writeInt(this.collections);
        dest.writeByte(this.has_collection ? (byte) 1 : (byte) 0);
        dest.writeInt(this.comments_count);
        dest.writeValue(this.user_id);
        dest.writeValue(this.feed_mark);
        dest.writeParcelable(this.userInfoBean, flags);
        dest.writeInt(this.is_audit);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.commentslist);
        dest.writeTypedList(this.mGroupDynamicLikeListBeanList);
        dest.writeInt(this.state);
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

    public GroupDynamicListBean() {
    }

    protected GroupDynamicListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.content = in.readString();
        this.group_id = in.readInt();
        this.views = in.readInt();
        this.diggs = in.readInt();
        this.has_like = in.readByte() != 0;
        this.collections = in.readInt();
        this.has_collection = in.readByte() != 0;
        this.comments_count = in.readInt();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.is_audit = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.images = in.createTypedArrayList(ImagesBean.CREATOR);
        this.commentslist = in.createTypedArrayList(GroupDynamicCommentListBean.CREATOR);
        this.mGroupDynamicLikeListBeanList = in.createTypedArrayList(DynamicDigListBean.CREATOR);
        this.state = in.readInt();
    }

    @Generated(hash = 1275723709)
    public GroupDynamicListBean(Long id, String title, String content, int group_id, int views, int diggs, boolean has_like, int collections, boolean has_collection, int comments_count, Long user_id, Long feed_mark, int is_audit, String created_at, String updated_at, List<ImagesBean> images, List<GroupDynamicCommentListBean> commentslist, List<DynamicDigListBean> mGroupDynamicLikeListBeanList, int state) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.group_id = group_id;
        this.views = views;
        this.diggs = diggs;
        this.has_like = has_like;
        this.collections = collections;
        this.has_collection = has_collection;
        this.comments_count = comments_count;
        this.user_id = user_id;
        this.feed_mark = feed_mark;
        this.is_audit = is_audit;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.images = images;
        this.commentslist = commentslist;
        this.mGroupDynamicLikeListBeanList = mGroupDynamicLikeListBeanList;
        this.state = state;
    }

    public static final Creator<GroupDynamicListBean> CREATOR = new Creator<GroupDynamicListBean>() {
        @Override
        public GroupDynamicListBean createFromParcel(Parcel source) {
            return new GroupDynamicListBean(source);
        }

        @Override
        public GroupDynamicListBean[] newArray(int size) {
            return new GroupDynamicListBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 699919709)
    private transient GroupDynamicListBeanDao myDao;
    @Generated(hash = 1005780391)
    private transient Long userInfoBean__resolvedKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupDynamicListBean that = (GroupDynamicListBean) o;

        if (group_id != that.group_id) return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + group_id;
        return result;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1201375789)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupDynamicListBeanDao() : null;
    }
}
