package com.zhiyicx.thinksnsplus.data.beans.qa;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DaoSession;
import com.zhiyicx.thinksnsplus.data.beans.QAListInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.AnswerInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoListBeanConvert;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class QAListInfoBean extends BaseListBean implements Serializable {
    @Transient
    private static final long serialVersionUID = -4337619110087134442L;
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
    @Id
    private Long id;
    private Long user_id;
    private String subject;
    private String body;
    private int anonymity;
    private double amount;
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
    private boolean watched;
    @Convert(converter = AnswerInfoBeanConvert.class, columnType = String.class)
    private AnswerInfoBean answer;
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "question_id")})
    private List<AnswerInfoBean> invitation_answers; // 问题邀请用户回答的答案列表，具体数据结构参考「回答」文档。
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "question_id")})
    private List<AnswerInfoBean> adoption_answers; // 问题采纳的答案列表，具体数据结构参考「回答」文档。
    @Convert(converter = TopicConvert.class, columnType = String.class)
    private List<QATopicBean> topics; // 问题话题列表，参考「话题」文档。
    @Convert(converter = UserInfoListBeanConvert.class, columnType = String.class)
    private List<UserInfoBean> invitations; // 问题邀请回答的用户列表，参考「用户」文档。
    @Convert(converter = UserInfoBeanConvert.class, columnType = String.class)
    private UserInfoBean user; // 用户资料，如果是 anonymity 是 1 则该字段不存在。
    @ToMany(joinProperties = {@JoinProperty(name = "id", referencedName = "question_id")})
    private List<AnswerInfoBean> answerInfoBeanList;
    @Convert(converter = AnswerInfoBeanConvert.class, columnType = String.class)
    private AnswerInfoBean my_answer; // 自己的回答，如果为空则表示自己还未回答，如果不为空则表示已经回答了

    @Override
    public Long getMaxId() {
        return id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAutomaticity() {
        return this.automaticity;
    }

    public void setAutomaticity(int automaticity) {
        this.automaticity = automaticity;
    }

    public int getLook() {
        return this.look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getExcellent() {
        return this.excellent;
    }

    public void setExcellent(int excellent) {
        this.excellent = excellent;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAnswers_count() {
        return this.answers_count;
    }

    public void setAnswers_count(int answers_count) {
        this.answers_count = answers_count;
    }

    public int getWatchers_count() {
        return this.watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
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


    public boolean getWatched() {
        return this.watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public List<QATopicBean> getTopics() {
        return this.topics;
    }

    public void setTopics(List<QATopicBean> topics) {
        this.topics = topics;
    }

    public List<UserInfoBean> getInvitations() {
        return this.invitations;
    }

    public void setInvitations(List<UserInfoBean> invitations) {
        this.invitations = invitations;
    }

    public UserInfoBean getUser() {
        return this.user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public void setInvitation_answers(List<AnswerInfoBean> invitation_answers) {
        this.invitation_answers = invitation_answers;
    }

    public void setAdoption_answers(List<AnswerInfoBean> adoption_answers) {
        this.adoption_answers = adoption_answers;
    }

    public void setAnswerInfoBeanList(List<AnswerInfoBean> answerInfoBeanList) {
        this.answerInfoBeanList = answerInfoBeanList;
    }

    public boolean isWatched() {
        return watched;
    }

    public AnswerInfoBean getMy_answer() {
        return my_answer;
    }

    public void setMy_answer(AnswerInfoBean my_answer) {
        this.my_answer = my_answer;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 9040246)
    public List<AnswerInfoBean> getInvitation_answers() {
        if (invitation_answers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerInfoBeanDao targetDao = daoSession.getAnswerInfoBeanDao();
            List<AnswerInfoBean> invitation_answersNew = targetDao._queryQAListInfoBean_Invitation_answers(id);
            synchronized (this) {
                if (invitation_answers == null) {
                    invitation_answers = invitation_answersNew;
                }
            }
        }
        return invitation_answers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1217704223)
    public synchronized void resetInvitation_answers() {
        invitation_answers = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1912618879)
    public List<AnswerInfoBean> getAdoption_answers() {
        if (adoption_answers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerInfoBeanDao targetDao = daoSession.getAnswerInfoBeanDao();
            List<AnswerInfoBean> adoption_answersNew = targetDao._queryQAListInfoBean_Adoption_answers(id);
            synchronized (this) {
                if (adoption_answers == null) {
                    adoption_answers = adoption_answersNew;
                }
            }
        }
        return adoption_answers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 2109537705)
    public synchronized void resetAdoption_answers() {
        adoption_answers = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1961481343)
    public List<AnswerInfoBean> getAnswerInfoBeanList() {
        if (answerInfoBeanList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AnswerInfoBeanDao targetDao = daoSession.getAnswerInfoBeanDao();
            List<AnswerInfoBean> answerInfoBeanListNew = targetDao._queryQAListInfoBean_AnswerInfoBeanList(id);
            synchronized (this) {
                if (answerInfoBeanList == null) {
                    answerInfoBeanList = answerInfoBeanListNew;
                }
            }
        }
        return answerInfoBeanList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 562190035)
    public synchronized void resetAnswerInfoBeanList() {
        answerInfoBeanList = null;
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
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 483821523)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getQAListInfoBeanDao() : null;
    }

    public AnswerInfoBean getAnswer() {
        return this.answer;
    }

    public void setAnswer(AnswerInfoBean answer) {
        this.answer = answer;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1951279767)
    private transient QAListInfoBeanDao myDao;

    @Generated(hash = 1991260694)
    public QAListInfoBean(Long id, Long user_id, String subject, String body, int anonymity, double amount, int automaticity, int look, int excellent, int status, int comments_count, int answers_count, int watchers_count, int likes_count, int views_count, String created_at, String updated_at, boolean watched, AnswerInfoBean answer, List<QATopicBean> topics, List<UserInfoBean> invitations, UserInfoBean user, AnswerInfoBean my_answer) {
        this.id = id;
        this.user_id = user_id;
        this.subject = subject;
        this.body = body;
        this.anonymity = anonymity;
        this.amount = amount;
        this.automaticity = automaticity;
        this.look = look;
        this.excellent = excellent;
        this.status = status;
        this.comments_count = comments_count;
        this.answers_count = answers_count;
        this.watchers_count = watchers_count;
        this.likes_count = likes_count;
        this.views_count = views_count;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.watched = watched;
        this.answer = answer;
        this.topics = topics;
        this.invitations = invitations;
        this.user = user;
        this.my_answer = my_answer;
    }

    @Generated(hash = 1163254106)
    public QAListInfoBean() {
    }

    public static class TopicConvert extends BaseConvert<List<QATopicBean>> {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeInt(this.anonymity);
        dest.writeDouble(this.amount);
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
        dest.writeByte(this.watched ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.answer, flags);
        dest.writeTypedList(this.invitation_answers);
        dest.writeTypedList(this.adoption_answers);
        dest.writeTypedList(this.topics);
        dest.writeTypedList(this.invitations);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.answerInfoBeanList);
    }

    protected QAListInfoBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.subject = in.readString();
        this.body = in.readString();
        this.anonymity = in.readInt();
        this.amount = in.readDouble();
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
        this.watched = in.readByte() != 0;
        this.answer = in.readParcelable(AnswerInfoBean.class.getClassLoader());
        this.invitation_answers = in.createTypedArrayList(AnswerInfoBean.CREATOR);
        this.adoption_answers = in.createTypedArrayList(AnswerInfoBean.CREATOR);
        this.topics = in.createTypedArrayList(QATopicBean.CREATOR);
        this.invitations = in.createTypedArrayList(UserInfoBean.CREATOR);
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.answerInfoBeanList = in.createTypedArrayList(AnswerInfoBean.CREATOR);
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
