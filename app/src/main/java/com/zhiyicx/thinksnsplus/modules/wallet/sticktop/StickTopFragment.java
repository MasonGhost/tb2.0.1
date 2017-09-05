package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/05/22/11:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopFragment extends TSFragment<StickTopContract.Presenter> implements StickTopContract.View {
    /**
     * 如果只有 parent_id ，就是动态或者资讯的操作，如果有 child_id 则表示 评论
     */
    public static final String PARENT_ID = "parent_id";
    public static final String CHILD_ID = "child_id";

    public static final String TYPE_DYNAMIC = "dynamic"; // value 动态置顶
    public static final String TYPE_INFO = "info";  // value 资讯置顶
    public static final String TYPE = "type";    // 置顶的 key

    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.et_top_input)
    EditText mEtTopInput;
    @BindView(R.id.et_top_total)
    EditText mEtTopTotal;
    @BindView(R.id.bt_top)
    TextView mBtTop;
    @BindView(R.id.tv_dynamic_top_dec)
    TextView mTvDynamicTopDec;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;

    private List<Integer> mSelectDays;
    private int mCurrentDays;
    private float mInputMoney;
    private double mBlance;
    private ActionPopupWindow mStickTopInstructionsPopupWindow;

    private long parent_id, child_id;
    private String type;

    public static StickTopFragment newInstance(Bundle bundle) {
        StickTopFragment stickTopFragment = new StickTopFragment();
        stickTopFragment.setArguments(bundle);
        return stickTopFragment;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    protected void initData() {
        type = getArguments().getString(TYPE, "");
        parent_id = getArguments().getLong(PARENT_ID, -1L);
        child_id = getArguments().getLong(CHILD_ID, -1L);
        mBlance = mPresenter.getBalance();
        mTvDynamicTopDec.setText(String.format(getString(R.string.to_top_description), 200f, mBlance));
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.to_top);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1);
        mSelectDays.add(5);
        mSelectDays.add(10);
        initSelectDays(mSelectDays);
        initListener();
    }

    @Override
    public boolean insufficientBalance() {
        return mBlance < mCurrentDays * mInputMoney;
    }

    @Override
    public void gotoRecharge() {
        startActivity(new Intent(getActivity(), WalletActivity.class));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_top;
    }

    @Override
    public double getInputMoney() {
        return mInputMoney;
    }

    @Override
    public int getTopDyas() {
        return mCurrentDays;
    }

    @Override
    public void topSuccess() {

    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS) {
            getActivity().finish();
        }
    }

    @Override
    public void updateBalance(double balance) {
        mBlance = balance;
        mTvDynamicTopDec.setText(String.format(getString(R.string.to_top_description), 200f, balance));
    }

    private void initListener() {
        mRbDaysGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_one:
                    mCurrentDays = mSelectDays.get(0);
                    break;
                case R.id.rb_two:
                    mCurrentDays = mSelectDays.get(1);
                    break;
                case R.id.rb_three:
                    mCurrentDays = mSelectDays.get(2);
                    break;
            }
            setConfirmEnable();
        });

        RxTextView.textChanges(mEtTopInput)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence)) {
                        try {
                            mInputMoney = Float.parseFloat(charSequence.toString());
                        } catch (NumberFormatException e) {
                            mInputMoney = 0f;
                        }
                    } else {
                        mInputMoney = 0f;
                    }
                    setConfirmEnable();
                }, throwable -> {
                    mInputMoney = 0f;
                    setConfirmEnable();
                });

        RxTextView.textChanges(mEtTopTotal)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> mBtTop.setText(getString(mBlance < mCurrentDays * mInputMoney
                        ? R.string.to_recharge : R.string.determine)));

        RxView.clicks(mBtTop)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (child_id > 0) {
                        mPresenter.stickTop(parent_id, child_id);
                    } else {
                        mPresenter.stickTop(parent_id);
                    }
                });
    }

    private void initSelectDays(List<Integer> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.select_day), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.select_day), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.select_day), mSelectDays.get(2)));
    }

    private void setConfirmEnable() {
        boolean enable = mCurrentDays > 0 && mInputMoney > 0;
        mBtTop.setEnabled(enable);
//        if (!enable)
//            return;
        mEtTopTotal.setText(String.valueOf(mCurrentDays * mInputMoney));
    }

    @Override
    public void initStickTopInstructionsPop() {
        if (mStickTopInstructionsPopupWindow != null) {
            mStickTopInstructionsPopupWindow.show();
            return;
        }
        mStickTopInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.sticktop_instructions))
                .desStr(getString(R.string.sticktop_instructions_detail))
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
