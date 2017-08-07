package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 打赏页
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class RewardFragment extends TSFragment<RewardContract.Presenter> implements RewardContract.View {
    public static final String BUNDLE_REWARD_TYPE = "reward_type";
    public static final String BUNDLE_SOURCE_ID = "sourceId";

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
    private long mSourceId;


    private double mRewardMoney; // money choosed for reward

    private List<Float> mRechargeLables; // recharge lables

    public static RewardFragment newInstance(Bundle bundle) {
        RewardFragment rechargeFragment = new RewardFragment();
        rechargeFragment.setArguments(bundle);
        return rechargeFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_reward;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.reward);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("reward type not be null");
        }
        mRewardType = (RewardType) getArguments().getSerializable(BUNDLE_REWARD_TYPE);
        mSourceId = getArguments().getLong(BUNDLE_SOURCE_ID);
    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.choose_reward_money);
        initListener();

    }

    @Override
    protected void initData() {
        initRechargeLables();
    }

    @Override
    public void rewardSuccess() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private void initRechargeLables() {

        mRechargeLables = new ArrayList<>();
        mRechargeLables.add(1000f);
        mRechargeLables.add(5000f);
        mRechargeLables.add(10000f);

        if (mRechargeLables == null) {
            return;
        }
        switch (mRechargeLables.size()) {
            case 6:
            case 5:
            case 4:
            case 3:
                mRbThree.setVisibility(View.VISIBLE);
                mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(2) / PayConfig.MONEY_UNIT));
            case 2:
                mRbTwo.setVisibility(View.VISIBLE);
                mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(1) / PayConfig.MONEY_UNIT));
            case 1:
                mRbOne.setVisibility(View.VISIBLE);
                mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mRechargeLables.get(0) / PayConfig.MONEY_UNIT));
                mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
                break;
            case 0:
                mLlRechargeChooseMoneyItem.setVisibility(View.GONE);
                break;
            default:

        }
    }

    @Override
    public void showSnackErrorMessage(String message) {
        super.showSnackErrorMessage(message);
        mBtTop.setEnabled(true);
    }

    private void initListener() {

        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mBtTop.setEnabled(false);
                    mPresenter.reward(PayConfig.realCurrencyYuan2Fen(mRewardMoney), mRewardType, mSourceId);
                });// 传入的是真实货币分单位

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
                            mRewardMoney = mRechargeLables.get(0)/PayConfig.MONEY_UNIT;
                            break;
                        case R.id.rb_two:
                            mRewardMoney = mRechargeLables.get(1)/PayConfig.MONEY_UNIT;
                            break;
                        case R.id.rb_three:
                            mRewardMoney = mRechargeLables.get(2)/PayConfig.MONEY_UNIT;
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
     * @param context    not application context clink
     * @param rewardType reward type {@link RewardType}
     */
    public static void startRewardActivity(Context context, RewardType rewardType, long sourceId) {

        Intent intent = new Intent(context, RewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REWARD_TYPE, rewardType);
        bundle.putSerializable(BUNDLE_SOURCE_ID, sourceId);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, rewardType.id);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }

    }
}
