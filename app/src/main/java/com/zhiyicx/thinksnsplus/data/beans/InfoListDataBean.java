package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.DaoException;

@Entity
public class InfoListDataBean extends BaseListBean implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;
    /**
     * id : 1
     * title : 123123
     * updated_at : 2017-03-13 09:59:32
     * storage : {"id":1,"image_width":null,"image_height":null}
     */
    @Id
    @Unique
    private Long id;
    private long user_id; // 发布者Id
    private Long info_type;
    private int is_collection_news;
    private int is_digg_news;
    private String title;
    private String content;
    private String text_content;
    private String from;
    private String created_at;
    private String updated_at;
    @Convert(converter = InfoStorageBeanConverter.class, columnType = String.class)
    private StorageBean image;
    // v2 新增的
    private int audit_status; // 审核状态 0-正常 1-待审核 2-草稿 3-驳回 4-删除 5-退款中
    private boolean is_pinned;// 是否置顶 详情中才有
    private String subject;
    private boolean has_collect;
    private boolean has_like;
    @Convert(converter = InfoCategoryConvert.class, columnType = String.class)
    private InfoCategory category;
    private boolean isTop; // 是否是置顶的资讯
    private String author; // 如果from是原创 则展示author
    private int hits;
    @Convert(converter = TagConvert.class, columnType = String.class)
    private List<UserTagBean> tags;
    private int digg_count;
    private int comment_count;
    private int is_recommend;
    private int audit_count;
    @Convert(converter = InfoDigListConvert.class, columnType = String.class)
    private List<InfoDigListBean> digList;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "info_id")})
    private List<InfoCommentListBean> commentList;
    @Convert(converter = InfoRelateListConvert.class, columnType = String.class)
    private List<InfoListDataBean> relateInfoList;


    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public int getIs_collection_news() {
        return is_collection_news;
    }

    public void setIs_collection_news(int is_collection_news) {
        this.is_collection_news = is_collection_news;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public StorageBean getImage() {
        return image;
    }

    public void setImage(StorageBean image) {
        this.image = image;
    }

    public int getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(int audit_status) {
        this.audit_status = audit_status;
    }

    public boolean is_pinned() {
        return is_pinned;
    }

    public void setIs_pinned(boolean is_pinned) {
        this.is_pinned = is_pinned;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isHas_collect() {
        return has_collect;
    }

    public void setHas_collect(boolean has_collect) {
        this.has_collect = has_collect;
    }

    public boolean isHas_like() {
        return has_like;
    }

    public void setHas_like(boolean has_like) {
        this.has_like = has_like;
    }

    public InfoCategory getCategory() {
        return category;
    }

    public void setCategory(InfoCategory category) {
        this.category = category;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public InfoListDataBean() {
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getDigg_count() {
        return digg_count;
    }

    public void setDigg_count(int digg_count) {
        this.digg_count = digg_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getIs_recommend() {
        return is_recommend;
    }

    public void setIs_recommend(int is_recommend) {
        this.is_recommend = is_recommend;
    }

    public int getAudit_count() {
        return audit_count;
    }

    public void setAudit_count(int audit_count) {
        this.audit_count = audit_count;
    }

    public List<InfoDigListBean> getDigList() {
        return digList;
    }

    public void setDigList(List<InfoDigListBean> digList) {
        this.digList = digList;
    }

    public void setCommentList(List<InfoCommentListBean> commentList) {
        this.commentList = commentList;
    }

    @Override
    public Long getMaxId() {
        return Long.parseLong(id + "");
    }

    public List<InfoListDataBean> getRelateInfoList() {
        return relateInfoList;
    }

    public void setRelateInfoList(List<InfoListDataBean> relateInfoList) {
        this.relateInfoList = relateInfoList;
    }

    public static class InfoStorageBeanConverter implements PropertyConverter<StorageBean, String> {

        @Override
        public StorageBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(StorageBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class InfoDigListConvert extends BaseConvert<List<InfoDigListBean>> {
    }

    public static class InfoRelateListConvert extends BaseConvert<List<InfoListDataBean>> {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof InfoListDataBean) {
            InfoListDataBean infoListDataBean = (InfoListDataBean) obj;
            return infoListDataBean.getId() == id;
        }
        return super.equals(obj);
    }


    public Long getInfo_type() {
        return this.info_type;
    }


    public void setInfo_type(Long info_type) {
        this.info_type = info_type;
    }

    public int getIs_digg_news() {
        return this.is_digg_news;
    }

    public void setIs_digg_news(int is_digg_news) {
        this.is_digg_news = is_digg_news;
    }

    public boolean getHas_collect() {
        return this.has_collect;
    }

    public boolean getHas_like() {
        return this.has_like;
    }

    public static class InfoCategory implements Serializable, Parcelable {
        private static final long serialVersionUID = -5033116664345775676L;
        private Long id;
        private String name;
        private int rank;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        @Override
        public String toString() {
            return "InfoCategory{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", rank=" + rank +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(this.id);
            dest.writeString(this.name);
            dest.writeInt(this.rank);
        }

        public InfoCategory() {
        }

        protected InfoCategory(Parcel in) {
            this.id = (Long) in.readValue(Long.class.getClassLoader());
            this.name = in.readString();
            this.rank = in.readInt();
        }

        public static final Creator<InfoCategory> CREATOR = new Creator<InfoCategory>() {
            @Override
            public InfoCategory createFromParcel(Parcel source) {
                return new InfoCategory(source);
            }

            @Override
            public InfoCategory[] newArray(int size) {
                return new InfoCategory[size];
            }
        };
    }

    public static class InfoCategoryConvert extends BaseConvert<InfoCategory> {
    }

    public boolean getIsTop() {
        return this.isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }


    public static class TagConvert extends BaseConvert<List<UserTagBean>> {
    }

    public List<UserTagBean> getTags() {
        return this.tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
    }


    public boolean getIs_pinned() {
        return this.is_pinned;
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
        dest.writeValue(this.info_type);
        dest.writeInt(this.is_collection_news);
        dest.writeInt(this.is_digg_news);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.text_content);
        dest.writeString(this.from);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.image, flags);
        dest.writeInt(this.audit_status);
        dest.writeByte(this.is_pinned ? (byte) 1 : (byte) 0);
        dest.writeString(this.subject);
        dest.writeByte(this.has_collect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_like ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.category, flags);
        dest.writeByte(this.isTop ? (byte) 1 : (byte) 0);
        dest.writeString(this.author);
        dest.writeInt(this.hits);
        dest.writeTypedList(this.tags);
        dest.writeInt(this.digg_count);
        dest.writeInt(this.comment_count);
        dest.writeInt(this.is_recommend);
        dest.writeInt(this.audit_count);
        dest.writeTypedList(this.digList);
        dest.writeTypedList(this.commentList);
        dest.writeTypedList(this.relateInfoList);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1050686231)
    public List<InfoCommentListBean> getCommentList() {
        if (commentList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InfoCommentListBeanDao targetDao = daoSession.getInfoCommentListBeanDao();
            List<InfoCommentListBean> commentListNew = targetDao._queryInfoListDataBean_CommentList(id);
            synchronized (this) {
                if (commentList == null) {
                    commentList = commentListNew;
                }
            }
        }
        return commentList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1195658147)
    public synchronized void resetCommentList() {
        commentList = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 338806337)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInfoListDataBeanDao() : null;
    }

    protected InfoListDataBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readLong();
        this.info_type = (Long) in.readValue(Long.class.getClassLoader());
        this.is_collection_news = in.readInt();
        this.is_digg_news = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.text_content = in.readString();
        this.from = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.image = in.readParcelable(StorageBean.class.getClassLoader());
        this.audit_status = in.readInt();
        this.is_pinned = in.readByte() != 0;
        this.subject = in.readString();
        this.has_collect = in.readByte() != 0;
        this.has_like = in.readByte() != 0;
        this.category = in.readParcelable(InfoCategory.class.getClassLoader());
        this.isTop = in.readByte() != 0;
        this.author = in.readString();
        this.hits = in.readInt();
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
        this.digg_count = in.readInt();
        this.comment_count = in.readInt();
        this.is_recommend = in.readInt();
        this.audit_count = in.readInt();
        this.digList = in.createTypedArrayList(InfoDigListBean.CREATOR);
        this.commentList = in.createTypedArrayList(InfoCommentListBean.CREATOR);
        this.relateInfoList = in.createTypedArrayList(InfoListDataBean.CREATOR);
    }

    @Generated(hash = 169543863)
    public InfoListDataBean(Long id, long user_id, Long info_type, int is_collection_news,
            int is_digg_news, String title, String content, String text_content, String from,
            String created_at, String updated_at, StorageBean image, int audit_status,
            boolean is_pinned, String subject, boolean has_collect, boolean has_like,
            InfoCategory category, boolean isTop, String author, int hits, List<UserTagBean> tags,
            int digg_count, int comment_count, int is_recommend, int audit_count,
            List<InfoDigListBean> digList, List<InfoListDataBean> relateInfoList) {
        this.id = id;
        this.user_id = user_id;
        this.info_type = info_type;
        this.is_collection_news = is_collection_news;
        this.is_digg_news = is_digg_news;
        this.title = title;
        this.content = content;
        this.text_content = text_content;
        this.from = from;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.image = image;
        this.audit_status = audit_status;
        this.is_pinned = is_pinned;
        this.subject = subject;
        this.has_collect = has_collect;
        this.has_like = has_like;
        this.category = category;
        this.isTop = isTop;
        this.author = author;
        this.hits = hits;
        this.tags = tags;
        this.digg_count = digg_count;
        this.comment_count = comment_count;
        this.is_recommend = is_recommend;
        this.audit_count = audit_count;
        this.digList = digList;
        this.relateInfoList = relateInfoList;
    }

    public static final Creator<InfoListDataBean> CREATOR = new Creator<InfoListDataBean>() {
        @Override
        public InfoListDataBean createFromParcel(Parcel source) {
            return new InfoListDataBean(source);
        }

        @Override
        public InfoListDataBean[] newArray(int size) {
            return new InfoListDataBean[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 438734104)
    private transient InfoListDataBeanDao myDao;
}

