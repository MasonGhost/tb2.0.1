package com.zhiyicx.thinksnsplus.modules.wallet.bill_detail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsListBean;

/**
 * @Author Jliuer
 * @Date 2017/06/09/17:18
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillDetailBean implements Parcelable {
    /**
     * id	int	记录id
     * owner_id	int	所属者id
     * channel	string	操作类型 recharge_ping_p_p - 充值, widthdraw - 提现, user - 转账, reward - 打赏
     * account	string	账户
     * title	string	标题
     * body	string	内容
     * action（type）	int	1 - 收入 -1 - 支出
     * amount	int	金额，分单位
     * status	int	订单状态，0: 等待，1：成功，-1: 失败
     */
    private int status;
    private long owner_id;
    @SerializedName(value = "action", alternate = {"type"})
    private int action;
    private int amount;
    @SerializedName(value = "account", alternate = {"target_id"})
    private String account;
    private String title;
    private String body;
    private String created_at;
    @SerializedName(value = "channel", alternate = {"target_type"})
    private String channel;
    private UserInfoBean mUserInfoBean;

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(long owner_id) {
        this.owner_id = owner_id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeLong(this.owner_id);
        dest.writeInt(this.action);
        dest.writeInt(this.amount);
        dest.writeString(this.account);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeString(this.created_at);
        dest.writeString(this.channel);
        dest.writeParcelable(this.mUserInfoBean, flags);
    }

    public BillDetailBean() {
    }

    protected BillDetailBean(Parcel in) {
        this.status = in.readInt();
        this.owner_id = in.readLong();
        this.action = in.readInt();
        this.amount = in.readInt();
        this.account = in.readString();
        this.title = in.readString();
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

    public static BillDetailBean recharge2Bill(RechargeSuccessBean rechargeSuccessBean, int ratio) {
        BillDetailBean billDetailBean = new BillDetailBean();
        billDetailBean.setAccount(rechargeSuccessBean.getAccount());
        billDetailBean.setAction(rechargeSuccessBean.getAction());
        billDetailBean.setAmount((int) PayConfig.realCurrency2GameCurrency(rechargeSuccessBean.getAmount(), ratio));
        billDetailBean.setBody(TextUtils.isEmpty(rechargeSuccessBean.getBody()) ? rechargeSuccessBean.getSubject() : rechargeSuccessBean.getBody());
        billDetailBean.setTitle(rechargeSuccessBean.getSubject());
        billDetailBean.setChannel(rechargeSuccessBean.getChannel());
        billDetailBean.setCreated_at(rechargeSuccessBean.getCreated_at());
        billDetailBean.setStatus(rechargeSuccessBean.getStatus());
        billDetailBean.setOwner_id(rechargeSuccessBean.getUser_id());
        billDetailBean.setUserInfoBean(rechargeSuccessBean.getUserInfoBean());
        return billDetailBean;
    }

    public static BillDetailBean withdrawals2Bill(WithdrawalsListBean withdrawalsListBean, int ratio) {
        BillDetailBean billDetailBean = new BillDetailBean();
        billDetailBean.setAccount(withdrawalsListBean.getAccount());
        billDetailBean.setAction(2);
        billDetailBean.setAmount((int) PayConfig.realCurrency2GameCurrency(withdrawalsListBean.getValue(), ratio));
        billDetailBean.setBody(AppApplication.getContext().getResources().getString(R.string.withdraw));
        billDetailBean.setChannel(withdrawalsListBean.getType());
        billDetailBean.setCreated_at(withdrawalsListBean.getCreated_at());
        billDetailBean.setStatus(withdrawalsListBean.getStatus());
        return billDetailBean;
    }

}
