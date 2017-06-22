package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/01/17:12
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollFragment extends TSFragment<DynamicCommentTollContract.Presenter>
        implements DynamicCommentTollContract.View {

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
    @BindView(R.id.ll_comment_choose_money_item)
    LinearLayout mLlCommentChooseMoneyItem;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    private List<Float> mSelectMoney;

    private float mCommentMoney;

    private ActionPopupWindow mTollCommentInstructionsPopupWindow;

    public static DynamicCommentTollFragment newInstance() {
        return new DynamicCommentTollFragment();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dynamic_comment_toll);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mLlCommentChooseMoneyItem.setVisibility(View.VISIBLE);
        mTvChooseTip.setText(R.string.dynamic_comment_toll_money);
        mSelectMoney = new ArrayList<>();
        mSelectMoney.add(1f);
        mSelectMoney.add(5f);
        mSelectMoney.add(10f);
        initSelectDays(mSelectMoney);
        initListener();
    }

    @Override
    protected void initData() {

    }

    @Override
    public float getCommentMoney() {
        return mCommentMoney;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_comment_toll;
    }

    private void initSelectDays(List<Float> mSelectMoney) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectMoney.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectMoney.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectMoney.get(2)));
    }

    private void initListener() {
        mRbDaysGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId != -1) {
                    resetEtInput();
                }
                switch (checkedId) {
                    case R.id.rb_one:
                        mCommentMoney = mSelectMoney.get(0);
                        break;
                    case R.id.rb_two:
                        mCommentMoney = mSelectMoney.get(1);
                        break;
                    case R.id.rb_three:
                        mCommentMoney = mSelectMoney.get(2);
                        break;
                    default:
                        break;
                }
                if (checkedId != -1) {
                    setConfirmEnable();
                }
            }
        });

        RxTextView.textChanges(mEtInput)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            mCommentMoney = Float.parseFloat(charSequence.toString());
                            mRbDaysGroup.clearCheck();
                        } else {
                            mCommentMoney = 0f;
                        }
                        setConfirmEnable();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mCommentMoney = 0f;
                    }
                });


        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.<Void>bindToLifecycle())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.tollDynamicComment(0L);
                        DeviceUtils.hideSoftKeyboard(getContext(), mEtInput);
                    }
                });
    }

    @Override
    public void initWithdrawalsInstructionsPop() {
        if (mTollCommentInstructionsPopupWindow != null) {
            mTollCommentInstructionsPopupWindow.show();
            return;
        }
        mTollCommentInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.monye_instructions))
                .desStr(getString(R.string.limit_monye))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mTollCommentInstructionsPopupWindow.hide();
                    }
                })
                .build();
        mTollCommentInstructionsPopupWindow.show();
    }

    private void setConfirmEnable() {
        mBtTop.setEnabled(mCommentMoney > 0);
    }

    private void resetEtInput() {
        mEtInput.setText("");
        DeviceUtils.hideSoftKeyboard(getContext(), mEtInput);
    }
}
