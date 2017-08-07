package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * @Describe 钱包的数据类
 * @Author Jungle68
 * @Date 2017/5/24
  @Contact master.jungle68@gmail.com
 */
@Entity
public class WalletBean implements Parcelable ,Serializable{
    private static final long serialVersionUID=123L;

    /**
     * id : 1
     * user_id : 1
     * balance : 0
     * created_at : 2017-05-22 00:00:00
     * updated_at : 2017-05-22 00:00:00
     * deleted_at : null
     */
    @Id
    private Long id;
    @Unique
    private int user_id;
    private double balance;
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeInt(this.user_id);
        dest.writeDouble(this.balance);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
    }

    public WalletBean() {
    }

    protected WalletBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.user_id = in.readInt();
        this.balance = in.readDouble();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
    }

    @Generated(hash = 1325167548)
    public WalletBean(Long id, int user_id, double balance, String created_at,
            String updated_at, String deleted_at) {
        this.id = id;
        this.user_id = user_id;
        this.balance = balance;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public static final Creator<WalletBean> CREATOR = new Creator<WalletBean>() {
        @Override
        public WalletBean createFromParcel(Parcel source) {
            return new WalletBean(source);
        }

        @Override
        public WalletBean[] newArray(int size) {
            return new WalletBean[size];
        }
    };

    @Override
    public String toString() {
        return "WalletBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", balance=" + balance +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }
}
