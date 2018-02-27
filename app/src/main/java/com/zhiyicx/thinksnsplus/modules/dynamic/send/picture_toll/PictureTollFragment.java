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
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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

    public static final String MONEY_NAME = "MONEY_NAME";
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

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;

    @Inject
    SystemRepository mSystemRepository;

    private ArrayList<Float> mSelectMoney;

    private int mPayType;

    private long mRechargeMoney;

    private String mRechargeMoneyStr;

    private Toll mToll;

    private int[] tollMoney = new int[]{R.id.rb_one, R.id.rb_two, R.id.rb_three};

    private ActionPopupWindow mMoneyInstructionsPopupWindow;

    private String mMoneyName;

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
        DaggerPictureTollComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .build()
                .inject(this);

        mSelectMoney = new ArrayList<>();
        mSelectMoney.add(1f);
        mSelectMoney.add(5f);
        mSelectMoney.add(10f);
        mMoneyName = getString(R.string.defualt_golde_name);

        mSystemConfigBean = mSystemRepository.getAppConfigInfoFromLocal();

        if (mSystemConfigBean != null) {
            int[] amount = new int[]{};
            if (mSystemConfigBean.getFeed() != null) {
                amount = mSystemConfigBean.getFeed().getItems();
            }
            if (amount != null && amount.length > 2) {
                mSelectMoney.add(0,(float) PayConfig.realCurrency2GameCurrency(amount[0], mSystemConfigBean.getWallet_ratio()));
                mSelectMoney.add(1,(float) PayConfig.realCurrency2GameCurrency(amount[1], mSystemConfigBean.getWallet_ratio()));
                mSelectMoney.add(2,(float) PayConfig.realCurrency2GameCurrency(amount[2], mSystemConfigBean.getWallet_ratio()));
            }
            if (mSystemConfigBean.getSite() != null && mSystemConfigBean.getSite().getIntegration_gold_name() != null) {
                mMoneyName = mSystemConfigBean.getSite().getIntegration_gold_name().getName();
            }
        }
        initSelectDays(mSelectMoney);
        mCustomMoney.setText(mMoneyName);
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
        if (mToll == null) {
            return;
        }
        if (mToll.getCustom_money() > 0) {
            mEtInput.setText(String.valueOf(mToll.getCustom_money()));
        } else {
            int position = mSelectMoney.indexOf((float) mToll.getToll_money());
            if (position != -1) {
                mRbDaysGroup.check(tollMoney[position]);
            }
        }
    }

    private void initTollWays() {
        if (mToll == null) {
            return;
        }
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

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TOLL_TYPE, mToll);
        intent.putExtra(TOLL_TYPE, bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void initListener() {
        // 确认
        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mRechargeMoney <= 0) {
                        initmMoneyInstructionsPop();
                        return;
                    }
                    mToll.setToll_type(mPayType);
                    back();
                });

        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            mRechargeMoneyStr = charSequence.toString();
            if (TextUtils.isEmpty(mRechargeMoneyStr)) {
                return;
            }
            mRbDaysGroup.clearCheck();
            mToll.setToll_money(0);
            try {
                mRechargeMoney = Long.parseLong(mRechargeMoneyStr);
            } catch (NumberFormatException ne) {
                showSnackErrorMessage(getString(R.string.limit_monye_death));
                mRechargeMoney = 0;
            }
            mToll.setCustom_money(mRechargeMoney);
            setConfirmEnable();
        }, throwable -> {
            throwable.printStackTrace();
            setCustomMoneyDefault();
            mRechargeMoney = 0;
        });


        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    switch (checkedId) {
                        case R.id.rb_one:
                            mRechargeMoney = mSelectMoney.get(0).longValue();
                            mToll.setToll_money(Double.valueOf(mRechargeMoney).longValue());
                            break;
                        case R.id.rb_two:
                            mRechargeMoney = mSelectMoney.get(1).longValue();
                            mToll.setToll_money(Double.valueOf(mRechargeMoney).longValue());
                            break;
                        case R.id.rb_three:
                            mRechargeMoney = mSelectMoney.get(2).longValue();
                            mToll.setToll_money(Double.valueOf(mRechargeMoney).longValue());
                            break;
                        case -1:
                            return;
                        default:
                    }
                    setConfirmEnable();
                    setCustomMoneyDefault();
                });

        RxRadioGroup.checkedChanges(mRbDaysGroupTollWays)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    switch (checkedId) {
                        case R.id.rb_ways_one:
                            mPayType = LOOK_TOLL;
                            break;
                        case R.id.rb_ways_two:
                            mPayType = DOWNLOAD_TOLL;
                            break;
                        default:
                            break;
                    }
                    setConfirmEnable();
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
                .desStr(String.format(Locale.getDefault(), getString(R.string.limit_monye), mMoneyName))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mMoneyInstructionsPopupWindow.hide())
                .build();
        mMoneyInstructionsPopupWindow.show();
    }
}
