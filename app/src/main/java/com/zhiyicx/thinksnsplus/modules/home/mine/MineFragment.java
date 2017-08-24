package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.collect.CollectListActivity;
import com.zhiyicx.thinksnsplus.modules.draftbox.DraftBoxActivity;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListActivity;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.rank.RankActivity;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsActivity;
import com.zhiyicx.thinksnsplus.modules.system_conversation.SystemConversationActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.CertificationTypePopupWindow;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.R.mipmap.ico_me_message_normal;
import static com.zhiyicx.thinksnsplus.R.mipmap.ico_me_message_remind;
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
    @BindView(R.id.rl_userinfo_container)
    RelativeLayout mRlUserinfoContainer;
    @BindView(R.id.tv_fans_count)
    TextView mTvFansCount;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.ll_follow_container)
    LinearLayout mLlFollowContainer;
    @BindView(R.id.bt_personal_page)
    CombinationButton mBtPersonalPage;
    @BindView(R.id.bt_ranking)
    CombinationButton mBtRanking;
    @BindView(R.id.bt_collect)
    CombinationButton mBtCollect;
    @BindView(R.id.bt_wallet)
    CombinationButton mBtWallet;
    @BindView(R.id.bt_suggestion)
    CombinationButton mBtSuggestion;
    @BindView(R.id.bt_draft_box)
    CombinationButton mDraftBox;
    @BindView(R.id.bt_setting)
    CombinationButton mBtSetting;
    @BindView(R.id.bt_certification)
    CombinationButton mBtCertification; // 认证

    @BindView(R.id.bv_fans_new_count)
    BadgeView mVvFansNewCount;

    private CertificationTypePopupWindow mCertificationWindow; // 选择认证人类的弹窗

    @Inject
    public MinePresenter mMinePresenter;

    private UserInfoBean mUserInfoBean;
    private UserCertificationInfo mUserCertificationInfo;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mBtRanking.setVisibility(View.GONE);// V2 点赞排行榜还没有,暂时隐藏
    }

    @Override
    protected void initData() {
        DaggerMinePresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .minePresenterModule(new MinePresenterModule(this))
                .build().inject(this);
        mPresenter.getCertificationInfo();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mPresenter != null) {
            mPresenter.updateUserInfo();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getUserInfoFromDB();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.mine);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

//    @Override
//    protected int setRightImg() {
//        return ico_me_message_normal;
//    }

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
        startActivity(new Intent(getActivity(), SystemConversationActivity.class));
        mPresenter.readMessageByKey(ApiConfig.NOTIFICATION_KEY_NOTICES);
    }

    @OnClick({R.id.rl_userinfo_container, R.id.ll_fans_container, R.id.ll_follow_container,
            R.id.bt_personal_page, R.id.bt_ranking, R.id.bt_collect, R.id.bt_wallet,
            R.id.bt_suggestion, R.id.bt_draft_box, R.id.bt_setting, R.id.bt_certification})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo_container:
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.ll_fans_container:
                long fansUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFans = new Bundle();
                bundleFans.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FANS_FRAGMENT_PAGE);
                bundleFans.putLong(FollowFansListFragment.PAGE_DATA, fansUserId);
                Intent itFans = new Intent(getActivity(), FollowFansListActivity.class);
                itFans.putExtras(bundleFans);
                startActivity(itFans);
                break;
            case R.id.ll_follow_container:
                long followUserId = AppApplication.getmCurrentLoginAuth().getUser_id();
                Bundle bundleFollow = new Bundle();
                bundleFollow.putInt(FollowFansListFragment.PAGE_TYPE, FollowFansListFragment.FOLLOW_FRAGMENT_PAGE);
                bundleFollow.putLong(FollowFansListFragment.PAGE_DATA, followUserId);
                Intent itFollow = new Intent(getActivity(), FollowFansListActivity.class);
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.bt_personal_page:
                Intent intent = new Intent(getContext(), PersonalCenterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, mUserInfoBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.bt_ranking:

                Intent toRank = new Intent(getContext(), RankActivity.class);
                startActivity(toRank);

                break;
            case R.id.bt_collect:
                startActivity(new Intent(getActivity(), CollectListActivity.class));
                break;
            case R.id.bt_wallet:
                startActivity(new Intent(getActivity(), WalletActivity.class));
                break;
            case R.id.bt_suggestion:
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
                //LoadingDialogUtils.showStateSuccess(getContext());
                break;
            case R.id.bt_draft_box:
                startActivity(new Intent(getActivity(), DraftBoxActivity.class));
                break;
            case R.id.bt_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.bt_certification:
                // 弹窗选择个人或者机构
                if (mUserCertificationInfo != null && mUserCertificationInfo.getId() != 0) {
                    Intent intentToDetail = new Intent(getActivity(), CertificationDetailActivity.class);
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
            default:
        }
    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        if (userInfoBean == null) {
            return;
        }
        this.mUserInfoBean = userInfoBean;
        // 设置用户头像
        ImageUtils.loadCircleUserHeadPic(mUserInfoBean, mIvHeadIcon);
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
        mBtWallet.setRightText(getString(R.string.money_format_with_unit, PayConfig.realCurrencyFen2Yuan(myMoney)));
    }

    @Override
    public void setNewFollowTip(int count) {
        mVvFansNewCount.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(count)));
    }

    @Override
    public void setNewSystemInfo(boolean isShow) {
        setToolBarRightImage(isShow ? ico_me_message_remind : ico_me_message_normal);
    }

    @Override
    public void updateCertification(UserCertificationInfo data) {
        if (data != null && data.getId() != 0) {
            mUserCertificationInfo = data;
            if (data.getStatus() == 1) {
                mBtCertification.setRightText(getString(R.string.certification_state_success));
            } else if (data.getStatus() == 0) {
                mBtCertification.setRightText(getString(R.string.certification_state_ing));
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
                    .with(getActivity())
                    .alpha(0.8f)
                    .setListener(this)
                    .build();
        }
        mCertificationWindow.show();
    }

    @Override
    public void onTypeSelected(int position) {
        mCertificationWindow.dismiss();
        Intent intent = new Intent(getActivity(), CertificationInputActivity.class);
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
