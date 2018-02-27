package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;
import static com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchActivity.BUNDLE_TOPIC_IDS;
import static com.zhiyicx.thinksnsplus.modules.usertag.TagFrom.QA_PUBLISH;

/**
 * @author Catherine
 * @describe 悬赏页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
public class QARewardFragment extends TSFragment<QARewardContract.Presenter> implements QARewardContract.View,
        CenterInfoPopWindow.CenterPopWindowItem1ClickListener {

    public static final String BUNDLE_RESULT = "bundle_result";
    public static final String BUNDLE_QUESTION_ID = "bundle_question_id";

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
    @BindView(R.id.tv_invite_hint)
    TextView mTvInviteHint;
    @BindView(R.id.wc_invite)
    SwitchCompat mWcInvite;
    @BindView(R.id.ll_qa_set_money)
    LinearLayout mLlQaSetMoney;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_qa_select_expert)
    CombinationButton mBtQaSelectExpert;
    @BindView(R.id.wc_onlooker)
    SwitchCompat mWcOnlooker;
    @BindView(R.id.rl_onlooker)
    RelativeLayout mRlOnlooker;
    @BindView(R.id.rb_onlookers_one)
    RadioButton mRbOnlookersOne;
    @BindView(R.id.rb_onlookers_two)
    RadioButton mRbOnlookersTwo;
    @BindView(R.id.rb_onlookers_three)
    RadioButton mRbOnlookersThree;
    @BindView(R.id.rb_onlookers_days_group)
    RadioGroup mRbOnlookersDaysGroup;
    @BindView(R.id.ll_qa_set_onlookers_money)
    LinearLayout mLlQaSetOnlookersMoney;
    @BindView(R.id.bt_publish)
    TextView mBtPublish;
    @BindView(R.id.tv_reward_rule)
    TextView mTvRewardRule;
    @BindView(R.id.et_onlooker_input)
    EditText mEtOnlookerInput;
    @BindView(R.id.ll_onlooker_set_custom_money)
    LinearLayout mLlOnlookerSetCustomMoney;
    @BindView(R.id.rl_invite_container)
    RelativeLayout mRlInviteContainer;

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;

    // 悬赏相关
    private List<Float> mRewardLabels; // reward labels
    private double mRewardMoney; // money choosed for reward
    // 围观相关
    private List<Float> mOnLookerLabels; // reward labels
    private double mOnLookerMoney; // money pay for watch

    private CenterInfoPopWindow mRulePop; // 悬赏规则

    private QAPublishBean mQAPublishBean;
    private Long mQuestionId = 0L; //  发布之后重新设置悬赏
    private QAListInfoBean mQaListInfoBean;

    private ActionPopupWindow mInstructionsPopupWindow;

    public static QARewardFragment instance(Bundle bundle) {
        QARewardFragment fragment = new QARewardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_post_reward;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mQAPublishBean != null) {
            mQAPublishBean = mPresenter.getDraftQuestion(mQAPublishBean.getMark());
        }

    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null && getArguments().containsKey(BUNDLE_QUESTION_ID)) {
            mQuestionId = getArguments().getLong(BUNDLE_QUESTION_ID);
        }
        if (!mQuestionId.equals(0L)) {
            mBtPublish.setText(getString(R.string.determine));
            mToolbarCenter.setText(getString(R.string.qa_reward_public));
            mRlInviteContainer.setVisibility(View.GONE);
        }
        mTvChooseTip.setText(R.string.qa_publish_reward_set_money);
        mTvInviteHint.setText(getString(R.string.qa_publish_reward));
        mTvInviteHint.append(getString(R.string.qa_publish_reward_invite));
        initListener();

        mEtInput.clearFocus();
        mWcOnlooker.setFocusable(true);
        mWcOnlooker.requestFocus();
    }

    @Override
    protected void initData() {
        initDefaultMoney();
        initAlertPopupWindow();
        mQAPublishBean = getArguments().getParcelable(BUNDLE_PUBLISHQA_BEAN);
        QAPublishBean draft = mQAPublishBean == null ? null : mPresenter.getDraftQuestion(mQAPublishBean.getMark());
        if (draft != null) {
            mWcInvite.setChecked(draft.getAutomaticity() == 1);
            mWcOnlooker.setChecked(draft.getLook() == 1);
            double money = draft.getAmount();
            if (money > 0) {
                mEtInput.setText(String.valueOf(draft.getAmount()));
            }
            if (draft.getInvitations() != null && !draft.getInvitations().isEmpty()) {
                List<QAPublishBean.Invitations> typeIdsList = new ArrayList<>();
                QAPublishBean.Invitations typeIds = new QAPublishBean.Invitations();
                typeIds.setUser(draft.getInvitations().get(0).getUser());
                typeIds.setName(draft.getInvitations().get(0).getName());
                typeIdsList.add(typeIds);
                mBtQaSelectExpert.setRightText(draft.getInvitations().get(0).getName());
            }
            configSureButton();
            if (draft.isHasAgainEdite()) {
                mRlInviteContainer.setVisibility(View.GONE);
            }
        }
        String moneyName = mPresenter.getIntegrationGoldName();
        mCustomMoney.setText(moneyName);
    }

    @Override
    protected String setCenterTitle() {
        return mQuestionId.equals(0L) ? getString(R.string.qa_publish_reward_enable_skip) : getString(R.string.qa_reward_public);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_reset_money);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setRightClick() {
        // 重置
        resetValue();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TagFrom.QA_PUBLISH.id && resultCode == RESULT_OK) {// 选择专家
            ExpertBean expertBean = data.getExtras().getParcelable(BUNDLE_RESULT);
            if (expertBean != null) {
                List<QAPublishBean.Invitations> typeIdsList = new ArrayList<>();
                QAPublishBean.Invitations typeIds = new QAPublishBean.Invitations();
                typeIds.setUser(expertBean.getId());
                typeIds.setName(expertBean.getName());
                typeIdsList.add(typeIds);
                mBtQaSelectExpert.setRightText(expertBean.getName());
                mQAPublishBean.setInvitations(typeIdsList);
                mPresenter.saveQuestion(mQAPublishBean);
            }
            configSureButton();
        }
    }

    private void initDefaultMoney() {
        mRewardLabels = new ArrayList<>();
        mOnLookerLabels = new ArrayList<>();
        mRewardLabels.add(100f);
        mRewardLabels.add(500f);
        mRewardLabels.add(1000f);

        mOnLookerLabels.add(100f);
        mOnLookerLabels.add(500f);
        mOnLookerLabels.add(1000f);

        setRbValue(mRbOne, mRewardLabels.get(0));
        setRbValue(mRbTwo, mRewardLabels.get(1));
        setRbValue(mRbThree, mRewardLabels.get(2));

        setRbValue(mRbOnlookersOne, mOnLookerLabels.get(0));
        setRbValue(mRbOnlookersTwo, mRewardLabels.get(1));
        setRbValue(mRbOnlookersThree, mRewardLabels.get(2));

    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.DONE) {
            // 发布成功后，要跳转问题详情
//            startActivity(new Intent(getActivity(), QA_Activity.class));
            goToQuestionDetail();
            getActivity().finish();
        } else if (prompt == Prompt.ERROR) {
            mBtPublish.setEnabled(true);
        }
    }

    @Override
    protected void setLeftClick() {
        mPresenter.saveQuestion(packgQuestion());
        super.setLeftClick();
    }

    @Override
    public void onBackPressed() {
        mPresenter.saveQuestion(packgQuestion());
        getActivity().finish();
    }

    private void initAlertPopupWindow() {
        if (mRulePop != null) {
            return;
        }
        mRulePop = CenterInfoPopWindow.builder()
                .titleStr(getString(R.string.qa_publish_reward_rule))
                .desStr("xxxxxxxxxxxxxxxxxxx")
                .item1Str(getString(R.string.get_it))
                .item1Color(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(() -> mRulePop.hide())
                .parentView(getView())
                .build();
    }

    private void setRbValue(RadioButton radioButton, float f) {
        radioButton.setText(String.format(getString(R.string.dynamic_send_toll_select_money), f));
    }

    private void initListener() {
        // 悬赏金额修改
        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        resetRewardInput();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mRewardMoney = mRewardLabels.get(0);
                            break;
                        case R.id.rb_two:
                            mRewardMoney = mRewardLabels.get(1);
                            break;
                        case R.id.rb_three:
                            mRewardMoney = mRewardLabels.get(2);
                            break;
                        default:
                    }
                    if (checkedId != -1) {
                        configSureButton();
                    }
                });
        // 围观金额修改
        RxRadioGroup.checkedChanges(mRbOnlookersDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        resetOnlookerInput();
                    }
                    switch (checkedId) {
                        case R.id.rb_onlookers_one:
                            mOnLookerMoney = mOnLookerLabels.get(0);
                            break;
                        case R.id.rb_onlookers_two:
                            mOnLookerMoney = mOnLookerLabels.get(1);
                            break;
                        case R.id.rb_onlookers_three:
                            mOnLookerMoney = mOnLookerLabels.get(2);
                            break;
                        default:
                    }
                    if (checkedId != -1) {
                        configSureButton();
                    }
                });
        // 监听悬赏金额的输入变化
        RxTextView.textChanges(mEtInput)
                .subscribe(charSequence -> {
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
                    mRewardMoney = 0;
                    configSureButton();
                });
        RxTextView.textChanges(mEtOnlookerInput)
                .subscribe(charSequence -> {
                    String mRechargeMoneyStr = charSequence.toString();
                    if (mRechargeMoneyStr.replaceAll(" ", "").length() > 0) {
                        mOnLookerMoney = Double.parseDouble(mRechargeMoneyStr);
                        if (mRbOnlookersDaysGroup.getCheckedRadioButtonId() != -1) {
                            mRbOnlookersDaysGroup.clearCheck();
                        }
                    } else {
                        mOnLookerMoney = 0;
                    }
//                    configSureButton();
                }, throwable -> {
                    mOnLookerMoney = 0;
//                    configSureButton();
                });
        // 邀请开关
        mWcInvite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mBtQaSelectExpert.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            mRlOnlooker.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            mWcOnlooker.setChecked(false);
            resetExpert();
            configSureButton();
        });
        // 围观开关
        mWcOnlooker.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            mLlOnlookerSetCustomMoney.setVisibility(isChecked ? View.VISIBLE : View.GONE);
//            mLlQaSetOnlookersMoney.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            // 关闭之后 重置围观的数据
            if (!isChecked) {
                resetOnLookerMoney();
            }
//            configSureButton();
        });
        RxView.clicks(mBtQaSelectExpert)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转搜索选择专家列表
                    Intent intent = new Intent(getActivity(), ExpertSearchActivity.class);
                    Bundle bundle = new Bundle();
                    String topic_ids = "";
                    if (mQAPublishBean.getTopics() != null) {
                        for (QAPublishBean.Topic qaTopicBean : mQAPublishBean.getTopics()) {
                            topic_ids += qaTopicBean.getId() + ",";
                        }
                    }
                    bundle.putString(BUNDLE_TOPIC_IDS, topic_ids);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, QA_PUBLISH.id);
                });
        RxView.clicks(mBtPublish)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mRewardMoney != (int) mRewardMoney) {
                        initInstructionsPop(R.string.limit_monye_death);
                        return;
                    }
                    // 发布
                    mBtPublish.setEnabled(false);
                    try {
                        if (mQuestionId.equals(0L)) {
                            if (mWcInvite.isChecked() && (mRewardMoney <= 0 || TextUtils.isEmpty(mBtQaSelectExpert.getRightText()))) {
                                showSnackErrorMessage(getString(R.string.not_invite_expert));
                                mBtPublish.setEnabled(true);
                                return;
                            }
                            packgQuestion();
                            mQAPublishBean.setAmount(mRewardMoney);
                            mPresenter.publishQuestion(mQAPublishBean);
                        } else {
                            // 已发布的资讯 重新设置悬赏金额
                            mPresenter.resetReward(mQuestionId, mRewardMoney);
                        }
                    } catch (Exception e) {
                        mBtPublish.setEnabled(true);
                        e.printStackTrace();
                    }

                });
        RxView.clicks(mTvRewardRule)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mRulePop.show());
    }

    private QAPublishBean packgQuestion() {
        if (mQAPublishBean == null) {
            return null;
        }
        if (!mQAPublishBean.isHasAgainEdite()) {// 发布成功后编辑的设置悬赏要放到最后一步
            mQAPublishBean.setAmount(mRewardMoney);
        }
        mQAPublishBean.setAutomaticity(mWcInvite.isChecked() ? 1 : 0);
        mQAPublishBean.setLook(mWcOnlooker.isChecked() ? 1 : 0);
        return mQAPublishBean;
    }

    private void resetValue() {
        mWcInvite.setChecked(false);
        resetExpert();
        resetRewardMoney();
        resetOnLookerMoney();
        resetRewardInput();
    }

    /**
     * 悬赏的数据重置
     */
    private void resetRewardMoney() {
        mRewardMoney = 0;
        mRbDaysGroup.clearCheck();
    }

    /**
     * 围观无论是关闭还是重置 都是直接清除整体
     * 同样 一旦打开就必须设置所有相关数据
     */
    private void resetOnLookerMoney() {
        mOnLookerMoney = 0;
        mEtOnlookerInput.setText("");
        mRbOnlookersDaysGroup.clearCheck();
        mWcOnlooker.setChecked(false);
    }

    /**
     * 悬赏的清空数据，在关闭邀请的时候调用
     * 此时 悬赏的钱不置为0 关闭按钮后，清除邀请人姓名
     */
    private void resetExpert() {
        mBtQaSelectExpert.setRightText("");
    }

    /**
     * 重置悬赏金额输入框，在选择默认三个时调用
     */
    private void resetRewardInput() {
        mEtInput.setText("");
    }

    /**
     * 重置围观金额，选择了默认三个时调用
     */
    private void resetOnlookerInput() {
        mEtOnlookerInput.setText("");
    }

    /**
     * 悬赏逻辑
     * 1、一旦开了邀请某人 那么必须设置金额和邀请人 否则不能发布
     * 2、一旦开启了围观 那么必须设置围观金额 否则不能发布
     * 3、什么都不设置 可以发布
     */
    private void configSureButton() {
        boolean isEnable = true;
        // 开了邀请但是居然不设置金额 想空手套白狼吗？？？ 不过这个邀请专家 就只能专家得钱了 万一专家并不想搭理你呢
        if (mWcInvite.isChecked() && (mRewardMoney <= 0 || TextUtils.isEmpty(mBtQaSelectExpert.getRightText()))) {
            isEnable = false;
        }
        // 围观都要收钱,但是收多少已经不是我能管的了QwQ.
        if (mWcOnlooker.isChecked() && mOnLookerMoney <= 0) {
            //isEnable = false;
        }
        mBtPublish.setEnabled(isEnable);
    }

    private void checkData() {

    }

    @Override
    public void onClicked() {
        mRulePop.dismiss();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void resetRewardSuccess() {
        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_QUESTION_ID, mRewardMoney);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void publishQuestionSuccess(QAListInfoBean qaListInfoBean) {
        this.mQaListInfoBean = qaListInfoBean;
    }

    private void goToQuestionDetail() {
        if (mQaListInfoBean != null) {
            EventBus.getDefault().post(new Bundle(), EventBusTagConfig.EVENT_PUBLISH_QUESTION);
            PublishQuestionFragment.gDraftQuestion = null;
            Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_QUESTION_BEAN, mQaListInfoBean);
            intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
            startActivity(intent);
        }
    }

    private void initInstructionsPop(int resDesStr) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .desStr(getString(resDesStr))
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.instructions))
                .desStr(getString(resDesStr))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mRulePop);
        dismissPop(mInstructionsPopupWindow);
    }
}
