package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLL_MONEY;
import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLL_TYPE;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.DOWNLOAD_TOLL;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.LOOK_TOLL;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment.OLDTOLL;

/**
 * @Author Jliuer
 * @Date 2017/05/25/16:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PictureTollFragment extends TSFragment {
    @BindView(R.id.tv_choose_tip_toll_ways)
    TextView mTvChooseTipTollWays;
    @BindView(R.id.rb_ways_one)
    RadioButton mRbWaysOne;
    @BindView(R.id.rb_ways_two)
    RadioButton mRbWaysTwo;
    @BindView(R.id.rb_days_group_toll_ways)
    RadioGroup mRbDaysGroupTollWays;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    private ArrayList<Float> mSelectDays;

    private int mPayType;

    private double mRechargeMoney;

    private String mRechargeMoneyStr;

    private Toll mToll;

    private int[] tollMoney = new int[]{R.id.rb_one, R.id.rb_two, R.id.rb_three};

    private ActionPopupWindow mMoneyInstructionsPopupWindow;

    public static PictureTollFragment newInstance(Bundle bundle) {
        PictureTollFragment fragment = new PictureTollFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dynamic_send_toll_title);
    }

    @Override
    protected String setRightTitle() {
        mToolbarRight.setTextColor(getColor(R.color.themeColor));
        return getString(R.string.dynamic_send_toll_reset);
    }

    @Override
    protected void setRightClick() {
        mRbDaysGroupTollWays.clearCheck();
        mPayType = 0;
        mRechargeMoney = 0;
        mRbDaysGroup.clearCheck();
        mEtInput.setText("");
        mToll.reset();
        setConfirmEnable();
    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.dynamic_send_toll_count);
        try {// 第一次进入时没有值
            ImageBean imageBean = getArguments().getParcelable(OLDTOLL);
            mToll = imageBean.getToll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mToll == null) {
            mToll = new Toll();
        }
        initListener();
    }

    @Override
    protected void initData() {
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1f);
        mSelectDays.add(5f);
        mSelectDays.add(10f);
        initSelectDays(mSelectDays);
    }

    private void initSelectDays(List<Float> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(2)));
        initTollWays();
        initTollMoney();
    }

    private void initTollMoney() {
        if (mToll == null)
            return;
        if (mToll.getCustom_money() > 0) {
            mEtInput.setText(String.valueOf(mToll.getCustom_money()));
        } else {
            int position = mSelectDays.indexOf(mToll.getToll_money());
            if (position != -1) {
                mRbDaysGroup.check(tollMoney[position]);
            }
        }
    }

    private void initTollWays() {
        if (mToll == null)
            return;
        if (mToll.getToll_type() == LOOK_TOLL) {
            mRbDaysGroupTollWays.check(R.id.rb_ways_one);
        } else if (mToll.getToll_type() == DOWNLOAD_TOLL) {
            mRbDaysGroupTollWays.check(R.id.rb_ways_two);
        }
    }

    @Override
    protected void setLeftClick() {
        back();
    }

    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable(TOLL_TYPE,mToll);
        intent.putExtra(TOLL_TYPE,bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void initListener() {
        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mRechargeMoneyStr.contains(".")) {
                            initmMoneyInstructionsPop();
                            return;
                        }
                        mToll.setToll_type(mPayType);
                        back();
                    }
                });

        RxTextView.afterTextChangeEvents(mEtInput)
                .subscribe(new Action1<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void call(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        mRechargeMoneyStr = textViewAfterTextChangeEvent.editable().toString();
                        if (TextUtils.isEmpty(mRechargeMoneyStr)) {
                            return;
                        }

                        mRbDaysGroup.clearCheck();
                        try {
                            mRechargeMoney = Double.parseDouble(mRechargeMoneyStr);
                        } catch (NumberFormatException ne) {
                            mRechargeMoney = 0;
                        }
                        mToll.setCustom_money(((Double) mRechargeMoney).floatValue());
                        setConfirmEnable();
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
                                mToll.setToll_money(Double.valueOf(mRechargeMoney).floatValue());
                                break;
                            case R.id.rb_two:
                                mRechargeMoney = mSelectDays.get(1);
                                mToll.setToll_money(Double.valueOf(mRechargeMoney).floatValue());
                                break;
                            case R.id.rb_three:
                                mRechargeMoney = mSelectDays.get(2);
                                mToll.setToll_money(Double.valueOf(mRechargeMoney).floatValue());
                                break;
                            case -1:
                                mToll.setToll_money(0);
                                break;
                            default:
                        }
                        setConfirmEnable();
                        setCustomMoneyDefault();
                    }
                });

        RxRadioGroup.checkedChanges(mRbDaysGroupTollWays)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer checkedId) {
                        switch (checkedId) {
                            case R.id.rb_ways_one:
                                mPayType = LOOK_TOLL;
                                break;
                            case R.id.rb_ways_two:
                                mPayType = DOWNLOAD_TOLL;
                                break;
                        }
                        setConfirmEnable();
                    }
                });
    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
        mToll.setCustom_money(0);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_picture_toll;
    }

    @OnClick(R.id.bt_top)
    public void onViewClicked() {

    }

    private void setConfirmEnable() {
        mBtTop.setEnabled(mPayType > 0 && mRechargeMoney > 0);
    }

    public void initmMoneyInstructionsPop() {
        if (mMoneyInstructionsPopupWindow != null) {
            mMoneyInstructionsPopupWindow.show();
            return;
        }
        mMoneyInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.transaction_description))
                .desStr(getString(R.string.limit_monye))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mMoneyInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mMoneyInstructionsPopupWindow.show();
    }
}
