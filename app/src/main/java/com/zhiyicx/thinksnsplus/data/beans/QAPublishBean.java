package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/08/10/16:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class QAPublishBean extends BaseDraftBean implements Parcelable {

    private String subject;// 问题主题或者说标题
    @Convert(converter = TopicConvert.class, columnType = String.class)
    private List<Topic> topics;// 绑定的话题
    @Convert(converter = InvitationsConvert.class, columnType = String.class)
    private List<Invitations> invitations;// 问题邀请回答的人
    private String body;// 问题描述
    private String text_body;// 非 markDown 问题描述
    private int anonymity;// 是否匿名 1 匿名 ，0 不匿名
    private int automaticity;// 邀请悬赏自动入账，只邀请一个人的情况下，允许悬赏金额自动入账到被邀请回答者钱包中。1 自动入账 ，0 不自动入账
    private int look;// 是否开启围观，当问题有采纳或者邀请人已回答，则对外部观众自动开启围观。设置围观必须设置悬赏金额。1 开启围观 ，0 不开启围观
    private double amount;// 问题价值，悬赏金额
    private Long id;
    @Id
    @Unique
    private Long mark;
    private Long user_id;
    private String updated_at;
    private String created_at;

    // 不建议 用 is 开头的不二类型，序列化时找不到 get set 等方法导致失败
    private boolean hasAgainEdite;// 已经发布的问题再次编辑
    private boolean hasAdoption;// 是否有已经采纳的回答
    public boolean isHasAdoption() {
        return hasAdoption;
    }

    public void setHasAdoption(boolean hasAdoption) {
        this.hasAdoption = hasAdoption;
    }

    public boolean isHasAgainEdite() {
        return hasAgainEdite;
    }

    public void setHasAgainEdite(boolean hasAgainEdite) {
        this.hasAgainEdite = hasAgainEdite;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        if (subject.isEmpty()) {
            return;
        }
        if (subject.endsWith("?") || subject.endsWith("？")) {
            this.subject = subject;
            return;
        }
        this.subject = subject + "?";
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Invitations> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitations> invitations) {
        this.invitations = invitations;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getText_body() {
        return text_body;
    }

    public void setText_body(String text_body) {
        this.text_body = text_body;
    }

    public int getAnonymity() {
        return anonymity;
    }

    public void setAnonymity(int anonymity) {
        this.anonymity = anonymity;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static class Topic implements Parcelable, Serializable {
        private static final long serialVersionUID = -7016435261647250643L;
        private int id;
        @Expose
        private String name;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public Topic() {
        }

        protected Topic(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<Topic> CREATOR = new Creator<Topic>() {
            @Override
            public Topic createFromParcel(Parcel source) {
                return new Topic(source);
            }

            @Override
            public Topic[] newArray(int size) {
                return new Topic[size];
            }
        };
    }

    public static class Invitations implements Parcelable, Serializable {
        private static final long serialVersionUID = -4339393354491900423L;
        private int user;
        @Expose
        private String name;

        public int getUser() {
            return user;
        }

        public void setUser(int user) {
            this.user = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.user);
            dest.writeString(this.name);
        }

        public Invitations() {
        }

        protected Invitations(Parcel in) {
            this.user = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<Invitations> CREATOR = new Creator<Invitations>() {
            @Override
            public Invitations createFromParcel(Parcel source) {
                return new Invitations(source);
            }

            @Override
            public Invitations[] newArray(int size) {
                return new Invitations[size];
            }
        };
    }


    public static class TopicConvert extends BaseConvert<List<Topic>> {
    }

    public static class InvitationsConvert extends BaseConvert<List<Invitations>> {
    }

    public static QAPublishBean qaListInfo2QAPublishBean(QAListInfoBean mQaListInfoBean) {
        QAPublishBean qaPublishBean = new QAPublishBean();
        String mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System
                .currentTimeMillis();
        qaPublishBean.setHasAgainEdite(true);
        qaPublishBean.setHasAdoption(mQaListInfoBean.getAdoption_answers() != null
                && !mQaListInfoBean.getAdoption_answers().isEmpty());
        qaPublishBean.setId(mQaListInfoBean.getId());
        qaPublishBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        qaPublishBean.setMark(Long.parseLong(mark));
        qaPublishBean.setSubject(mQaListInfoBean.getSubject());
        qaPublishBean.setBody(mQaListInfoBean.getBody());
        List<QAPublishBean.Topic> typeIdsList = new ArrayList<>();
        for (QATopicBean qaTopicBean : mQaListInfoBean.getTopics()) {
            QAPublishBean.Topic typeIds = new QAPublishBean.Topic();
            typeIds.setId(qaTopicBean.getId().intValue());
            typeIds.setName(qaTopicBean.getName());
            typeIdsList.add(typeIds);
        }
        qaPublishBean.setTopics(typeIdsList);

        if (mQaListInfoBean.getInvitations() != null && !mQaListInfoBean.getInvitations().isEmpty()) {
            List<QAPublishBean.Invitations> invitations = new ArrayList<>();
            for (UserInfoBean userInfoBean : mQaListInfoBean.getInvitations()) {
                QAPublishBean.Invitations invitation = new QAPublishBean.Invitations();
                invitation.setName(userInfoBean.getName());
                invitation.setUser(userInfoBean.getUser_id().intValue());
                invitations.add(invitation);
            }
            qaPublishBean.setInvitations(invitations);
        }
        qaPublishBean.setAutomaticity(mQaListInfoBean.getAutomaticity());
        qaPublishBean.setAmount(mQaListInfoBean.getAmount());
        qaPublishBean.setAnonymity(mQaListInfoBean.getAnonymity());
        qaPublishBean.setLook(mQaListInfoBean.getLook());
        qaPublishBean.setUser_id(mQaListInfoBean.getUser_id());
        return qaPublishBean;
    }

    public boolean getHasAgainEdite() {
        return this.hasAgainEdite;
    }

    public boolean getHasAdoption() {
        return this.hasAdoption;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.subject);
        dest.writeTypedList(this.topics);
        dest.writeTypedList(this.invitations);
        dest.writeString(this.body);
        dest.writeString(this.text_body);
        dest.writeInt(this.anonymity);
        dest.writeInt(this.automaticity);
        dest.writeInt(this.look);
        dest.writeDouble(this.amount);
        dest.writeValue(this.id);
        dest.writeValue(this.mark);
        dest.writeValue(this.user_id);
        dest.writeString(this.updated_at);
        dest.writeString(this.created_at);
        dest.writeByte(this.hasAgainEdite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasAdoption ? (byte) 1 : (byte) 0);
    }

    public QAPublishBean() {
    }

    protected QAPublishBean(Parcel in) {
        super(in);
        this.subject = in.readString();
        this.topics = in.createTypedArrayList(Topic.CREATOR);
        this.invitations = in.createTypedArrayList(Invitations.CREATOR);
        this.body = in.readString();
        this.text_body = in.readString();
        this.anonymity = in.readInt();
        this.automaticity = in.readInt();
        this.look = in.readInt();
        this.amount = in.readDouble();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mark = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.updated_at = in.readString();
        this.created_at = in.readString();
        this.hasAgainEdite = in.readByte() != 0;
        this.hasAdoption = in.readByte() != 0;
    }

    @Generated(hash = 1079887802)
    public QAPublishBean(String subject, List<Topic> topics, List<Invitations> invitations, String body,
            String text_body, int anonymity, int automaticity, int look, double amount, Long id,
            Long mark, Long user_id, String updated_at, String created_at, boolean hasAgainEdite,
            boolean hasAdoption) {
        this.subject = subject;
        this.topics = topics;
        this.invitations = invitations;
        this.body = body;
        this.text_body = text_body;
        this.anonymity = anonymity;
        this.automaticity = automaticity;
        this.look = look;
        this.amount = amount;
        this.id = id;
        this.mark = mark;
        this.user_id = user_id;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.hasAgainEdite = hasAgainEdite;
        this.hasAdoption = hasAdoption;
    }

    public static final Creator<QAPublishBean> CREATOR = new Creator<QAPublishBean>() {
        @Override
        public QAPublishBean createFromParcel(Parcel source) {
            return new QAPublishBean(source);
        }

        @Override
        public QAPublishBean[] newArray(int size) {
            return new QAPublishBean[size];
        }
    };
}
