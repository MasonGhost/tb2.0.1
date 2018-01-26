package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Jliuer
 * @Date 2017/11/29/15:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class CirclePostListBean extends BaseListBean implements Serializable, Cloneable {

    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;
    private static final long serialVersionUID = 9154485538884327047L;

    /**
     * id : 88
     * group_id : 1
     * user_id : 1
     * title : 内容标题
     * summary : 帖子介绍
     * likes_count : 0
     * comments_count : 0
     * views_count : 0
     * liked : true
     * collected : true
     * created_at : 2017-11-28 07:12:20
     * updated_at : 2017-11-28 07:12:20
     * images : [{"id":113,"size":"397x246"},{"id":115,"size":"397x246"}]
     * user : {"id":1,"name":"admin","bio":null,"sex":2,"location":"四川省 巴中市 南江县","created_at":"2017-10-23 01:17:34","updated_at":"2017-11-15 07:36:17","avatar":"http://thinksns-plus.dev/api/v2/users/1/avatar","bg":null,"verified":{"type":"user","icon":"http://thinksns-plus.dev/storage/certifications/000/000/0us/er.png","description":"1111"},"extra":{"user_id":1,"likes_count":5,"comments_count":3,"followers_count":0,"followings_count":6,"updated_at":"2017-11-27 07:25:04","feeds_count":8,"questions_count":2,"answers_count":0,"checkin_count":7,"last_checkin_count":1}}
     */
    @Id
    private Long id;
    @Unique
    private Long post_mark;
    private long group_id;
    private Long user_id;
    private String title;
    private String summary;
    private int likes_count;
    private int comments_count;
    private int views_count;
    private boolean liked;
    private boolean collected;
    private String created_at;
    private String updated_at;
    @Convert(converter = UserInfoBeanConvert.class, columnType = String.class)
    private UserInfoBean user;
    @Convert(converter = CirclePostImageConvert.class, columnType = String.class)
    private List<ImagesBean> images;
    @Convert(converter = CirclePostCommentConvert.class, columnType = String.class)
    private List<CirclePostCommentBean> comments;
    private int state = SEND_ING;

    private int reward_amount;
    private int reward_number;
    private String body;
    @Convert(converter = CircleInfoConvert.class, columnType = String.class)
    private CircleInfo group;
    private boolean pinned;
    @Convert(converter = PostDigListConvert.class, columnType = String.class)
    private List<PostDigListBean> digs;

    @Override
    public Long getMaxId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getReward_amount() {
        return reward_amount;
    }

    public void setReward_amount(int reward_amount) {
        this.reward_amount = reward_amount;
    }

    public int getReward_number() {
        return reward_number;
    }

    public void setReward_number(int reward_number) {
        this.reward_number = reward_number;
    }

    public List<PostDigListBean> getDigList() {
        return digs;
    }

    public void setDigList(List<PostDigListBean> digs) {
        this.digs = digs;
    }

    public boolean hasPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public CircleInfo getGroup() {
        return group;
    }

    public void setGroup(CircleInfo group) {
        this.group = group;
    }

    public Long getPost_mark() {
        return post_mark;
    }

    public void setPost_mark(Long post_mark) {
        this.post_mark = post_mark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<CirclePostCommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CirclePostCommentBean> comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public boolean hasLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean hasCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
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

    @Keep
    public UserInfoBean getUserInfoBean() {
        return user;
    }

    @Keep
    public void setUserInfoBean(UserInfoBean user) {
        this.user = user;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class ImagesBean implements Serializable, Parcelable {
        private static final long serialVersionUID = -2450120806619198355L;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeLong(this.group_id);
        dest.writeValue(this.user_id);
        dest.writeString(this.title);
        dest.writeString(this.summary);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.state);
        dest.writeInt(this.comments_count);
        dest.writeInt(this.views_count);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.collected ? (byte) 1 : (byte) 0);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.comments);
    }

    public boolean getLiked() {
        return this.liked;
    }

    public boolean getCollected() {
        return this.collected;
    }

    public UserInfoBean getUser() {
        return this.user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public boolean getPinned() {
        return this.pinned;
    }

    public List<PostDigListBean> getDigs() {
        return this.digs;
    }

    public void setDigs(List<PostDigListBean> digs) {
        this.digs = digs;
    }

    public CirclePostListBean() {
    }

    protected CirclePostListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.group_id = in.readLong();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.title = in.readString();
        this.summary = in.readString();
        this.likes_count = in.readInt();
        this.comments_count = in.readInt();
        this.views_count = in.readInt();
        this.state = in.readInt();
        this.liked = in.readByte() != 0;
        this.collected = in.readByte() != 0;
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.images = in.createTypedArrayList(ImagesBean.CREATOR);
        this.comments = in.createTypedArrayList(CirclePostCommentBean.CREATOR);
    }

    @Generated(hash = 1415342187)
    public CirclePostListBean(Long id, Long post_mark, long group_id, Long user_id, String title, String summary, int likes_count, int comments_count, int views_count, boolean liked, boolean collected, String created_at, String updated_at, UserInfoBean user, List<ImagesBean> images, List<CirclePostCommentBean> comments, int state, int reward_amount, int reward_number, String body, CircleInfo group, boolean pinned, List<PostDigListBean> digs) {
        this.id = id;
        this.post_mark = post_mark;
        this.group_id = group_id;
        this.user_id = user_id;
        this.title = title;
        this.summary = summary;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.views_count = views_count;
        this.liked = liked;
        this.collected = collected;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
        this.images = images;
        this.comments = comments;
        this.state = state;
        this.reward_amount = reward_amount;
        this.reward_number = reward_number;
        this.body = body;
        this.group = group;
        this.pinned = pinned;
        this.digs = digs;
    }

    public static final Creator<CirclePostListBean> CREATOR = new Creator<CirclePostListBean>() {
        @Override
        public CirclePostListBean createFromParcel(Parcel source) {
            return new CirclePostListBean(source);
        }

        @Override
        public CirclePostListBean[] newArray(int size) {
            return new CirclePostListBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CirclePostListBean that = (CirclePostListBean) o;

        if (group_id != that.group_id) {
            return false;
        }
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (int) (group_id ^ (group_id >>> 32));
        return result;
    }

    public static class CirclePostImageConvert extends BaseConvert<List<ImagesBean>> {
    }

    public static class CirclePostCommentConvert extends BaseConvert<List<CirclePostCommentBean>> {
    }

    public static class PostDigListConvert extends BaseConvert<List<PostDigListBean>> {
    }

    public static class CircleInfoConvert extends BaseConvert<CircleInfo> {
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
