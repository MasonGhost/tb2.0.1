package com.zhiyicx.thinksnsplus.modules.wallet.bill_detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.wallet.bill.BillListFragment.BILL_INFO;

/**
 * @Author Jliuer
 * @Date 2017/05/23/15:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillDetailFragment extends TSFragment {

    @BindView(R.id.bill_status)
    TextView mBillStatus;
    @BindView(R.id.tv_mine_money)
    TextView mTvMineMoney;
    @BindView(R.id.bill_user)
    TextView mBillUser;
    @BindView(R.id.bill_user_container)
    LinearLayout mBillUserContainer;
    @BindView(R.id.bill_user_head)
    TextView mBillUserHead;
    @BindView(R.id.bill_account)
    TextView mBillAccount;
    @BindView(R.id.bill_account_container)
    LinearLayout mBillAccountContainer;
    @BindView(R.id.bill_desc)
    TextView mBillDesc;
    @BindView(R.id.bill_time)
    TextView mBillTime;
    @BindView(R.id.iv_user_portrait)
    UserAvatarView mIvUserPortrait;

    private BillDetailBean mBillDetailBean;

    public static BillDetailFragment getInstance(Bundle bundle) {
        BillDetailFragment billDetailFragment = new BillDetailFragment();
        billDetailFragment.setArguments(bundle);
        return billDetailFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.account_detail);
    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected void initData() {
        mBillDetailBean = getArguments().getParcelable(BILL_INFO);
        int action = mBillDetailBean.getAction(); // action =0代表 money 增加
        int status = mBillDetailBean.getStatus();
        boolean is_user = mBillDetailBean.getUserInfoBean() != null;
//    channel    recharge_ping_p_p - 充值, widthdraw - 提现, user - 转账, reward - 打赏

        switch (mBillDetailBean.getChannel()) {
            case "recharge_ping_p_p":
                mBillUser.setText(getString(R.string.account_form_name));
                break;
            case "widthdraw":
                mBillUser.setText(R.string.account_to_name);
                break;
            case "user":
                mBillUser.setText(getString(action > 0 ? R.string.account_form_name : R.string.account_to_name));


                break;
            case "reward":
                mBillUser.setText(getString(action > 0 ? R.string.account_to_name : R.string.account_form_name));

                break;
            default:
                mBillUser.setText(getString(action > 0 ? R.string.account_to_name : R.string.account_form_name));
        }
        mBillStatus.setText(getString(status == 0 ? R.string.transaction_doing : (status == 1 ? R.string.transaction_success : R.string
                .transaction_fail)));
        String moneyStr = (status == 1 ? (action < 0 ? "-" : "+") : "") + String.format(Locale.getDefault(), getString(R.string.money_format),
                PayConfig.realCurrencyFen2Yuan(mBillDetailBean.getAmount()));
        mTvMineMoney.setText(moneyStr);
        mBillUserContainer.setVisibility(is_user ? View.VISIBLE : View.GONE);
        mBillAccountContainer.setVisibility(is_user ? View.GONE : View.VISIBLE);
        mBillAccount.setText(TextUtils.isEmpty(mBillDetailBean.getAccount()) ? mBillDetailBean.getChannel() : mBillDetailBean.getAccount());
        mBillDesc.setText(!TextUtils.isEmpty(mBillDetailBean.getBody()) ? mBillDetailBean.getBody() : "");
        mBillTime.setText(TimeUtils.string2_Dya_Week_Time(mBillDetailBean.getCreated_at()));

        if (!is_user) {
            return;
        }
        dealUserInfo(mBillDetailBean.getUserInfoBean());
    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_account;
    }

    private void dealUserInfo(UserInfoBean userInfoBean) {
        mBillUserHead.setText(userInfoBean.getName());
        ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvUserPortrait);

    }

}
