package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Catherine
 * @describe 专家bean
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertBean extends BaseListBean {


    /**
     * id : 1
     * name : Seven
     * bio : Seven 的个人传记
     * sex : 2
     * location : 成都 中国
     * created_at : 2017-06-02 08:43:54
     * updated_at : 2017-07-25 03:59:39
     * following : false
     * follower : false
     * avatar : http://plus.io/api/v2/users/1/avatar
     * bg : http://plus.io/storage/user-bg/000/000/000/01.png
     * verified : null
     * extra : {"user_id":1,"likes_count":0,"comments_count":8,"followers_count":0,"followings_count":1,"updated_at":"2017-08-01 06:06:37","feeds_count":0,"questions_count":5,"answers_count":3}
     * tags : [{"id":1,"name":"标签1","tag_category_id":1}]
     */
    @SerializedName(value = "id", alternate = {"user_id"})
    private int id;
    private String name;
    private String bio;
    private int sex;
    private String location;
    private String created_at;
    private String updated_at;
    private boolean following;
    private boolean follower;
    private String avatar;
    private String bg;
    private VerifiedBean verified;
    private ExtraBean extra;
    private List<UserTagBean> tags;

    @Override
    public Long getMaxId() {
        return (long) id;
    }

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

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isFollower() {
        return follower;
    }

    public void setFollower(boolean follower) {
        this.follower = follower;
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

    public List<UserTagBean> getTags() {
        return tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
    }

    public static class ExtraBean implements Parcelable, Serializable {
        private static final long serialVersionUID = -7614931495429263456L;
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

        protected ExtraBean(Parcel in) {
            user_id = in.readInt();
            likes_count = in.readInt();
            comments_count = in.readInt();
            followers_count = in.readInt();
            followings_count = in.readInt();
            updated_at = in.readString();
            feeds_count = in.readInt();
            questions_count = in.readInt();
            answers_count = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(user_id);
            dest.writeInt(likes_count);
            dest.writeInt(comments_count);
            dest.writeInt(followers_count);
            dest.writeInt(followings_count);
            dest.writeString(updated_at);
            dest.writeInt(feeds_count);
            dest.writeInt(questions_count);
            dest.writeInt(answers_count);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ExtraBean> CREATOR = new Creator<ExtraBean>() {
            @Override
            public ExtraBean createFromParcel(Parcel in) {
                return new ExtraBean(in);
            }

            @Override
            public ExtraBean[] newArray(int size) {
                return new ExtraBean[size];
            }
        };

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
    }

    public static class TagsBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 4814188227142987987L;
        /**
         * id : 1
         * name : 标签1
         * tag_category_id : 1
         */

        private int id;
        private String name;
        private int tag_category_id;

        protected TagsBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            tag_category_id = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeInt(tag_category_id);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TagsBean> CREATOR = new Creator<TagsBean>() {
            @Override
            public TagsBean createFromParcel(Parcel in) {
                return new TagsBean(in);
            }

            @Override
            public TagsBean[] newArray(int size) {
                return new TagsBean[size];
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

        public int getTag_category_id() {
            return tag_category_id;
        }

        public void setTag_category_id(int tag_category_id) {
            this.tag_category_id = tag_category_id;
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
        dest.writeString(this.name);
        dest.writeString(this.bio);
        dest.writeInt(this.sex);
        dest.writeString(this.location);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.following ? (byte) 1 : (byte) 0);
        dest.writeByte(this.follower ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar);
        dest.writeString(this.bg);
        dest.writeParcelable(this.verified, flags);
        dest.writeParcelable(this.extra, flags);
        dest.writeTypedList(this.tags);
    }

    public ExpertBean() {
    }

    protected ExpertBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.name = in.readString();
        this.bio = in.readString();
        this.sex = in.readInt();
        this.location = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.following = in.readByte() != 0;
        this.follower = in.readByte() != 0;
        this.avatar = in.readString();
        this.bg = in.readString();
        this.verified = in.readParcelable(VerifiedBean.class.getClassLoader());
        this.extra = in.readParcelable(ExtraBean.class.getClassLoader());
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
    }

    public static final Creator<ExpertBean> CREATOR = new Creator<ExpertBean>() {
        @Override
        public ExpertBean createFromParcel(Parcel source) {
            return new ExpertBean(source);
        }

        @Override
        public ExpertBean[] newArray(int size) {
            return new ExpertBean[size];
        }
    };
}
