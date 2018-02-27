package com.zhiyicx.thinksnsplus.modules.wallet.sticktop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.ErrorCodeConfig.REPEAT_OPERATION;

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

    /**
     * value 动态置顶
     */
    public static final String TYPE_DYNAMIC = "dynamic";

    /**
     * value 资讯置顶
     */
    public static final String TYPE_INFO = "info";

    /**
     * value 帖子置顶
     */
    public static final String TYPE_POST = "post";

    /**
     * value 管理员置顶
     */
    public static final String TYPE_MANAGER = "manager";

    /**
     * 置顶的 key
     */
    public static final String TYPE = "type";


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

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;
    @BindView(R.id.tv_total_money)
    TextView mTotalMoney;

    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.ll_top_money)
    LinearLayout mLlTopMoney;
    @BindView(R.id.ll_top_total_money)
    LinearLayout mLlTopTotalMoney;

    private List<Integer> mSelectDays;
    private int mCurrentDays;
    private int mInputMoney;
    private long mBlance;
    private double mInputMoneyDouble;
    private ActionPopupWindow mStickTopInstructionsPopupWindow;

    private long parent_id, child_id;
    private String type;
    private boolean isManager;
    private int mErrorCode;

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
        isManager = getArguments().getBoolean(TYPE_MANAGER);
        mBlance = mPresenter.getBalance();
        mTvDynamicTopDec.setText(getString(R.string.to_top_description, 200, mPresenter.getIntegrationGoldName(), mBlance));
        String moneyName = mPresenter.getIntegrationGoldName();
        mCustomMoney.setText(moneyName);
        mTotalMoney.setText(moneyName);

        if (isManager) {
            mLine.setVisibility(View.GONE);
            mLlTopMoney.setVisibility(View.GONE);
            mLlTopTotalMoney.setVisibility(View.GONE);
        }
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
//        mSystemConfigBean = mPresenter.getSystemConfigBean();
//        if (mSystemConfigBean.getCurrencyRecharge() != null && mSystemConfigBean.getCurrencyRecharge().isOpen()) {
        startActivity(new Intent(getActivity(), MineIntegrationActivity.class));
//        }else {
//            showSnackErrorMessage(getString(R.string.current_money_not_enouph));
//        }
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
    public void onFailure(String message, int code) {
        mErrorCode = code;
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS) {
            getActivity().finish();
        } else if (prompt == Prompt.ERROR && mErrorCode == REPEAT_OPERATION) {
            getActivity().finish();
        }
    }

    @Override
    public void updateBalance(long balance) {
        mBlance = balance;
        mTvDynamicTopDec.setText(String.format(getString(R.string.to_top_description), 200, mPresenter.getIntegrationGoldName(), balance));
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
                default:
            }
            setConfirmEnable();
        });

        RxTextView.textChanges(mEtTopInput)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence)) {
                        try {
                            mInputMoneyDouble = Double.parseDouble(charSequence.toString());
                            mInputMoney = Integer.parseInt(charSequence.toString());
                        } catch (Exception e) {
                            mInputMoney = -1;
                        }
                    } else {
                        mInputMoney = 0;
                        mInputMoneyDouble = 0f;
                    }
                    setConfirmEnable();
                }, throwable -> {
                    mInputMoney = 0;
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

    @Override
    public boolean useInputMoney() {
        return !mEtTopInput.getText().toString().isEmpty();
    }

    private void initSelectDays(List<Integer> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.select_day), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.select_day), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.select_day), mSelectDays.get(2)));
    }

    private void setConfirmEnable() {
        boolean enable = mCurrentDays > 0 && mInputMoneyDouble > 0;
        mBtTop.setEnabled(enable);
        long money = (long) (mCurrentDays * mInputMoneyDouble);
        mEtTopTotal.setText(money > 0 ? money + "" : "");
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

    /**
     * @param context
     * @param type      资源类型：动态、资讯等
     * @param parent_id 资源的ic
     * @param child_id  评论的 id
     */
    public static void startSticTopActivity(Context context, String type, Long parent_id, Long child_id) {
        Bundle bundle = new Bundle();
        bundle.putString(StickTopFragment.TYPE, type);// 资源类型
        bundle.putLong(StickTopFragment.PARENT_ID, parent_id);// 资源id
        if (child_id != null) {
            bundle.putLong(StickTopFragment.CHILD_ID, child_id);
        }
        Intent intent = new Intent(context, StickTopActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startSticTopActivity(Context context, String type, Long parent_id) {
        startSticTopActivity(context, type, parent_id, null);
    }

    public static void startSticTopActivity(Context context, String type, Long parent_id, Long child_id, boolean isManager) {
        Bundle bundle = new Bundle();

        // 资源类型
        bundle.putString(StickTopFragment.TYPE, type);

        // 资源id
        bundle.putLong(StickTopFragment.PARENT_ID, parent_id);

        bundle.putBoolean(StickTopFragment.TYPE_MANAGER, isManager);

        if (child_id != null) {
            bundle.putLong(StickTopFragment.CHILD_ID, child_id);
        }
        Intent intent = new Intent(context, StickTopActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startSticTopActivity(Context context, String type, Long parent_id, boolean isManager) {
        startSticTopActivity(context, type, parent_id, null, isManager);
    }

}
