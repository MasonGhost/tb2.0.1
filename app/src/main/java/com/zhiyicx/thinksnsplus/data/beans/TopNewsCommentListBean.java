package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/09/11/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopNewsCommentListBean extends BaseListBean implements Parcelable, Serializable {

    public static final int TOP_REVIEW = 0;
    public static final int TOP_SUCCESS = 1;
    public static final int TOP_REFUSE = 2;

    private static final long serialVersionUID = -4707561156899223475L;
    /**
     * id : 12
     * created_at : 2017-07-27 08:43:33
     * updated_at : 2017-07-27 08:44:20
     * channel : news:comment
     * state : 1
     * raw : 1
     * target : 1
     * user_id : 2
     * target_user : 2
     * amount : 50
     * day : 5
     * cate_id : null
     * expires_at : 2017-08-01 08:44:20
     * news : {"id":1,"created_at":"2017-07-25 00:00:00","updated_at":"2017-07-27 08:58:02","title":"资讯标题","content":"阿斯顿发生地方爱上地方爱上地方阿斯顿","digg_count":0,"comment_count":1,"hits":1,"from":"1","is_recommend":1,"subject":"潇洒地方","author":"哈哈哈","audit_status":0,"audit_count":0,"user_id":2,"category":{"id":1,"name":"分类1","rank":0},"image":{"id":1,"size":"1920x1080"},"pinned":{"id":13,"created_at":"2017-07-27 09:10:04","updated_at":"2017-07-27 09:10:04","channel":"news","state":1,"raw":0,"target":1,"user_id":2,"target_user":0,"amount":50,"day":5,"cate_id":null,"expires_at":"2017-07-25 00:00:00"}}
     * comment : {"id":1,"user_id":2,"target_user":2,"reply_user":0,"body":"sldkfjalksdjflakjsdflkajsd","commentable_id":1,"commentable_type":"news","created_at":"2017-07-25 00:00:00","updated_at":"2017-07-25 00:00:00"}
     */

    private Long id;
    private String created_at;
    private String updated_at;
    private String channel;
    private int state;
    private int raw;
    private int target;
    private long user_id;
    private long target_user;
    private int amount;
    private int day;
    private int cate_id;
    private String expires_at;
    private NewsBean news;
    private CommentedBean comment;
    private UserInfoBean commentUser;
    private UserInfoBean replyUser;

    @Override
    public Long getMaxId() {
        return id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getRaw() {
        return raw;
    }

    public void setRaw(int raw) {
        this.raw = raw;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getTarget_user() {
        return target_user;
    }

    public void setTarget_user(long target_user) {
        this.target_user = target_user;
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

    public int getCate_id() {
        return cate_id;
    }

    public void setCate_id(int cate_id) {
        this.cate_id = cate_id;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public CommentedBean getComment() {
        return comment;
    }

    public void setComment(CommentedBean comment) {
        this.comment = comment;
    }

    public UserInfoBean getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(UserInfoBean commentUser) {
        this.commentUser = commentUser;
    }

    public UserInfoBean getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserInfoBean replyUser) {
        this.replyUser = replyUser;
    }

    public static class NewsBean implements Parcelable, Serializable {

        private static final long serialVersionUID = -7029874385901552294L;
        /**
         * id : 1
         * created_at : 2017-07-25 00:00:00
         * updated_at : 2017-07-27 08:58:02
         * title : 资讯标题
         * content : 阿斯顿发生地方爱上地方爱上地方阿斯顿
         * digg_count : 0
         * comment_count : 1
         * hits : 1
         * from : 1
         * is_recommend : 1
         * subject : 潇洒地方
         * author : 哈哈哈
         * audit_status : 0
         * audit_count : 0
         * user_id : 2
         * category : {"id":1,"name":"分类1","rank":0}
         * image : {"id":1,"size":"1920x1080"}
         * pinned : {"id":13,"created_at":"2017-07-27 09:10:04","updated_at":"2017-07-27 09:10:04","channel":"news","state":1,"raw":0,"target":1,"user_id":2,"target_user":0,"amount":50,"day":5,"cate_id":null,"expires_at":"2017-07-25 00:00:00"}
         */

        private int id;
        private String created_at;
        private String updated_at;
        private String title;
        private String content;
        private int digg_count;
        private int comment_count;
        private int hits;
        private String from;
        private int is_recommend;
        private String subject;
        private String author;
        private int audit_status;
        private int audit_count;
        private int user_id;
        private CategoryBean category;
        private ImageBean image;
        private PinnedBean pinned;

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

        public String getTitle() {
            return title;
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

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public int getIs_recommend() {
            return is_recommend;
        }

        public void setIs_recommend(int is_recommend) {
            this.is_recommend = is_recommend;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getAudit_status() {
            return audit_status;
        }

        public void setAudit_status(int audit_status) {
            this.audit_status = audit_status;
        }

        public int getAudit_count() {
            return audit_count;
        }

        public void setAudit_count(int audit_count) {
            this.audit_count = audit_count;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public CategoryBean getCategory() {
            return category;
        }

        public void setCategory(CategoryBean category) {
            this.category = category;
        }

        public ImageBean getImage() {
            return image;
        }

        public void setImage(ImageBean image) {
            this.image = image;
        }

        public PinnedBean getPinned() {
            return pinned;
        }

        public void setPinned(PinnedBean pinned) {
            this.pinned = pinned;
        }

        public static class CategoryBean implements Parcelable, Serializable {
            private static final long serialVersionUID = 4191297793991940572L;
            /**
             * id : 1
             * name : 分类1
             * rank : 0
             */

            private int id;
            private String name;
            private int rank;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
                dest.writeInt(this.rank);
            }

            public CategoryBean() {
            }

            protected CategoryBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
                this.rank = in.readInt();
            }

            public static final Creator<CategoryBean> CREATOR = new Creator<CategoryBean>() {
                @Override
                public CategoryBean createFromParcel(Parcel source) {
                    return new CategoryBean(source);
                }

                @Override
                public CategoryBean[] newArray(int size) {
                    return new CategoryBean[size];
                }
            };
        }

        public static class ImageBean implements Parcelable, Serializable {
            private static final long serialVersionUID = 3573717855476298423L;
            /**
             * id : 1
             * size : 1920x1080
             */

            private int id;
            private String size;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.size);
            }

            public ImageBean() {
            }

            protected ImageBean(Parcel in) {
                this.id = in.readInt();
                this.size = in.readString();
            }

            public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
                @Override
                public ImageBean createFromParcel(Parcel source) {
                    return new ImageBean(source);
                }

                @Override
                public ImageBean[] newArray(int size) {
                    return new ImageBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeString(this.title);
            dest.writeString(this.content);
            dest.writeInt(this.digg_count);
            dest.writeInt(this.comment_count);
            dest.writeInt(this.hits);
            dest.writeString(this.from);
            dest.writeInt(this.is_recommend);
            dest.writeString(this.subject);
            dest.writeString(this.author);
            dest.writeInt(this.audit_status);
            dest.writeInt(this.audit_count);
            dest.writeInt(this.user_id);
            dest.writeParcelable(this.category, flags);
            dest.writeParcelable(this.image, flags);
            dest.writeParcelable(this.pinned, flags);
        }

        public NewsBean() {
        }

        protected NewsBean(Parcel in) {
            this.id = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.title = in.readString();
            this.content = in.readString();
            this.digg_count = in.readInt();
            this.comment_count = in.readInt();
            this.hits = in.readInt();
            this.from = in.readString();
            this.is_recommend = in.readInt();
            this.subject = in.readString();
            this.author = in.readString();
            this.audit_status = in.readInt();
            this.audit_count = in.readInt();
            this.user_id = in.readInt();
            this.category = in.readParcelable(CategoryBean.class.getClassLoader());
            this.image = in.readParcelable(ImageBean.class.getClassLoader());
            this.pinned = in.readParcelable(PinnedBean.class.getClassLoader());
        }

        public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
            @Override
            public NewsBean createFromParcel(Parcel source) {
                return new NewsBean(source);
            }

            @Override
            public NewsBean[] newArray(int size) {
                return new NewsBean[size];
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
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.channel);
        dest.writeInt(this.state);
        dest.writeInt(this.raw);
        dest.writeInt(this.target);
        dest.writeLong(this.user_id);
        dest.writeLong(this.target_user);
        dest.writeInt(this.amount);
        dest.writeInt(this.day);
        dest.writeInt(this.cate_id);
        dest.writeString(this.expires_at);
        dest.writeParcelable(this.news, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.commentUser, flags);
        dest.writeParcelable(this.replyUser, flags);
    }

    public TopNewsCommentListBean() {
    }

    protected TopNewsCommentListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.channel = in.readString();
        this.state = in.readInt();
        this.raw = in.readInt();
        this.target = in.readInt();
        this.user_id = in.readLong();
        this.target_user = in.readLong();
        this.amount = in.readInt();
        this.day = in.readInt();
        this.cate_id = in.readInt();
        this.expires_at = in.readString();
        this.news = in.readParcelable(NewsBean.class.getClassLoader());
        this.comment = in.readParcelable(CommentedBean.class.getClassLoader());
        this.commentUser = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.replyUser = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<TopNewsCommentListBean> CREATOR = new Creator<TopNewsCommentListBean>() {
        @Override
        public TopNewsCommentListBean createFromParcel(Parcel source) {
            return new TopNewsCommentListBean(source);
        }

        @Override
        public TopNewsCommentListBean[] newArray(int size) {
            return new TopNewsCommentListBean[size];
        }
    };
}
