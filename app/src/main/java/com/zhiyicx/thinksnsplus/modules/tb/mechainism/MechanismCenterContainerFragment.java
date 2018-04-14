package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.detail.MerchainMessageListContract;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.TBDynamicFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.TBMainDynamicFragment;
import com.zhiyicx.thinksnsplus.modules.tb.info.TBMerchianMainInfoListFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscription;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterContainerFragment extends TSViewPagerFragment implements DynamicFragment.OnCommentClickListener,
        MechanismCenterFragment.OnMerchainismInfoChangedListener {

    @BindView(R.id.mechainism_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.iv_head)
    UserAvatarView mIvHeadIcon;
    @BindView(R.id.tv_name)
    TextView mTvUserName;
    @BindView(R.id.tv_dec)
    TextView mTvDes;
    @BindView(R.id.ll_follow_container)
    View mLlFollowContainer;

    private UserInfoBean mUserInfoBean;

    protected ActionPopupWindow mMorePop;


    @Inject
    UserInfoRepository mUserInfoRepository;
    private Subscription mUserinfoSub;


    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
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
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(mActivity.getResources().getStringArray(R.array
                .integration_mechanism_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(MechanismCenterFragment.newInstance(getArguments()));
            /*mFragmentList.add(TBMainDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this, getArguments().getParcelable
                    (PersonalCenterFragment.PERSONAL_CENTER_DATA)));*/
            mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this, getArguments().getParcelable
                    (PersonalCenterFragment.PERSONAL_CENTER_DATA)));
            mFragmentList.add(TBMerchianMainInfoListFragment.newInstance(mUserInfoBean.getUser_id()));
        }
        return mFragmentList;
    }

    @Override
    public void onButtonMenuShow(boolean isShow) {

    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        mTsvToolbar.setLeftImg(0);
        initListener();
    }

    private void getUserinfo() {
        mUserinfoSub = mUserInfoRepository.getSpecifiedUserInfo(mUserInfoBean.getUser_id(), null, null)
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBean = data;
                        updateUseFollow();
                    }
                });
    }

    private void initListener() {
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float statusBarHeight = (float) DeviceUtils.getStatuBarHeight(mActivity);
            float point = (float) Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();
            float statusPer = statusBarHeight / (float) appBarLayout.getTotalScrollRange();
            float paddingTop = (float) appBarLayout.getTotalScrollRange() * (statusPer + point - 1);
            if (mVpFragment instanceof NoPullViewPager) {
                NoPullViewPager viewPager = (NoPullViewPager) mVpFragment;
                viewPager.setCanScroll(point == 1);
            }
            LogUtils.d(point);
            if (paddingTop < 0) {
                return;
            }
            mTsvToolbar.setPadding(0, (int) paddingTop, 0, 0);

        });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mechanismcenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInfoBean = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
    }

    @Override
    protected void initData() {
        ImageUtils.loadCircleUserHeadPic(mUserInfoBean, mIvHeadIcon);
        // 设置用户名
        mTvUserName.setText(mUserInfoBean.getName());
        updateUserDes(mUserInfoBean.getIntro());
        getUserinfo();

    }

    /**
     * 更新机构用户描述
     *
     * @param des
     */
    private void updateUserDes(String des) {
        mTvDes.setText(des);
    }

    private void updateUseFollow() {
//        mLlFollowContainer.setVisibility(mUserInfoBean.getFollower() ? View.GONE : View.VISIBLE);
        try {
            ((MechanismCenterFragment) mFragmentList.get(0)).updateFollowStat(mUserInfoBean.getFollower());

        } catch (Exception e) {
        }
    }

    public static MechanismCenterContainerFragment newInstance(Bundle bundle) {
        MechanismCenterContainerFragment mechanismCenterContainerFragment = new MechanismCenterContainerFragment();
        mechanismCenterContainerFragment.setArguments(bundle);
        return mechanismCenterContainerFragment;
    }


    @OnClick({R.id.iv_back, R.id.iv_more, R.id.tv_attention})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_more:
                initMorePop();
                break;
            case R.id.tv_attention:
                handleUser();
                break;
            default:
        }
    }

    private void handleUser() {
        mUserInfoRepository.handleFollow(mUserInfoBean);
        updateUseFollow();
    }

    protected void initMorePop() {
        mMorePop = ActionPopupWindow.builder()
                .item1Str(mUserInfoBean.getFollower() ? getString(R.string.cancel_follow) : "")
                .item2Str(AppApplication.getMyUserIdWithdefault() == mUserInfoBean.getUser_id() ? "" : getString(R.string.report))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 取消关注
                    mUserInfoRepository.handleFollow(mUserInfoBean);
                    mUserInfoBean.setFollower(false);
                    mMorePop.hide();
                    updateUseFollow();
                })  // 关注
                .item2ClickListener(() -> {                    // 举报帖子
                    ReportResourceBean reportResourceBean = new ReportResourceBean(mUserInfoBean, String.valueOf(mUserInfoBean
                            .getUser_id()),
                            mUserInfoBean.getName(), mUserInfoBean.getAvatar(), mTvDes.getText().toString(), ReportType.USER);
                    ReportActivity.startReportActivity(mActivity, reportResourceBean);
                    mMorePop.hide();
                })
                .bottomClickListener(() -> {
                    mMorePop.hide();
                })
                .build();
        mMorePop.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mMorePop);
        if (mUserinfoSub != null && !mUserinfoSub.isUnsubscribed()) {
            mUserinfoSub.unsubscribe();
        }
    }

    @Override
    public void onMerchainismInfoChanged(MerchainInfo merchainInfo) {
        if (merchainInfo != null && !TextUtils.isEmpty(merchainInfo.getRemarks())) {
            updateUserDes(merchainInfo.getRemarks());
        }

    }

    @Override
    public void handleFollow() {
        handleUser();
    }
}
