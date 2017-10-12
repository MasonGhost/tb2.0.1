package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.io.Serializable;

/**
 * @Describe 打赏列表数据
 * @Author Jungle68
 * @Date 2017/8/1
 * @Contact master.jungle68@gmail.com
 */
public class RewardsListBean extends BaseListBean implements Serializable{


    private static final long serialVersionUID = 1028072730844897321L;
    /**
     * id : 1
     * user_id : 1
     * target_user : 1
     * amount : 200
     * rewardable_id : 1
     * rewardable_type : news
     * created_at : 2017-08-01 02:13:28
     * updated_at : 2017-08-01 02:13:28
     */

    private Long id;
    private Long user_id;
    private Long target_user;
    private Long amount;
    private int rewardable_id;
    private String rewardable_type;
    private String created_at;
    private String updated_at;
    private UserInfoBean user;

    @Override
    public Long getMaxId() {
        return id;
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

    public Long getTarget_user() {
        return target_user;
    }

    public void setTarget_user(Long target_user) {
        this.target_user = target_user;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public int getRewardable_id() {
        return rewardable_id;
    }

    public void setRewardable_id(int rewardable_id) {
        this.rewardable_id = rewardable_id;
    }

    public String getRewardable_type() {
        return rewardable_type;
    }

    public void setRewardable_type(String rewardable_type) {
        this.rewardable_type = rewardable_type;
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

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }




    @Override
    public String toString() {
        return "RewardsListBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", target_user=" + target_user +
                ", amount=" + amount +
                ", rewardable_id=" + rewardable_id +
                ", rewardable_type='" + rewardable_type + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", user=" + user +
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
        dest.writeValue(this.target_user);
        dest.writeValue(this.amount);
        dest.writeInt(this.rewardable_id);
        dest.writeString(this.rewardable_type);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.user, flags);
    }

    public RewardsListBean() {
    }

    protected RewardsListBean(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.target_user = (Long) in.readValue(Long.class.getClassLoader());
        this.amount = (Long) in.readValue(Long.class.getClassLoader());
        this.rewardable_id = in.readInt();
        this.rewardable_type = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<RewardsListBean> CREATOR = new Creator<RewardsListBean>() {
        @Override
        public RewardsListBean createFromParcel(Parcel source) {
            return new RewardsListBean(source);
        }

        @Override
        public RewardsListBean[] newArray(int size) {
            return new RewardsListBean[size];
        }
    };
}
