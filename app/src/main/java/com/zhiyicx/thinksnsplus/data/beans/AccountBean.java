package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

/**
 * @author Catherine
 * @describe 用户账号bean
 * @date 2017/7/26
 * @contact email:648129313@qq.com
 */
@Entity
public class AccountBean implements Parcelable{

    @Id
    private Long id;
    @Unique
    private String accountName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public AccountBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.accountName);
    }

    protected AccountBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.accountName = in.readString();
    }

    @Generated(hash = 1048446462)
    public AccountBean(Long id, String accountName) {
        this.id = id;
        this.accountName = accountName;
    }

    public static final Creator<AccountBean> CREATOR = new Creator<AccountBean>() {
        @Override
        public AccountBean createFromParcel(Parcel source) {
            return new AccountBean(source);
        }

        @Override
        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };
}
