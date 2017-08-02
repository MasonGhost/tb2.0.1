package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.pingplusplus.android.Pingpp;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 打赏页
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class RewardFragment extends TSFragment<RewardContract.Presenter> implements RewardContract.View {
    public static final String BUNDLE_DATA = "walletconfig";
    public static final String BUNDLE_REWARD_TYPE = "reward_type";

    @BindView(R.id.ll_recharge_choose_money_item)
    LinearLayout mLlRechargeChooseMoneyItem;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    private RewardType mRewardType; // reward type
    private WalletConfigBean mWalletConfigBean; // wallet config info


    private double mRewardMoney; // money choosed for reward

    private List<Float> mRechargeLables; // recharge lables

    public static RewardFragment newInstance(Bundle bundle) {
        RewardFragment rechargeFragment = new RewardFragment();
        rechargeFragment.setArguments(bundle);
        return rechargeFragment;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_reward;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.recharge);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.choose_recharge_money);
        initListener();

    }

    @Override
    protected void initData() {
        initRechargeLables();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result", "");
                /* 处理返回值
                 * "success" - 支付成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                int id = UIUtils.getResourceByName("pay_" + result, "string", getContext());
                if (result.contains("success")) {
                    showSnackSuccessMessage(getString(id));
                } else {
                    showSnackErrorMessage(getString(id));
                }
                if (result.equals("success")) {
//                    mPresenter.rechargeSuccess(mPayChargeId);
                }
            }
        }
    }


    private void initRechargeLables() {
        if (getArguments() != null) {
            mWalletConfigBean = getArguments().getParcelable(BUNDLE_DATA);
        }
        mRechargeLables = mWalletConfigBean.getLabels();

        if (mRechargeLables == null) {
            return;
        }
        switch (mRechargeLables.size()) {
            case 6:
            case 5:
            case 4:
            case 3:
                mRbThree.setVisibility(View.VISIBLE);
                mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(2) / 100f));
            case 2:
                mRbTwo.setVisibility(View.VISIBLE);
                mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(1) / 100f));
            case 1:
                mRbOne.setVisibility(View.VISIBLE);
                mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(0) / 100f));
                mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
                break;
            case 0:
                mLlRechargeChooseMoneyItem.setVisibility(View.GONE);
                break;
            default:

        }
    }


    private void initListener() {

        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> LogUtils.d("sure click"));// 传入的是真实货币分单位

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String mRechargeMoneyStr = charSequence.toString();
            if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                mRewardMoney = Double.parseDouble(mRechargeMoneyStr);
                if (mRbDaysGroup.getCheckedRadioButtonId() != -1) {
                    mRbDaysGroup.clearCheck();
                }
            } else {
                mRewardMoney = 0;
            }
            configSureButton();
        }, throwable -> {
            throwable.printStackTrace();
            setCustomMoneyDefault();
            mRewardMoney = 0;
            configSureButton();
        });

        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        setCustomMoneyDefault();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mRewardMoney = mRechargeLables.get(0);
                            break;
                        case R.id.rb_two:
                            mRewardMoney = mRechargeLables.get(1);
                            break;
                        case R.id.rb_three:
                            mRewardMoney = mRechargeLables.get(2);
                            break;
                    }
                    if (checkedId != -1) {
                        configSureButton();
                    }
                });

    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    private void configSureButton() {
        mBtTop.setEnabled(mRewardMoney > 0);
    }


    /**
     *
     * @param context not application context clink
     * @param rewardType  reward type {@link RewardType}
     */
    public static void startRewardActivity(Context context, RewardType rewardType) {

        Intent intent = new Intent(context, RewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REWARD_TYPE, rewardType);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
