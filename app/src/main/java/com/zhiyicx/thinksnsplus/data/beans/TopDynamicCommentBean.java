package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class TopDynamicCommentBean extends BaseListBean {

    /**
     * id : 2
     * amount : 10
     * day : 1
     * user_id : 1
     * expires_at : 2017-07-05 08:29:49
     * created_at : 2017-06-30 12:04:15
     * comment : {"id":2,"content":"我是第2条评论","pinned":true,"user_id":1,"reply_to_user_id":0,
     * "created_at":"2017-06-27 08:59:14"}
     * feed : {"id":1,"content":"动态内容"}
     */
    @Id
    private Long id;
    private int amount;
    private int day;
    private int user_id;
    private String expires_at;
    private String created_at;
    @Convert(converter = CommentConvert.class, columnType = String.class)
    private CommentBean comment;
    @Convert(converter = FeedConvert.class, columnType = String.class)
    private FeedBean feed;

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public FeedBean getFeed() {
        return feed;
    }

    public void setFeed(FeedBean feed) {
        this.feed = feed;
    }

    public static class CommentBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871009L;
        /**
         * id : 2
         * content : 我是第2条评论
         * pinned : true
         * user_id : 1
         * reply_to_user_id : 0
         * created_at : 2017-06-27 08:59:14
         */

        private int id;
        private String content;
        private boolean pinned;
        private int user_id;
        private int reply_to_user_id;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isPinned() {
            return pinned;
        }

        public void setPinned(boolean pinned) {
            this.pinned = pinned;
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

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.content);
            dest.writeByte(this.pinned ? (byte) 1 : (byte) 0);
            dest.writeInt(this.user_id);
            dest.writeInt(this.reply_to_user_id);
            dest.writeString(this.created_at);
        }

        public CommentBean() {
        }

        protected CommentBean(Parcel in) {
            this.id = in.readInt();
            this.content = in.readString();
            this.pinned = in.readByte() != 0;
            this.user_id = in.readInt();
            this.reply_to_user_id = in.readInt();
            this.created_at = in.readString();
        }

        public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
            @Override
            public CommentBean createFromParcel(Parcel source) {
                return new CommentBean(source);
            }

            @Override
            public CommentBean[] newArray(int size) {
                return new CommentBean[size];
            }
        };
    }

    public static class FeedBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871009L;
        /**
         * id : 1
         * content : 动态内容
         */

        private int id;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.content);
        }

        public FeedBean() {
        }

        protected FeedBean(Parcel in) {
            this.id = in.readInt();
            this.content = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeInt(this.amount);
        dest.writeInt(this.day);
        dest.writeInt(this.user_id);
        dest.writeString(this.expires_at);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.feed, flags);
    }

    public TopDynamicCommentBean() {
    }

    protected TopDynamicCommentBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.amount = in.readInt();
        this.day = in.readInt();
        this.user_id = in.readInt();
        this.expires_at = in.readString();
        this.created_at = in.readString();
        this.comment = in.readParcelable(CommentBean.class.getClassLoader());
        this.feed = in.readParcelable(FeedBean.class.getClassLoader());
    }

    public static final Creator<TopDynamicCommentBean> CREATOR = new Creator<TopDynamicCommentBean>() {
        @Override
        public TopDynamicCommentBean createFromParcel(Parcel source) {
            return new TopDynamicCommentBean(source);
        }

        @Override
        public TopDynamicCommentBean[] newArray(int size) {
            return new TopDynamicCommentBean[size];
        }
    };

    public static class CommentConvert implements PropertyConverter<CommentBean, String> {
        @Override
        public CommentBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(CommentBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class FeedConvert implements PropertyConverter<FeedBean, String> {
        @Override
        public FeedBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(FeedBean entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }
}
