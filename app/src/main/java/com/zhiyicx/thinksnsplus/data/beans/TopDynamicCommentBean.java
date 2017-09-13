package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopDynamicCommentBean extends BaseListBean {

    public static final int TOP_REFUSE = 0;
    public static final int TOP_REVIEWING = 1;
    public static final int TOP_SUCCESS = 2;


    /**
     * id : 4
     * channel : comment
     * target : 1
     * user_id : 1
     * amount : 1
     * day : 3
     * expires_at : null
     * created_at : 2017-07-21 03:47:09
     * updated_at : 2017-07-21 03:47:09
     * target_user : 1
     * raw : 1
     * feed : {"id":1,"user_id":1,"feed_content":"动态内容","feed_from":1,"like_count":1,"feed_view_count":0,"feed_comment_count":6,"feed_latitude":null,"feed_longtitude":null,"feed_geohash":null,"audit_status":1,"feed_mark":1,"pinned":0,"created_at":"2017-06-27 07:04:32","updated_at":"2017-07-20 08:53:24","deleted_at":null,"pinned_amount":0,"images":[],"paid_node":null}
     * comment : {"id":1,"user_id":1,"target_user":1,"reply_user":0,"body":"我是第一条评论","commentable_id":1,"commentable_type":"feeds","created_at":"2017-07-20 08:34:41","updated_at":"2017-07-20 08:34:41"}
     */

    private Long id;
    private String channel;
    private int target;
    private Long user_id;
    private int amount;
    private int day;
    private String expires_at;
    private String created_at;
    private String updated_at;
    private int target_user;
    private int raw;
    private FeedBean feed;
    private CommentedBean comment;
    private UserInfoBean userInfoBean;

    @Override
    public Long getMaxId() {
        return id;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
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

    public int getTarget_user() {
        return target_user;
    }

    public void setTarget_user(int target_user) {
        this.target_user = target_user;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public FeedBean getFeed() {
        return feed;
    }

    public void setFeed(FeedBean feed) {
        this.feed = feed;
    }

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

    public static class FeedBean implements Parcelable,Serializable{

        private static final long serialVersionUID = 1461771570674420147L;
        /**
         * id : 1
         * user_id : 1
         * feed_content : 动态内容
         * feed_from : 1
         * like_count : 1
         * feed_view_count : 0
         * feed_comment_count : 6
         * feed_latitude : null
         * feed_longtitude : null
         * feed_geohash : null
         * audit_status : 1
         * feed_mark : 1
         * pinned : 0
         * created_at : 2017-06-27 07:04:32
         * updated_at : 2017-07-20 08:53:24
         * deleted_at : null
         * pinned_amount : 0
         * images : []
         * paid_node : null
         */

        private int id;
        private Long user_id;
        private String feed_content;
        private int feed_from;
        private int like_count;
        private int feed_view_count;
        private int feed_comment_count;
        private String feed_latitude;
        private String feed_longtitude;
        private String feed_geohash;
        private int audit_status;
        private Long feed_mark;
        private int pinned;
        private String created_at;
        private String updated_at;
        private String deleted_at;
        private int pinned_amount;
        private PaidNote paid_node;
        private List<DynamicDetailBeanV2.ImagesBean> images;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }

        public String getFeed_content() {
            return feed_content;
        }

        public void setFeed_content(String feed_content) {
            this.feed_content = feed_content;
        }

        public int getFeed_from() {
            return feed_from;
        }

        public void setFeed_from(int feed_from) {
            this.feed_from = feed_from;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getFeed_view_count() {
            return feed_view_count;
        }

        public void setFeed_view_count(int feed_view_count) {
            this.feed_view_count = feed_view_count;
        }

        public int getFeed_comment_count() {
            return feed_comment_count;
        }

        public void setFeed_comment_count(int feed_comment_count) {
            this.feed_comment_count = feed_comment_count;
        }

        public String getFeed_latitude() {
            return feed_latitude;
        }

        public void setFeed_latitude(String feed_latitude) {
            this.feed_latitude = feed_latitude;
        }

        public String getFeed_longtitude() {
            return feed_longtitude;
        }

        public void setFeed_longtitude(String feed_longtitude) {
            this.feed_longtitude = feed_longtitude;
        }

        public String getFeed_geohash() {
            return feed_geohash;
        }

        public void setFeed_geohash(String feed_geohash) {
            this.feed_geohash = feed_geohash;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        public Long getFeed_mark() {
            return feed_mark;
        }

        public void setFeed_mark(Long feed_mark) {
            this.feed_mark = feed_mark;
        }

        public int getPinned() {
            return pinned;
        }

        public void setPinned(int pinned) {
            this.pinned = pinned;
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

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public int getPinned_amount() {
            return pinned_amount;
        }

        public void setPinned_amount(int pinned_amount) {
            this.pinned_amount = pinned_amount;
        }

        public PaidNote getPaid_node() {
            return paid_node;
        }

        public void setPaid_node(PaidNote paid_node) {
            this.paid_node = paid_node;
        }

        public List<DynamicDetailBeanV2.ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<DynamicDetailBeanV2.ImagesBean> images) {
            this.images = images;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeValue(this.user_id);
            dest.writeString(this.feed_content);
            dest.writeInt(this.feed_from);
            dest.writeInt(this.like_count);
            dest.writeInt(this.feed_view_count);
            dest.writeInt(this.feed_comment_count);
            dest.writeString(this.feed_latitude);
            dest.writeString(this.feed_longtitude);
            dest.writeString(this.feed_geohash);
            dest.writeInt(this.audit_status);
            dest.writeLong(this.feed_mark);
            dest.writeInt(this.pinned);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeString(this.deleted_at);
            dest.writeInt(this.pinned_amount);
            dest.writeParcelable(this.paid_node, flags);
            dest.writeTypedList(this.images);
        }

        public FeedBean() {
        }

        protected FeedBean(Parcel in) {
            this.id = in.readInt();
            this.user_id = (Long) in.readValue(Long.class.getClassLoader());
            this.feed_content = in.readString();
            this.feed_from = in.readInt();
            this.like_count = in.readInt();
            this.feed_view_count = in.readInt();
            this.feed_comment_count = in.readInt();
            this.feed_latitude = in.readString();
            this.feed_longtitude = in.readString();
            this.feed_geohash = in.readString();
            this.audit_status = in.readInt();
            this.feed_mark = in.readLong();
            this.pinned = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.deleted_at = in.readString();
            this.pinned_amount = in.readInt();
            this.paid_node = in.readParcelable(PaidNote.class.getClassLoader());
            this.images = in.createTypedArrayList(DynamicDetailBeanV2.ImagesBean.CREATOR);
        }

        public static final Creator<FeedBean> CREATOR = new Creator<FeedBean>() {
            @Override
            public FeedBean createFromParcel(Parcel source) {
                return new FeedBean(source);
            }

            @Override
            public FeedBean[] newArray(int size) {
                return new FeedBean[size];
            }
        };
    }

}
