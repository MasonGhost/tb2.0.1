package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.wallet.PayType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
public class RechargeFragment extends TSFragment<RechargeContract.Presenter> implements RechargeContract.View {

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
    @BindView(R.id.bt_recharge_style)
    CombinationButton mBtRechargeStyle;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    private ActionPopupWindow mPayStylePopupWindow;// 性别选择弹框
    private ActionPopupWindow mRechargeInstructionsPopupWindow;// 充值说明选择弹框

    private int mPayType;

    private double mRechargeMoney;

    private ArrayList<Integer> mSelectDays;

    public static RechargeFragment newInstance() {
        return new RechargeFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_recharge;
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
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1);
        mSelectDays.add(5);
        mSelectDays.add(10);
        initSelectDays(mSelectDays);
    }

    private void initSelectDays(List<Integer> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.select_day), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.select_day), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.select_day), mSelectDays.get(2)));
    }


    private void initListener() {

        // 选择充值方式
        RxView.clicks(mBtRechargeStyle)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initPayStylePop();
                    }
                });
        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        showSnackSuccessMessage("mBtReCharge");
                    }
                });
        // 
        RxTextView.afterTextChangeEvents(mEtInput)
                .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        if (TextUtils.isEmpty(textViewAfterTextChangeEvent.editable().toString())) {
                            return;
                        }

                        if (textViewAfterTextChangeEvent.editable().toString().contains(".")) {
                            setCustomMoneyDefault();
                            DeviceUtils.hideSoftKeyboard(getContext(), mEtInput);
                            initmRechargeInstructionsPop();
                        } else {
                            mRbDaysGroup.clearCheck();
                            try {
                                mRechargeMoney = Double.parseDouble(textViewAfterTextChangeEvent.editable().toString());
                            } catch (NumberFormatException ne) {

                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        setCustomMoneyDefault();
                        mRechargeMoney = 0;
                    }
                });
        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer checkedId) {
                        switch (checkedId) {
                            case R.id.rb_one:
                                mRechargeMoney = mSelectDays.get(0);
                                break;
                            case R.id.rb_two:
                                mRechargeMoney = mSelectDays.get(1);
                                break;
                            case R.id.rb_three:
                                mRechargeMoney = mSelectDays.get(2);
                                break;
                        }
                        setCustomMoneyDefault();
                    }
                });

    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    /**
     * 充值方式选择弹框
     */
    private void initPayStylePop() {
        if (mPayStylePopupWindow != null) {
            mPayStylePopupWindow.show();
            return;
        }
        mPayStylePopupWindow = ActionPopupWindow.builder()
                .item2Str(getString(R.string.choose_pay_style_formart, getString(R.string.alipay)))
                .item3Str(getString(R.string.choose_pay_style_formart, getString(R.string.wxpay)))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayType = PayType.ALIPAY.value;
                        mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.alipay)));
                        mPayStylePopupWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayType = PayType.WX.value;
                        mBtRechargeStyle.setRightText(getString(R.string.choose_recharge_style_formart, getString(R.string.wxpay)));
                        mPayStylePopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPayStylePopupWindow.hide();
                    }
                })
                .build();
        mPayStylePopupWindow.show();
    }

    /**
     * 充值说明选择弹框
     */
    private void initmRechargeInstructionsPop() {
        if (mRechargeInstructionsPopupWindow != null) {
            mRechargeInstructionsPopupWindow.show();
            return;
        }
        mRechargeInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.recharge_instructions))
                .desStr(getString(R.string.recharge_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mRechargeInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mRechargeInstructionsPopupWindow.show();
    }

}
