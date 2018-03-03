package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.support.v4.widget.RxNestedScrollView;
import com.wcy.overscroll.OverScrollLayout;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskRewardRuleBean;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.mycode.MyCodeActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionActivity;
import com.zhiyicx.thinksnsplus.modules.tb.invitation.InvitationActivity;
import com.zhiyicx.thinksnsplus.modules.tb.invitation.editcode.EditInviteCodeActivity;
import com.zhiyicx.thinksnsplus.modules.tb.rank.RankListActivity;
import com.zhiyicx.thinksnsplus.modules.tb.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.rule.WalletRuleFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CertificationTypePopupWindow;
import com.zhiyicx.thinksnsplus.widget.MineTaskItemView;
import com.zhiyicx.thinksnsplus.widget.checkin.CheckInView;
import com.zhiyicx.thinksnsplus.widget.popwindow.TBCenterInfoPopWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskBean.TBTASKTRIGGER.CERTIFICATION;
import static com.zhiyicx.thinksnsplus.data.beans.tbtask.TBTaskBean.TBTASKTRIGGER.SHARE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_CERTIFICATION_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;

/**
 * @Describe 我的页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MineFragment extends TSFragment<MineContract.Presenter> implements MineContract.View,
        CertificationTypePopupWindow.OnTypeSelectListener {

    @BindView(R.id.iv_head_icon)
    UserAvatarView mIvHeadIcon;
    @BindView(R.id.tv_mine)
    TextView mTvMine;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.tv_friends_count)
    TextView mTvFriendsCount;
    @BindView(R.id.bv_fans_new_count)
    BadgeView mVvFansNewCount;
    @BindView(R.id.tv_task_tip)
    TextView mTvTaskTip;
    @BindView(R.id.tv_reward_des)
    TextView mTvRewardDes;
    @BindView(R.id.tv_every_day_check_in_tip)
    TextView mTvEveryDayCheckInTip;
    @BindView(R.id.tv_continiuous_check_in_tip)
    TextView mTvContiniuousCheckInTip;
    @BindView(R.id.mti_invite_friends)
    MineTaskItemView mMtiInviteFriends;
    @BindView(R.id.mti_edit_invite_code)
    MineTaskItemView mMtiEditInviteCode;
    @BindView(R.id.mti_share_dynamic)
    MineTaskItemView mMtiShareDynamic;
    @BindView(R.id.mti_certify)
    MineTaskItemView mMtiCertify;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.tv_check_in)
    TextView mTvCheck_in;

    @BindView(R.id.overscroll)
    OverScrollLayout mOverScrollLayout;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.fl_toolbar_contaier)
    FrameLayout mFlToolbarContaier;
    @BindView(R.id.fl_title)
    FrameLayout mFrameLayout;
    @BindView(R.id.rl_userinfo_container)
    RelativeLayout mLayout;
    @BindView(R.id.checkin_view)
    CheckInView mCheckInView;

    /**
     * 签到提示弹框
     */
    private TBCenterInfoPopWindow mCheckTipPop;

    /**
     * 选择认证人类型的弹窗
     */
    private CertificationTypePopupWindow mCertificationWindow;
    private int mStatusHeight;

    private List<String> signInData = new ArrayList<>();


    @Inject
    public MinePresenter mMinePresenter;

    private UserInfoBean mUserInfoBean;
    private TBTaskContainerBean mTBTaskContainerBean;

    private UserCertificationInfo mUserCertificationInfo;
    private float mCurrentAlpha = 0;

    TBCenterInfoPopWindow mTBCenterInfoPopWindow;

    /**
     * 签到数据
     */
    private CheckInBean mCheckInBean;
    private TBTaskRewardRuleBean mTBTaskRewardRuleBean;
    private Subscription mTimeerSub;


    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Observable.create(subscriber -> {
            DaggerMinePresenterComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .minePresenterModule(new MinePresenterModule(MineFragment.this))
                    .build().inject(MineFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        onPresenterInjected();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

        initCheckViewData();

    }

    private void initCheckViewData() {
        signInData.add("第一天");
        signInData.add("第二天");
        signInData.add("第三天");
        signInData.add("第四天");
        signInData.add("第五天");
        signInData.add("第六天");
        signInData.add("第七天");
        mCheckInView.setSignInData(signInData);
    }

    @Override
    protected void initView(View rootView) {
        mStatusHeight = DeviceUtils.getStatuBarHeight(mActivity.getApplicationContext());
        mOverScrollLayout.setTopOverScrollEnable(false);
        mFlToolbarContaier.setPadding(0, mStatusHeight, 0, 0);
        mFrameLayout.setPadding(0, mStatusHeight + getResources().getDimensionPixelOffset(R
                .dimen.toolbar_height), 0, 0);
        RxNestedScrollView.scrollChangeEvents(mScrollView)
                .compose(this.bindToLifecycle())
                .subscribe(viewScrollChangeEvent ->
                {
                    float alpha = ((float) (viewScrollChangeEvent.scrollY()) / (mFlToolbarContaier.getHeight() + mStatusHeight));
                    if (mCurrentAlpha > 1 && alpha > 1) {
                        mCurrentAlpha = 1;
                    } else {
                        mCurrentAlpha = alpha;
                    }
                    mFrameLayout.setAlpha(mCurrentAlpha);
                    mTvMine.setAlpha(mCurrentAlpha);
                    mIvSetting.setImageResource(mCurrentAlpha > 0.5 ? R.mipmap.ico_setting_black : R.mipmap.ico_setting);
                });

    }

    private void onPresenterInjected() {
        if (mTBTaskContainerBean == null && mPresenter != null) {
            mPresenter.getTaskInfo();
        }
    }

    private void setMineTaskViewData(MineTaskItemView mineTaskViewData, String point, boolean isAdd, String buttonText, int buttonTextColor, String
            title, String des,
                                     boolean progressShow, int buttonBgRes) {
        mineTaskViewData.setVisibility(View.VISIBLE);
        mineTaskViewData.setPoint(point);
        mineTaskViewData.setPointSymbol(isAdd);
        mineTaskViewData.setButtonText(buttonText);
        mineTaskViewData.setTitle(title);
        mineTaskViewData.setDes(des);
        mineTaskViewData.setProgressVisiable(progressShow);
        mineTaskViewData.setTvButtonTextColor(buttonTextColor);
        mineTaskViewData.setTvButtonBackground(buttonBgRes);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        reLoadUserInfo(isVisibleToUser);
    }

    private void reLoadUserInfo(boolean isVisibleToUser) {
        if (isVisibleToUser && mPresenter != null) {
            if (mCheckInBean == null) {
                mPresenter.getCheckInfo();
            }
            mPresenter.getUserInfoFromDB();
            mPresenter.updateUserInfo();
//            mPresenter.getCertificationInfo();
            mPresenter.getTaskInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reLoadUserInfo(getUserVisibleHint());
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.mine);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_code;
    }

    @Override
    protected String setRightTitle() {
        return "";
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(mActivity, MyCodeActivity.class));
        if (mPresenter != null) {
            mPresenter.readMessageByKey(NotificationConfig.NOTIFICATION_KEY_NOTICES);
        }
    }

    @Override
    public void getTaskRewardRuleSuccess(TBTaskRewardRuleBean data) {
        this.mTBTaskRewardRuleBean = data;
        goRuleDetail();
    }

    private void goRuleDetail() {
        Intent intent = new Intent(getActivity(), WalletRuleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(WalletRuleFragment.BUNDLE_RULE, mTBTaskRewardRuleBean.getExplain());
        bundle.putString(WalletRuleFragment.BUNDLE_TITLE, getString(R.string.reward_des));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.ll_fans_container, R.id.ll_follow_container, R.id.ll_friends, R.id.iv_setting, R.id.tv_check_in, R.id.v_userinfo, R.id
            .tv_reward_des})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_reward_des:
                if (mTBTaskRewardRuleBean == null) {
                    mPresenter.getTaskRewardRule();
                } else {
                    goRuleDetail();
                }
                break;
               /*
             个人资料
             */
            case R.id.v_userinfo:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            /*
             签到
             */
            case R.id.tv_check_in:
                mPresenter.checkIn();
                break;
                /*
                  排行榜
                 */
            case R.id.ll_fans_container:
                startActivity(new Intent(mActivity, RankListActivity.class));
                break;
                /*
                 钱包
                 */
            case R.id.ll_follow_container:
                startActivity(new Intent(mActivity, WalletActivity.class));

                break;
                /*
                好友
                 */
            case R.id.ll_friends:
                startActivity(new Intent(mActivity, ContributionActivity.class));

                break;
            case R.id.iv_setting:
                startActivity(new Intent(mActivity, SettingsActivity.class));
                break;

            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            mUserInfoBean.setRank_status(data.getIntExtra("rankStatus", 0));
        }
    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return;
        }
        if (mUserInfoBean == null) {
            // 设置用户头像
            ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvHeadIcon);
        } else {
            boolean imageAvatarIsChanged = userInfoBean.getAvatar() != null && (mUserInfoBean.getAvatar() == null || !userInfoBean.getAvatar()
                    .equals(mUserInfoBean.getAvatar()));
            boolean verifiedIsChanged = userInfoBean.getVerified() != null && userInfoBean.getVerified().getType() != null && (mUserInfoBean
                    .getVerified() == null ||
                    !userInfoBean.getVerified().getType().equals(mUserInfoBean
                            .getVerified()
                            .getType()));
            if (imageAvatarIsChanged || verifiedIsChanged) {
                // 设置用户头像
                ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvHeadIcon);
            }
        }
        // 设置用户名
        mTvUserName.setText(userInfoBean.getName());
        // 排行榜
        String followedCount = String.valueOf(userInfoBean.getUser_rank());
        mTvFansCount.setText(ConvertUtils.numberConvert(Integer.parseInt(followedCount)));
        // 钱包
        mTvFollowCount.setText(ConvertUtils.numberConvert(userInfoBean.getWallet() != null ? (int) userInfoBean.getWallet().getBalance() : 0));
        mTvFriendsCount.setText(String.valueOf(userInfoBean.getFriend_count()));
        this.mUserInfoBean = userInfoBean;
    }

    @Override
    public void getTaskInfoSuccess(TBTaskContainerBean data) {
        this.mTBTaskContainerBean = data;
        updateTaskInfo();
    }

    private void updateTaskInfo() {
        if (mTBTaskContainerBean.getTasks() != null) {
            setMineTaskViewData(mMtiInviteFriends, String.valueOf(mTBTaskContainerBean.getExtras().getInvite_friend().getReward()), true, getString
                            (R.string.immedite_invitation),
                    getColor(R.color.white)
                    , mTBTaskContainerBean.getExtras().getInvite_friend().getTitle(), mTBTaskContainerBean.getExtras().getInvite_friend().getDesc(),
                    false,
                    R.drawable
                            .selector_button_corner_circle_solid_small_gradient);
            mMtiInviteFriends.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mActivity, InvitationActivity.class));
                }
            });

            setMineTaskViewData(mMtiEditInviteCode, String.valueOf(mTBTaskContainerBean.getExtras().getInvite_codeX().getReward()), true, getString
                            (mTBTaskContainerBean.isInvite_code() != null ? R.string.has_invite_code : R.string.go_edit_invite_code),
                    getColor(R.color.white)
                    , mTBTaskContainerBean.getExtras().getInvite_codeX().getTitle(), mTBTaskContainerBean.getExtras().getInvite_codeX().getDesc(),
                    false, R
                            .drawable
                            .selector_button_corner_circle_solid_small_gradient);
            mMtiEditInviteCode.getButton().setEnabled(mTBTaskContainerBean.isInvite_code() == null);

            mMtiEditInviteCode.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mActivity, EditInviteCodeActivity.class));
                }
            });


            for (TBTaskBean tbTaskBean : mTBTaskContainerBean.getTasks()) {
                if (SHARE.value.equals(tbTaskBean.getTrigger())) {
                    setMineTaskViewData(mMtiShareDynamic, String.valueOf(tbTaskBean.getAmount()), true, tbTaskBean.getNotes().size()
                                    + "/" + tbTaskBean.getFrequency(),
                            getColor(R.color.themeColor)
                            , tbTaskBean.getName(), tbTaskBean.getDescription(), true,
                            0);
                    mMtiShareDynamic.setprogress(tbTaskBean.getNotes().size() * 100 / tbTaskBean.getFrequency());
                } else if (CERTIFICATION.value.equals(tbTaskBean.getTrigger())) {


                    setMineTaskViewData(mMtiCertify, String.valueOf(tbTaskBean.getAmount()), true, getString(R.string.immediate_certify), getColor
                                    (R.color.white)
                            , tbTaskBean.getName(), tbTaskBean.getDescription(),
                            false, R
                                    .drawable
                                    .selector_button_corner_circle_solid_small_gradient);
                    /**
                     * 0待审核 1已通过 2已驳回 3未认证
                     */
                    switch (mTBTaskContainerBean.getCertified()) {
                        case 0:
                            mMtiCertify.setButtonText(getString(R.string.review_ing));
                            mMtiCertify.getButton().setEnabled(false);

                            break;
                        case 1:
                            mMtiCertify.setButtonText(getString(R.string.has_certifyed));
                            mMtiCertify.getButton().setEnabled(false);

                            break;

                        case 2:
                            mMtiCertify.setButtonText(getString(R.string.has_reflected));


                            break;
                        case 3:
                            mMtiCertify.setButtonText(getString(R.string.immediate_certify));


                            break;

                        default:

                    }

                    mMtiCertify.getButton().setOnClickListener(v -> {
                        mTBCenterInfoPopWindow = TBCenterInfoPopWindow.builder()
                                .titleStr("温馨提示")
                                .desStr("程序猿哥哥努力加班中...")
                                .item1Str(getString(R.string.get_it))
                                .item1Color(R.color.white)
                                .isOutsideTouch(true)
                                .isFocus(true)
//                .animationStyle(R.style.style_actionPopupAnimation)
                                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                                .with(mActivity)
                                .buildCenterPopWindowItem1ClickListener(() -> mTBCenterInfoPopWindow.hide())
                                .parentView(v)
                                .build();
                        mTBCenterInfoPopWindow.show();
                    });
                }


            }
        }

    }

    @Override
    public void setNewFollowTip(int count) {
        mVvFansNewCount.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(count)));
    }

    @Override
    public void setNewSystemInfo(boolean isShow) {
//        setToolBarRightImage(isShow ? ico_me_message_remind : ico_me_message_normal);
    }

    @Override
    public void updateCertification(UserCertificationInfo data) {
        if (data != null && data.getId() != 0) {
            mUserCertificationInfo = data;
        } else {
        }
        if (mCertificationWindow != null) {
            mCertificationWindow.dismiss();
        }
    }

    /**
     * 签到成功
     */
    @Override
    public void checkinSucces() {
        if (mCheckInBean != null) {
            mCheckInBean.setChecked_in(true);
            mCheckInBean.setLast_checkin_count(mCheckInBean.getLast_checkin_count() + 1);
            mCheckInBean.setCheckin_count(mCheckInBean.getCheckin_count() + 1);
            if (mUserInfoBean.getWallet() == null) {
                mUserInfoBean.setWallet(new WalletBean());
            }
            mUserInfoBean.getWallet().setBalance(mUserInfoBean.getWallet().getBalance() + mCheckInBean.getCheck_in_reward());
            setUserInfo(mUserInfoBean);
            updateCheckInInfo();
            showCheckPop();
        }

    }

    private void showCheckPop() {
        mCheckTipPop = TBCenterInfoPopWindow.builder()
                .isOutsideTouch(true)
                .imageResId(R.mipmap.pic_coins)
                .titleColor(R.color.reward_conins_color)
                .titleStr("+" + mCheckInBean.getCheck_in_reward())
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(mActivity)
                .parentView(getContentView())
                .build();
        mCheckTipPop.show();
         mTimeerSub = Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        if (getActivity() != null && mCheckTipPop != null && mCheckTipPop.isShowing()) {
                            mCheckTipPop.hide();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }
                });
    }

    /**
     * 获取签到信息成功回调
     *
     * @param data
     */
    @Override
    public void getCheckInInfoSuccess(CheckInBean data) {
        mCheckInBean = data;
        updateCheckInInfo();
    }

    /**
     * 更新签到信息
     */

    private void updateCheckInInfo() {
        mTvCheck_in.setText(getString(mCheckInBean.isChecked_in() ? R.string.checked : R.string.check_in));
        mTvCheck_in.setEnabled(!mCheckInBean.isChecked_in());
        mTvContiniuousCheckInTip.setText(ColorPhrase.from(getString(R.string.has_continiuous_check_in_format, "<" +
                mCheckInBean.getLast_checkin_count() + ">")).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.checkin_nums_color))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format());
        mCheckInView.setCurretn(mCheckInBean.getLast_checkin_count() % 7);
    }


    @Override
    public void onTypeSelected(int position) {
        mCertificationWindow.dismiss();
        Intent intent = new Intent(mActivity, CertificationInputActivity.class);
        Bundle bundle = new Bundle();
        if (position == 0) {
            // 跳转个人认证
            bundle.putInt(BUNDLE_TYPE, 0);
        } else {
            // 跳转企业认证
            bundle.putInt(BUNDLE_TYPE, 1);
        }
        intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mCertificationWindow);
        dismissPop(mCheckTipPop);
        if(mTimeerSub!=null&&mTimeerSub.isUnsubscribed()){
            mTimeerSub.unsubscribe();
        }
    }
}
