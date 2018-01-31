package com.zhiyicx.thinksnsplus.modules.wallet.bill_detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

/**
 * @Author Jliuer
 * @Date 2017/06/09/17:18
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillDetailBean implements Parcelable{
    private int status;
    private int action;
    private int amount;
    private String account;
    private String body;
    private String created_at;
    private String channel;
    private UserInfoBean mUserInfoBean;

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    private BillDetailBean() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public static BillDetailBean recharge2Bill(RechargeSuccessBean rechargeSuccessBean,int ratio) {
        BillDetailBean billDetailBean = new BillDetailBean();
        billDetailBean.setAccount(rechargeSuccessBean.getAccount());
        billDetailBean.setAction(rechargeSuccessBean.getAction());
        billDetailBean.setAmount((int)PayConfig.realCurrency2GameCurrency(rechargeSuccessBean.getAmount(),ratio));
        billDetailBean.setBody(TextUtils.isEmpty(rechargeSuccessBean.getBody())?rechargeSuccessBean.getSubject():rechargeSuccessBean.getBody());
        billDetailBean.setChannel(rechargeSuccessBean.getChannel());
        billDetailBean.setCreated_at(rechargeSuccessBean.getCreated_at());
        billDetailBean.setStatus(rechargeSuccessBean.getStatus());
        billDetailBean.setUserInfoBean(rechargeSuccessBean.getUserInfoBean());
        return billDetailBean;
    }

    public static BillDetailBean withdrawals2Bill(WithdrawalsListBean withdrawalsListBean,int ratio) {
        BillDetailBean billDetailBean = new BillDetailBean();
        billDetailBean.setAccount(withdrawalsListBean.getAccount());
        billDetailBean.setAction(2);
        billDetailBean.setAmount((int)PayConfig.realCurrency2GameCurrency(withdrawalsListBean.getValue(),ratio));
        billDetailBean.setBody("提现");
        billDetailBean.setChannel(withdrawalsListBean.getType());
        billDetailBean.setCreated_at(withdrawalsListBean.getCreated_at());
        billDetailBean.setStatus(withdrawalsListBean.getStatus());
        return billDetailBean;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.action);
        dest.writeInt(this.amount);
        dest.writeString(this.account);
        dest.writeString(this.body);
        dest.writeString(this.created_at);
        dest.writeString(this.channel);
        dest.writeParcelable(this.mUserInfoBean, flags);
    }

    protected BillDetailBean(Parcel in) {
        this.status = in.readInt();
        this.action = in.readInt();
        this.amount = in.readInt();
        this.account = in.readString();
        this.body = in.readString();
        this.created_at = in.readString();
        this.channel = in.readString();
        this.mUserInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    public static final Creator<BillDetailBean> CREATOR = new Creator<BillDetailBean>() {
        @Override
        public BillDetailBean createFromParcel(Parcel source) {
            return new BillDetailBean(source);
        }

        @Override
        public BillDetailBean[] newArray(int size) {
            return new BillDetailBean[size];
        }
    };
}
