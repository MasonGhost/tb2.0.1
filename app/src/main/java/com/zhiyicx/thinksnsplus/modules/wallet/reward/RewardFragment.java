package com.zhiyicx.thinksnsplus.modules.wallet.reward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.klinker.android.link_builder.Link;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

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
    public static final String BUNDLE_SOURCE_USER = "user";

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
    @BindView(R.id.tv_reward_success_money)
    TextView mTvRewardSuccessMoney;
    @BindView(R.id.iv_cancle)
    ImageView mIvCancle;

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;
    @BindView(R.id.ll_input_contanier)
    View mLlInputContanier;

    @BindView(R.id.ll_content)
    View mLlContent;
    @BindView(R.id.ll_success)
    View mLlSuccess;
    @BindView(R.id.iv_reward_avatar)
    UserAvatarView mImageView;
    @BindView(R.id.tv_name)
    TextView mTvName;
    /**
     * reward type
     */
    private RewardType mRewardType;
    private long mSourceId;
    private UserInfoBean mUserInfoBean;


    private double mRewardMoney; // money choosed for reward

    private List<Float> mRechargeLables; // recharge lables

    private ActionPopupWindow mStickTopInstructionsPopupWindow;

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
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("reward type not be null");
        }
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        mRewardType = (RewardType) getArguments().getSerializable(BUNDLE_REWARD_TYPE);
        mUserInfoBean = (UserInfoBean) getArguments().getSerializable(BUNDLE_SOURCE_USER);
        mSourceId = getArguments().getLong(BUNDLE_SOURCE_ID);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.dynamic_send_toll_reset);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mRbDaysGroup.clearCheck();
        mEtInput.setText("");
    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.choose_reward_money);
        initListener();

    }

    @Override
    protected void initData() {
        initRechargeLables();
        mCustomMoney.setText(R.string.app_name_simple);
        if (mUserInfoBean != null) {
            mImageView.setVisibility(View.VISIBLE);
            ImageUtils.loadUserHead( mUserInfoBean,mImageView ,false);
            mTvName.setVisibility(View.VISIBLE);
            mTvName.setText(mUserInfoBean.getName());
        }
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        if (prompt == Prompt.SUCCESS) {
            try {
                rewardSuccess(mRewardMoney);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rewardSuccess(double rewardMoney) {
        mLlContent.setVisibility(View.GONE);
        mBtTop.setEnabled(true);
        mBtTop.setText(getString(R.string.haode));
        mTvRewardSuccessMoney.setText(((int) rewardMoney + mPresenter.getWalletGoldName()));
        mLlSuccess.setVisibility(View.VISIBLE);
    }

    private void initRechargeLables() {
        String[] amount = new String[]{};
        mRechargeLables = new ArrayList<>();
        try {
            amount = mSystemConfigBean.getSite().getReward().getAmounts().split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (amount.length > 0) {// 配置的打赏金额
            mRechargeLables.add((float) PayConfig.realCurrencyFen2Yuan(Float.parseFloat(amount[0])));
            mRechargeLables.add((float) PayConfig.realCurrencyFen2Yuan(Float.parseFloat(amount[1])));
            mRechargeLables.add((float) PayConfig.realCurrencyFen2Yuan(Float.parseFloat(amount[2])));
        } else {
            mRechargeLables.add(1f);
            mRechargeLables.add(5f);
            mRechargeLables.add(10f);
        }


        if (mRechargeLables == null) {
            return;
        }
        switch (mRechargeLables.size()) {
            case 6:
            case 5:
            case 4:
            case 3:
                setCheckboxText(mRbThree, mRechargeLables.get(2));
            case 2:
                setCheckboxText(mRbTwo, mRechargeLables.get(1));

            case 1:
                setCheckboxText(mRbOne, mRechargeLables.get(0));
                break;
            case 0:
                mLlRechargeChooseMoneyItem.setVisibility(View.GONE);
                break;
            default:
                break;

        }
    }

    private void setCheckboxText(RadioButton checkboxText, double text) {
        mLlRechargeChooseMoneyItem.setVisibility(View.VISIBLE);
        checkboxText.setText(String.format(getString(R.string.money_format_tb), text));
        Spannable spannable = new SpannableString(checkboxText.getText());
        spannable.setSpan(new AbsoluteSizeSpan(10, true), checkboxText.getText().length() - 2, checkboxText.getText().length(), 0);
        checkboxText.setText(spannable);
    }

    @Override
    public void showSnackErrorMessage(String message) {
        ToastUtils.showToast(message);
        configSureButton();
    }

    private void initListener() {

        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mBtTop.getText().equals(getString(R.string.haode))) {
                        setresultSuccess();
                    } else {

                        if (!mEtInput.getText().toString().isEmpty() && mRewardMoney != (int) mRewardMoney) {
                            DeviceUtils.hideSoftKeyboard(getContext(), mBtTop);
                            initStickTopInstructionsPop();
                        } else {
                            setSureBtEnable(false);
                            mPresenter.reward(PayConfig.realCurrencyYuan2Fen(mRewardMoney), mRewardType, mSourceId);
                        }
                    }
                });// 传入的是真实货币分单位

        // 取消
        RxView.clicks(mIvCancle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    getActivity().finish();
                });

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String mRechargeMoneyStr = charSequence.toString();
            if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                try {
                    mRewardMoney = Double.parseDouble(mRechargeMoneyStr);
                } catch (Exception ignored) {
                }
                if (mRbDaysGroup.getCheckedRadioButtonId() != -1) {
                    mRbDaysGroup.clearCheck();
                }
                mLlInputContanier.setBackgroundResource(R.drawable.shape_dynamic_topday_bg_radus_theme);
            } else {
                mRewardMoney = 0;
                mLlInputContanier.setBackgroundResource(R.drawable.shape_dynamic_topday_bg_radus_grey);
            }
            configSureButton();
        }, throwable -> {
            throwable.printStackTrace();
            setCustomMoneyDefault();
            mRewardMoney = 0;
            configSureButton();
        });

        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        setCustomMoneyDefault();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mRewardMoney = Double.parseDouble(getString(R.string.money_format, mRechargeLables.get(0)));
                            break;
                        case R.id.rb_two:
                            mRewardMoney = Double.parseDouble(getString(R.string.money_format, mRechargeLables.get(1)));
                            break;
                        case R.id.rb_three:
                            mRewardMoney = Double.parseDouble(getString(R.string.money_format, mRechargeLables.get(2)));
                            break;
                        default:
                            break;
                    }
                    if (checkedId != -1) {
                        configSureButton();
                    }
                });

    }

    private void setresultSuccess() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    private void configSureButton() {
        setSureBtEnable(mRewardMoney > 0);
//        mToolbarRight.setEnabled(mRewardMoney > 0);
    }

    @Override
    public void setSureBtEnable(boolean b) {
        mBtTop.setEnabled(b);
    }

    /**
     * @param context    not application context clink
     * @param rewardType reward type {@link RewardType}
     */
    public static void startRewardActivity(Context context, RewardType rewardType, long sourceId, UserInfoBean userInfoBean) {

        Intent intent = new Intent(context, RewardActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REWARD_TYPE, rewardType);
        bundle.putSerializable(BUNDLE_SOURCE_ID, sourceId);
        if (userInfoBean != null) {
            bundle.putSerializable(BUNDLE_SOURCE_USER, userInfoBean);
        }
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, rewardType.id);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }

    }


    public void initStickTopInstructionsPop() {
        if (mStickTopInstructionsPopupWindow != null) {
            mStickTopInstructionsPopupWindow.show();
            return;
        }
        mStickTopInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.sticktop_reward_instructions))
                .desStr(getString(R.string.sticktop_reward_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mStickTopInstructionsPopupWindow.hide())
                .build();
        mStickTopInstructionsPopupWindow.show();
    }
}
