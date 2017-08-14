package com.zhiyicx.thinksnsplus.data.beans.qa;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QAListInfoBean extends BaseListBean {


    /**
     * id : 1
     * user_id : 1
     * subject : 第一个提问?
     * body : null
     * anonymity : 0
     * amount : 0
     * automaticity : 0
     * look : 0
     * excellent : 0
     * status : 0
     * comments_count : 0
     * answers_count : 3
     * watchers_count : 0
     * likes_count : 0
     * views_count : 0
     * created_at : 2017-07-28 08:38:54
     * updated_at : 2017-08-01 06:03:21
     * user : {"id":1,"name":"Seven","bio":"Seven 的个人传记","sex":2,"location":"成都 中国","created_at":"2017-06-02 08:43:54","updated_at":"2017-07-25 03:59:39","avatar":"http://plus.io/api/v2/users/1/avatar","bg":"http://plus.io/storage/user-bg/000/000/000/01.png","verified":null,"extra":{"user_id":1,"likes_count":0,"comments_count":8,"followers_count":0,"followings_count":1,"updated_at":"2017-08-01 06:06:37","feeds_count":0,"questions_count":5,"answers_count":3}}
     */

    private int id;
    private int user_id;
    private String subject;
    private String body;
    private int anonymity;
    private int amount;
    private int automaticity;
    private int look;
    private int excellent;
    private int status;
    private int comments_count;
    private int answers_count;
    private int watchers_count;
    private int likes_count;
    private int views_count;
    private String created_at;
    private String updated_at;

    private UserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAutomaticity() {
        return automaticity;
    }

    public void setAutomaticity(int automaticity) {
        this.automaticity = automaticity;
    }

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getExcellent() {
        return excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAnswers_count() {
        return answers_count;
    }

    public void setAnswers_count(int answers_count) {
        this.answers_count = answers_count;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Parcelable,Serializable{
        private static final long serialVersionUID = -5707219526469433886L;
        /**
         * id : 1
         * name : Seven
         * bio : Seven 的个人传记
         * sex : 2
         * location : 成都 中国
         * created_at : 2017-06-02 08:43:54
         * updated_at : 2017-07-25 03:59:39
         * avatar : http://plus.io/api/v2/users/1/avatar
         * bg : http://plus.io/storage/user-bg/000/000/000/01.png
         * verified : null
         * extra : {"user_id":1,"likes_count":0,"comments_count":8,"followers_count":0,"followings_count":1,"updated_at":"2017-08-01 06:06:37","feeds_count":0,"questions_count":5,"answers_count":3}
         */

        private int id;
        private String name;
        private String bio;
        private int sex;
        private String location;
        private String created_at;
        private String updated_at;
        private String avatar;
        private String bg;
        private VerifiedBean verified;
        private ExtraBean extra;

        protected UserBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            bio = in.readString();
            sex = in.readInt();
            location = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            avatar = in.readString();
            bg = in.readString();
            verified = in.readParcelable(VerifiedBean.class.getClassLoader());
            extra = in.readParcelable(ExtraBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(bio);
            dest.writeInt(sex);
            dest.writeString(location);
            dest.writeString(created_at);
            dest.writeString(updated_at);
            dest.writeString(avatar);
            dest.writeString(bg);
            dest.writeParcelable(verified, flags);
            dest.writeParcelable(extra, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel in) {
                return new UserBean(in);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };

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

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBg() {
            return bg;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public VerifiedBean getVerified() {
            return verified;
        }

        public void setVerified(VerifiedBean verified) {
            this.verified = verified;
        }

        public ExtraBean getExtra() {
            return extra;
        }

        public void setExtra(ExtraBean extra) {
            this.extra = extra;
        }

        public static class ExtraBean implements Parcelable,Serializable{
            private static final long serialVersionUID = -4248334771597200990L;
            /**
             * user_id : 1
             * likes_count : 0
             * comments_count : 8
             * followers_count : 0
             * followings_count : 1
             * updated_at : 2017-08-01 06:06:37
             * feeds_count : 0
             * questions_count : 5
             * answers_count : 3
             */

            private int user_id;
            private int likes_count;
            private int comments_count;
            private int followers_count;
            private int followings_count;
            private String updated_at;
            private int feeds_count;
            private int questions_count;
            private int answers_count;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getLikes_count() {
                return likes_count;
            }

            public void setLikes_count(int likes_count) {
                this.likes_count = likes_count;
            }

            public int getComments_count() {
                return comments_count;
            }

            public void setComments_count(int comments_count) {
                this.comments_count = comments_count;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public int getFollowings_count() {
                return followings_count;
            }

            public void setFollowings_count(int followings_count) {
                this.followings_count = followings_count;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public int getFeeds_count() {
                return feeds_count;
            }

            public void setFeeds_count(int feeds_count) {
                this.feeds_count = feeds_count;
            }

            public int getQuestions_count() {
                return questions_count;
            }

            public void setQuestions_count(int questions_count) {
                this.questions_count = questions_count;
            }

            public int getAnswers_count() {
                return answers_count;
            }

            public void setAnswers_count(int answers_count) {
                this.answers_count = answers_count;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.user_id);
                dest.writeInt(this.likes_count);
                dest.writeInt(this.comments_count);
                dest.writeInt(this.followers_count);
                dest.writeInt(this.followings_count);
                dest.writeString(this.updated_at);
                dest.writeInt(this.feeds_count);
                dest.writeInt(this.questions_count);
                dest.writeInt(this.answers_count);
            }

            public ExtraBean() {
            }

            protected ExtraBean(Parcel in) {
                this.user_id = in.readInt();
                this.likes_count = in.readInt();
                this.comments_count = in.readInt();
                this.followers_count = in.readInt();
                this.followings_count = in.readInt();
                this.updated_at = in.readString();
                this.feeds_count = in.readInt();
                this.questions_count = in.readInt();
                this.answers_count = in.readInt();
            }

            public static final Creator<ExtraBean> CREATOR = new Creator<ExtraBean>() {
                @Override
                public ExtraBean createFromParcel(Parcel source) {
                    return new ExtraBean(source);
                }

                @Override
                public ExtraBean[] newArray(int size) {
                    return new ExtraBean[size];
                }
            };
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeInt(this.user_id);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeInt(this.anonymity);
        dest.writeInt(this.amount);
        dest.writeInt(this.automaticity);
        dest.writeInt(this.look);
        dest.writeInt(this.excellent);
        dest.writeInt(this.status);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.answers_count);
        dest.writeInt(this.watchers_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.views_count);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
    }

    public QAListInfoBean() {
    }

    protected QAListInfoBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.user_id = in.readInt();
        this.subject = in.readString();
        this.body = in.readString();
        this.anonymity = in.readInt();
        this.amount = in.readInt();
        this.automaticity = in.readInt();
        this.look = in.readInt();
        this.excellent = in.readInt();
        this.status = in.readInt();
        this.comments_count = in.readInt();
        this.answers_count = in.readInt();
        this.watchers_count = in.readInt();
        this.likes_count = in.readInt();
        this.views_count = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
    }

    public static final Creator<QAListInfoBean> CREATOR = new Creator<QAListInfoBean>() {
        @Override
        public QAListInfoBean createFromParcel(Parcel source) {
            return new QAListInfoBean(source);
        }

        @Override
        public QAListInfoBean[] newArray(int size) {
            return new QAListInfoBean[size];
        }
    };
}
