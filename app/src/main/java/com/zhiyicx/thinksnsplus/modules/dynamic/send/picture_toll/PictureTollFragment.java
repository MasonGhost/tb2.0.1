package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

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
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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

    public static PictureTollFragment newInstance(){
        return new PictureTollFragment();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dynamic_send_toll_title);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.dynamic_send_toll_count);
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
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(2)));
    }

    private void initListener() {
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

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_picture_toll;
    }

    @OnClick(R.id.bt_top)
    public void onViewClicked() {

    }
}
