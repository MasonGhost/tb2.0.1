package com.zhiyicx.thinksnsplus.data.beans.qa;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoListBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe this bean just for topic in qa
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class QATopicBean extends BaseListBean implements Serializable{
    private static final long serialVersionUID = -2380980242688116330L;

    @Id
    private Long id;
    private String name;
    private String description;
    private int questions_count;
    private int experts_count;
    @Convert(converter = UserInfoListBeanConvert.class, columnType = String.class)
    private List<UserInfoBean> experts;
    private int follows_count;
    private boolean has_follow;
    private String avatar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestions_count() {
        return this.questions_count;
    }

    public void setQuestions_count(int questions_count) {
        this.questions_count = questions_count;
    }

    public int getExperts_count() {
        return this.experts_count;
    }

    public void setExperts_count(int experts_count) {
        this.experts_count = experts_count;
    }

    public int getFollows_count() {
        return this.follows_count;
    }

    public void setFollows_count(int follows_count) {
        this.follows_count = follows_count;
    }

    public boolean getHas_follow() {
        return this.has_follow;
    }

    public void setHas_follow(boolean has_follow) {
        this.has_follow = has_follow;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<UserInfoBean> getExperts() {
        return this.experts;
    }

    public void setExperts(List<UserInfoBean> experts) {
        this.experts = experts;
    }

    @Override
    public Long getMaxId() {
        return id;
    }

    public QATopicBean() {
    }

    public QATopicBean(Long id,String name) {
        this.id=id;
        this.name=name;
    }
    

    public static class UserInfoCovert extends BaseConvert<List<UserInfoBean>>{}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.questions_count);
        dest.writeInt(this.experts_count);
        dest.writeTypedList(this.experts);
        dest.writeInt(this.follows_count);
        dest.writeByte(this.has_follow ? (byte) 1 : (byte) 0);
        dest.writeString(this.avatar);
    }

    protected QATopicBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.questions_count = in.readInt();
        this.experts_count = in.readInt();
        this.experts = in.createTypedArrayList(UserInfoBean.CREATOR);
        this.follows_count = in.readInt();
        this.has_follow = in.readByte() != 0;
        this.avatar = in.readString();
    }

    @Generated(hash = 1732529303)
    public QATopicBean(Long id, String name, String description,
            int questions_count, int experts_count, List<UserInfoBean> experts,
            int follows_count, boolean has_follow, String avatar) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questions_count = questions_count;
        this.experts_count = experts_count;
        this.experts = experts;
        this.follows_count = follows_count;
        this.has_follow = has_follow;
        this.avatar = avatar;
    }

    public static final Creator<QATopicBean> CREATOR = new Creator<QATopicBean>() {
        @Override
        public QATopicBean createFromParcel(Parcel source) {
            return new QATopicBean(source);
        }

        @Override
        public QATopicBean[] newArray(int size) {
            return new QATopicBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QATopicBean that = (QATopicBean) o;

        if (!id.equals(that.id)) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
