package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.RewardsListBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * @author Catherine
 * @describe 回答的详情
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@Entity
public class AnswerInfoBean extends BaseListBean implements Serializable{
    @Transient
    private static final long serialVersionUID = -6138662175756334333L;

    @Id
    private Long id;
    private Long question_id;
    private Long user_id;
    private String body;
    private int anonymity; // 是否匿名，1 代表匿名发布，匿名后不会返回任何用户信息。
    private int adoption; // 是否采纳的答案
    private int invited; // 是否该回答是被邀请的人的回答。
    private int comments_count; // 评论总数统计
    private double rewards_amount; // 回答打赏总额统计
    private int rewarder_count; // 打赏的人总数统计
    private int likes_count; // 回答喜欢总数统计
    private int views_count; // 回答浏览量统计
    private String created_at;
    private String updated_at;
    @Convert(converter = UserInfoBeanConvert.class,columnType = String.class)
    private UserInfoBean user;
    private boolean liked;
    private boolean collected;
    @Transient
    private  List<AnswerCommentListBean> commentList;
    private boolean rewarded;
    @Convert(converter = AnswerDigListBeanConvert.class,columnType = String.class)
    private List<AnswerDigListBean> likes;
    @Convert(converter = RewardsListBeanConvert.class,columnType = String.class)
    private List<RewardsListBean> rewarders;
    @ToOne(joinProperty = "question_id")
    private QAListInfoBean question;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1250738736)
    private transient AnswerInfoBeanDao myDao;

    @Generated(hash = 1083683949)
    public AnswerInfoBean(Long id, Long question_id, Long user_id, String body, int anonymity,
            int adoption, int invited, int comments_count, double rewards_amount, int rewarder_count,
            int likes_count, int views_count, String created_at, String updated_at, UserInfoBean user,
            boolean liked, boolean collected, boolean rewarded, List<AnswerDigListBean> likes,
            List<RewardsListBean> rewarders) {
        this.id = id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.body = body;
        this.anonymity = anonymity;
        this.adoption = adoption;
        this.invited = invited;
        this.comments_count = comments_count;
        this.rewards_amount = rewards_amount;
        this.rewarder_count = rewarder_count;
        this.likes_count = likes_count;
        this.views_count = views_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
        this.liked = liked;
        this.collected = collected;
        this.rewarded = rewarded;
        this.likes = likes;
        this.rewarders = rewarders;
    }

    @Generated(hash = 1616850933)
    public AnswerInfoBean() {
    }

    @Generated(hash = 527827701)
    private transient Long question__resolvedKey;

    public static class AnswerDigListBeanConvert extends BaseConvert<List<AnswerDigListBean>>{}

    @Override
    public Long getMaxId() {
        return id;
    }

    @Keep
    public List<AnswerCommentListBean> getCommentList() {
        return commentList;
    }

    @Keep
    public void setCommentList(List<AnswerCommentListBean> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "AnswerInfoBean{" +
                "id=" + id +
                ", question_id=" + question_id +
                ", user_id=" + user_id +
                ", body='" + body + '\'' +
                ", anonymity=" + anonymity +
                ", adoption=" + adoption +
                ", invited=" + invited +
                ", comments_count=" + comments_count +
                ", rewards_amount=" + rewards_amount +
                ", rewarder_count=" + rewarder_count +
                ", likes_count=" + likes_count +
                ", views_count=" + views_count +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", user=" + user +
                ", liked=" + liked +
                ", collected=" + collected +
                ", rewarded=" + rewarded +
                ", question=" + question +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestion_id() {
        return this.question_id;
    }

    public void setQuestion_id(Long question_id) {
        this.question_id = question_id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAnonymity() {
        return this.anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
    }

    public int getAdoption() {
        return this.adoption;
    }

    public void setAdoption(int adoption) {
        this.adoption = adoption;
    }

    public int getInvited() {
        return this.invited;
    }

    public void setInvited(int invited) {
        this.invited = invited;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public double getRewards_amount() {
        return this.rewards_amount;
    }

    public void setRewards_amount(double rewards_amount) {
        this.rewards_amount = rewards_amount;
    }

    public int getRewarder_count() {
        return this.rewarder_count;
    }

    public void setRewarder_count(int rewarder_count) {
        this.rewarder_count = rewarder_count;
    }

    public int getLikes_count() {
        return this.likes_count;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public int getViews_count() {
        return this.views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean getLiked() {
        return this.liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean getCollected() {
        return this.collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean getRewarded() {
        return this.rewarded;
    }

    public void setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
    }

    public List<AnswerDigListBean> getLikes() {
        return this.likes;
    }

    public void setLikes(List<AnswerDigListBean> likes) {
        this.likes = likes;
    }

    public List<RewardsListBean> getRewarders() {
        return this.rewarders;
    }

    public void setRewarders(List<RewardsListBean> rewarders) {
        this.rewarders = rewarders;
    }

    public UserInfoBean getUser() {
        return this.user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1451724453)
    public QAListInfoBean getQuestion() {
        Long __key = this.question_id;
        if (question__resolvedKey == null || !question__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            QAListInfoBeanDao targetDao = daoSession.getQAListInfoBeanDao();
            QAListInfoBean questionNew = targetDao.load(__key);
            synchronized (this) {
                question = questionNew;
                question__resolvedKey = __key;
            }
        }
        return question;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 333227727)
    public void setQuestion(QAListInfoBean question) {
        synchronized (this) {
            this.question = question;
            question_id = question == null ? null : question.getId();
            question__resolvedKey = question_id;
        }
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.question_id);
        dest.writeValue(this.user_id);
        dest.writeString(this.body);
        dest.writeInt(this.anonymity);
        dest.writeInt(this.adoption);
        dest.writeInt(this.invited);
        dest.writeInt(this.comments_count);
        dest.writeDouble(this.rewards_amount);
        dest.writeInt(this.rewarder_count);
        dest.writeInt(this.likes_count);
        dest.writeInt(this.views_count);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.collected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.commentList);
        dest.writeByte(this.rewarded ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.likes);
        dest.writeTypedList(this.rewarders);
        dest.writeParcelable(this.question, flags);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 705869050)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAnswerInfoBeanDao() : null;
    }

    protected AnswerInfoBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.question_id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.body = in.readString();
        this.anonymity = in.readInt();
        this.adoption = in.readInt();
        this.invited = in.readInt();
        this.comments_count = in.readInt();
        this.rewards_amount = in.readDouble();
        this.rewarder_count = in.readInt();
        this.likes_count = in.readInt();
        this.views_count = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.liked = in.readByte() != 0;
        this.collected = in.readByte() != 0;
        this.commentList = in.createTypedArrayList(AnswerCommentListBean.CREATOR);
        this.rewarded = in.readByte() != 0;
        this.likes = in.createTypedArrayList(AnswerDigListBean.CREATOR);
        this.rewarders = in.createTypedArrayList(RewardsListBean.CREATOR);
        this.question = in.readParcelable(QAListInfoBean.class.getClassLoader());
    }

    public static final Creator<AnswerInfoBean> CREATOR = new Creator<AnswerInfoBean>() {
        @Override
        public AnswerInfoBean createFromParcel(Parcel source) {
            return new AnswerInfoBean(source);
        }

        @Override
        public AnswerInfoBean[] newArray(int size) {
            return new AnswerInfoBean[size];
        }
    };
}
