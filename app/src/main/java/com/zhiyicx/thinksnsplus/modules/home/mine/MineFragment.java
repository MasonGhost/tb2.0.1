package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wcy.overscroll.OverScrollLayout;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.mycode.MyCodeActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CertificationTypePopupWindow;
import com.zhiyicx.thinksnsplus.widget.MineTaskItemView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.schedulers.Schedulers;

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

    @BindView(R.id.overscroll)
    OverScrollLayout mOverScrollLayout;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.fl_toolbar_contaier)
    FrameLayout mFlToolbarContaier;
    /**
     * 选择认证人类型的弹窗
     */
    private CertificationTypePopupWindow mCertificationWindow;

    @Inject
    public MinePresenter mMinePresenter;

    private UserInfoBean mUserInfoBean;

    private UserCertificationInfo mUserCertificationInfo;

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.create(subscriber -> {
            DaggerMinePresenterComponent.builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .minePresenterModule(new MinePresenterModule(MineFragment.this))
                    .build().inject(MineFragment.this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .subscribe(o -> {
                }, Throwable::printStackTrace);
    }

    @Override
    protected void initView(View rootView) {
        mOverScrollLayout.setTopOverScrollEnable(false);
        mFlToolbarContaier.setPadding(0, DeviceUtils.getStatuBarHeight(mActivity.getApplicationContext()), 0, 0);
        mScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                LogUtils.d("layout", left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom);
            }
        });
        setMineTaskViewData(mMtiInviteFriends, "100", true, getString(R.string.immedite_invitation), getColor(R.color.white)
                , getString(R.string.invite_friend_str), getString(R.string.invite_friend_str_fomart, 100, "TBMark"), false, R.drawable
                        .selector_button_corner_circle_solid_small_gradient);
        setMineTaskViewData(mMtiEditInviteCode, "8", true, getString(R.string.go_edit_invite_code), getColor(R.color.white)
                , getString(R.string.edit_invite_code), getString(R.string.edit_invite_code_fomart, 8, "TBMark"), false, R.drawable
                        .selector_button_corner_circle_solid_small_gradient);

        setMineTaskViewData(mMtiShareDynamic, "5", true, "1/4", getColor(R.color.themeColor)
                , getString(R.string.share_dynamic), getString(R.string.share_dynamic_fomart, 5, "TBMark"), true, 0);
        setMineTaskViewData(mMtiCertify, "50", true, getString(R.string.immediate_certify), getColor(R.color.white)
                , getString(R.string.certification), getString(R.string.certification_format, 50, "TBMark"), false, R.drawable
                        .selector_button_corner_circle_solid_small_gradient);
    }

    private void setMineTaskViewData(MineTaskItemView mineTaskViewData, String point, boolean isAdd, String buttonText, int buttonTextColor, String
            title, String des,
                                     boolean progressShow, int buttonBgRes) {
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
            mPresenter.getUserInfoFromDB();
            mPresenter.updateUserInfo();
            mPresenter.getCertificationInfo();
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

    @OnClick({R.id.ll_fans_container, R.id.ll_follow_container, R.id.iv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
                /*
                  粉丝列表
                 */
            case R.id.ll_fans_container:
                long fansUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFans = new Bundle();
                bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
                bundleFans.putLong(FollowFansListFragment.PAGE_DATA, fansUserId);
                Intent itFans = new Intent(mActivity, FollowFansListActivity.class);
                itFans.putExtras(bundleFans);
                startActivity(itFans);
                break;
                /*
                 关注列表
                 */
            case R.id.ll_follow_container:
                long followUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFollow = new Bundle();
                bundleFollow.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
                bundleFollow.putLong(FollowFansListFragment.PAGE_DATA, followUserId);
                Intent itFollow = new Intent(mActivity, FollowFansListActivity.class);
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
//            case R.id.bt_personal_page:
//                PersonalCenterFragment.startToPersonalCenter(mActivity, mUserInfoBean);
//                break;
//
            case R.id.iv_setting:
                startActivity(new Intent(mActivity, SettingsActivity.class));
                break;
//            case R.id.bt_certification:
//                // 弹窗选择个人或者机构，被驳回也只能重新申请哦 (*^__^*)
//                if (mUserCertificationInfo != null
//                        && mUserCertificationInfo.getId() != 0
//                        && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
//                    Intent intentToDetail = new Intent(mActivity, CertificationDetailActivity.class);
//                    Bundle bundleData = new Bundle();
//                    if (mUserCertificationInfo.getCertification_name().equals(SendCertificationBean.USER)) {
//                        // 跳转个人认证
//                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 0);
//                    } else {
//                        // 跳转企业认证
//                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 1);
//                    }
//                    bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
//                    intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
//                    startActivity(intentToDetail);
//                } else {
//                    initCertificationTypePop();
//                }
//                break;
//            case R.id.bt_my_friends:
//                // 我的朋友
//                startActivity(new Intent(mActivity, MyFriendsListActivity.class));
//                break;
            default:
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
        // 设置粉丝数
        String followedCount = String.valueOf(userInfoBean.getExtra().getFollowers_count());
        mTvFansCount.setText(ConvertUtils.numberConvert(Integer.parseInt(followedCount)));
        // 设置关注数
        String followingCount = String.valueOf(userInfoBean.getExtra().getFollowings_count());
        mTvFollowCount.setText(ConvertUtils.numberConvert(Integer.parseInt(followingCount)));
        double myMoney = 0;
        if (userInfoBean.getWallet() != null) {
            myMoney = userInfoBean.getWallet().getBalance();
        }
        this.mUserInfoBean = userInfoBean;
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
//            if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
//                mBtCertification.setRightText(getString(R.string.certification_state_success));
//            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REVIEWING.value) {
//                mBtCertification.setRightText(getString(R.string.certification_state_ing));
//            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
//                mBtCertification.setRightText(getString(R.string.certification_state_failed));
//            }
        } else {
//            mBtCertification.setRightText("");
        }
        if (mCertificationWindow != null) {
            mCertificationWindow.dismiss();
        }
    }

    private void initCertificationTypePop() {
        if (mCertificationWindow == null) {
            mCertificationWindow = CertificationTypePopupWindow.Builder()
                    .with(mActivity)
                    .alpha(0.8f)
                    .setListener(this)
                    .build();
        }
        mCertificationWindow.show();
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

}
