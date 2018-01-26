package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.circle.mine.container.MyCircleContainerActivity;
import com.zhiyicx.thinksnsplus.modules.collect.CollectListActivity;
import com.zhiyicx.thinksnsplus.modules.draftbox.DraftBoxActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.mycode.MyCodeActivity;
import com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.MyMusicActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CertificationTypePopupWindow;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;
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
    @BindView(R.id.tv_user_signature)
    TextView mTvUserSignature;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.bt_wallet)
    CombinationButton mBtWallet;
    @BindView(R.id.bt_mine_integration)
    CombinationButton btMineIntegration;
    @BindView(R.id.bt_certification)
    CombinationButton mBtCertification;
    @BindView(R.id.bv_fans_new_count)
    BadgeView mVvFansNewCount;

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
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
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

    @OnClick({R.id.rl_userinfo_container, R.id.ll_fans_container, R.id.ll_follow_container, R.id.bt_my_info,
            R.id.bt_personal_page, R.id.bt_collect, R.id.bt_wallet, R.id.bt_mine_integration, R.id.bt_music,
            R.id.bt_suggestion, R.id.bt_draft_box, R.id.bt_setting, R.id.bt_certification, R.id.bt_my_qa, R.id.bt_my_group,
            R.id.bt_my_friends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo_container:
                startActivity(new Intent(mActivity, UserInfoActivity.class));
                break;
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
            case R.id.bt_personal_page:
                PersonalCenterFragment.startToPersonalCenter(mActivity, mUserInfoBean);
                break;
            /*
             * 我的投稿
             */
            case R.id.bt_my_info:
                startActivity(new Intent(mActivity, ManuscriptsActivity.class));
                break;
            /*
              我的收藏
             */
            case R.id.bt_collect:
                startActivity(new Intent(mActivity, CollectListActivity.class));
                break;
            /*
              我的钱包
             */
            case R.id.bt_wallet:
                startActivity(new Intent(mActivity, WalletActivity.class));
                break;
            /*
              我的积分 
             */
            case R.id.bt_mine_integration:
                startActivity(new Intent(mActivity, MineIntegrationActivity.class));
                break;
            /*
              我的音乐
             */
            case R.id.bt_music:
                startActivity(new Intent(mActivity, MyMusicActivity.class));
                break;
            case R.id.bt_suggestion:
                startActivity(new Intent(mActivity, FeedBackActivity.class));
                break;
             /*
              草稿箱
              */
            case R.id.bt_draft_box:
                startActivity(new Intent(mActivity, DraftBoxActivity.class));
                break;
            case R.id.bt_setting:
                startActivity(new Intent(mActivity, SettingsActivity.class));
                break;
            case R.id.bt_certification:
                // 弹窗选择个人或者机构，被驳回也只能重新申请哦 (*^__^*)
                if (mUserCertificationInfo != null
                        && mUserCertificationInfo.getId() != 0
                        && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                    Intent intentToDetail = new Intent(mActivity, CertificationDetailActivity.class);
                    Bundle bundleData = new Bundle();
                    if (mUserCertificationInfo.getCertification_name().equals(SendCertificationBean.USER)) {
                        // 跳转个人认证
                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 0);
                    } else {
                        // 跳转企业认证
                        bundleData.putInt(BUNDLE_DETAIL_TYPE, 1);
                    }
                    bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                    intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                    startActivity(intentToDetail);
                } else {
                    initCertificationTypePop();
                }
                break;
            case R.id.bt_my_qa:
                // 我的问答
                startActivity(new Intent(mActivity, MyQuestionActivity.class));
                break;
            case R.id.bt_my_group:
                // 我的圈子
                startActivity(new Intent(mActivity, MyCircleContainerActivity.class));
                break;
            case R.id.bt_my_friends:
                // 我的朋友
                startActivity(new Intent(mActivity, MyFriendsListActivity.class));
                break;
            default:
        }
    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        btMineIntegration.setLeftText(getString(R.string.my_integration_name,mPresenter.getGoldName()));
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
        // 设置简介
        mTvUserSignature.setText(TextUtils.isEmpty(userInfoBean.getIntro()) ? getString(R.string.intro_default) : userInfoBean.getIntro());
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
        mBtWallet.setRightText(getString(R.string.money_format_with_unit, PayConfig.realCurrency2GameCurrency(myMoney, mPresenter.getRatio())
                , ""));
        btMineIntegration.setRightText(String.valueOf(userInfoBean.getFormatCurrencyNum()));
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
            if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_success));
            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REVIEWING.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_ing));
            } else if (data.getStatus() == UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                mBtCertification.setRightText(getString(R.string.certification_state_failed));
            }
        } else {
            mBtCertification.setRightText("");
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
