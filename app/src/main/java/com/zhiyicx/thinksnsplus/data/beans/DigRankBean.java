package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class DigRankBean extends BaseListBean {
    /**
     * id : 6
     * user_id : 2
     * value : 3
     */
    @Id
    private Long id;
    private Long user_id;
    @ToOne(joinProperty = "user_id")
    private UserInfoBean digUserInfo;// 点赞人的用户信息
    private String value;

    public UserInfoBean getDigUserInfo() {
        return digUserInfo;
    }

    public void setDigUserInfo(UserInfoBean digUserInfo) {
        this.digUserInfo = digUserInfo;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "DigRankBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", value='" + value  +
                '}';
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
        dest.writeParcelable(this.digUserInfo, flags);
        dest.writeString(this.value);
    }

    public DigRankBean() {
    }

    protected DigRankBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.digUserInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.value = in.readString();
    }

    public static final Creator<DigRankBean> CREATOR = new Creator<DigRankBean>() {
        @Override
        public DigRankBean createFromParcel(Parcel source) {
            return new DigRankBean(source);
        }

        @Override
        public DigRankBean[] newArray(int size) {
            return new DigRankBean[size];
        }
    };
}
