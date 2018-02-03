package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.QAListInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.RewardsListBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * @author Catherine
 * @describe 回答的详情
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */
@Entity
public class AnswerInfoBean extends BaseListBean implements Serializable {
    private static final long serialVersionUID = -6138662175756334333L;

    @Id
    private Long id;
    private Long question_id;
    private Long user_id;
    private String body;
    private String text_body;
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
    private String onlookers_total;// 围观金额总数
    @Convert(converter = UserInfoBeanConvert.class, columnType = String.class)
    private UserInfoBean user;
    private boolean liked;
    private boolean collected;
    @Transient
    private List<AnswerCommentListBean> commentList;
    private boolean rewarded;
    @Convert(converter = AnswerDigListBeanConvert.class, columnType = String.class)
    private List<AnswerDigListBean> likes;
    @Convert(converter = RewardsListBeanConvert.class, columnType = String.class)
    private List<RewardsListBean> rewarders;
    @Convert(converter = QAListInfoBeanConvert.class, columnType = String.class)
    private QAListInfoBean question;
    private boolean could = true; // 是否开启了围观,true > 可以直接看到
    private int onlookers_count; // 围观人数

    public static class AnswerDigListBeanConvert extends BaseConvert<List<AnswerDigListBean>> {
    }

    public String getText_body() {
        return text_body;
    }

    public void setText_body(String text_body) {
        this.text_body = text_body;
    }

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

    @Keep
    public QAListInfoBean getQuestion() {
        return question;
    }

    @Keep
    public void setQuestion(QAListInfoBean question) {
        this.question = question;
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

    public String getOnlookers_total() {
        return onlookers_total;
    }

    public void setOnlookers_total(String onlookers_total) {
        this.onlookers_total = onlookers_total;
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

    public boolean getCould() {
        return this.could;
    }

    public void setCould(boolean could) {
        this.could = could;
    }

    public int getOnlookers_count() {
        return this.onlookers_count;
    }

    public void setOnlookers_count(int onlookers_count) {
        this.onlookers_count = onlookers_count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnswerInfoBean that = (AnswerInfoBean) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        return question_id != null ? question_id.equals(that.question_id) : that.question_id == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (question_id != null ? question_id.hashCode() : 0);
        return result;
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
        dest.writeString(this.text_body);
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
        dest.writeString(this.onlookers_total);
        dest.writeParcelable(this.user, flags);
        dest.writeByte(this.liked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.collected ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.commentList);
        dest.writeByte(this.rewarded ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.likes);
        dest.writeTypedList(this.rewarders);
        dest.writeParcelable(this.question, flags);
        dest.writeByte(this.could ? (byte) 1 : (byte) 0);
        dest.writeInt(this.onlookers_count);
    }

    public AnswerInfoBean() {
    }

    protected AnswerInfoBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.question_id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.body = in.readString();
        this.text_body = in.readString();
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
        this.onlookers_total = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.liked = in.readByte() != 0;
        this.collected = in.readByte() != 0;
        this.commentList = in.createTypedArrayList(AnswerCommentListBean.CREATOR);
        this.rewarded = in.readByte() != 0;
        this.likes = in.createTypedArrayList(AnswerDigListBean.CREATOR);
        this.rewarders = in.createTypedArrayList(RewardsListBean.CREATOR);
        this.question = in.readParcelable(QAListInfoBean.class.getClassLoader());
        this.could = in.readByte() != 0;
        this.onlookers_count = in.readInt();
    }

    @Generated(hash = 791149338)
    public AnswerInfoBean(Long id, Long question_id, Long user_id, String body, String text_body,
            int anonymity, int adoption, int invited, int comments_count, double rewards_amount,
            int rewarder_count, int likes_count, int views_count, String created_at, String updated_at,
            String onlookers_total, UserInfoBean user, boolean liked, boolean collected,
            boolean rewarded, List<AnswerDigListBean> likes, List<RewardsListBean> rewarders,
            QAListInfoBean question, boolean could, int onlookers_count) {
        this.id = id;
        this.question_id = question_id;
        this.user_id = user_id;
        this.body = body;
        this.text_body = text_body;
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
        this.onlookers_total = onlookers_total;
        this.user = user;
        this.liked = liked;
        this.collected = collected;
        this.rewarded = rewarded;
        this.likes = likes;
        this.rewarders = rewarders;
        this.question = question;
        this.could = could;
        this.onlookers_count = onlookers_count;
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
