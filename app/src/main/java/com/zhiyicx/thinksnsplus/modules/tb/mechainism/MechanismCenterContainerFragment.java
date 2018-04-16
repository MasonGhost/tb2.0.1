package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.NoPullViewPager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment;
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
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.CAN_COMMENT;

/**
 * @Author Jliuer
 * @Date 2018/03/01/9:42
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MechanismCenterContainerFragment extends TSViewPagerFragment implements
        MechanismCenterFragment.OnMerchainismInfoChangedListener,
        TBMainDynamicFragment.OnItemCommentClickListener,
        InputLimitView.OnSendClickListener{

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
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;

    private UserInfoBean mUserInfoBean;

    protected ActionPopupWindow mMorePop;

    private MechanismCenterFragment mMechanismCenterFragment;
    private TBMainDynamicFragment mTBMainDynamicFragment;
    private TBMerchianMainInfoListFragment mTBMerchianMainInfoListFragment;
    private DynamicDetailBeanV2 mDynamicDetailBeanV2;


    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    //private Subscription mUserinfoSub;


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
            mMechanismCenterFragment = MechanismCenterFragment.newInstance(getArguments());
            mTBMainDynamicFragment = TBMainDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, (UserInfoBean) getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA));
            mTBMainDynamicFragment.setOnItemCommentClickListener(this);
            mTBMerchianMainInfoListFragment = TBMerchianMainInfoListFragment.newInstance(mUserInfoBean.getUser_id());
            mFragmentList.add(mMechanismCenterFragment);
            mFragmentList.add(mTBMainDynamicFragment);
            /*mFragmentList.add(TBDynamicFragment.newInstance(ApiConfig.DYNAMIC_TYPE_NEW, this, getArguments().getParcelable
                    (PersonalCenterFragment.PERSONAL_CENTER_DATA)));*/
            mFragmentList.add(mTBMerchianMainInfoListFragment);
        }
        return mFragmentList;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        initInputView();
        mTsvToolbar.setLeftImg(0);
        initListener();
    }

    /*private void getUserinfo() {
        mUserinfoSub = mUserInfoRepository.getSpecifiedUserInfo(mUserInfoBean.getUser_id(), null, null)
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBean = data;
                        updateUseFollow();
                    }
                });
    }*/

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
        //getUserinfo();

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
            mMechanismCenterFragment.updateFollowStat(mUserInfoBean.getFollower());
            mTBMainDynamicFragment.updateFollower(mUserInfoBean.getFollower());
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
                handleFollow();
                break;
            default:
        }
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
                    handleFollow();
                    mMorePop.hide();
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
        /*if (mUserinfoSub != null && !mUserinfoSub.isUnsubscribed()) {
            mUserinfoSub.unsubscribe();
        }*/
    }

    @Override
    public void onMerchainismInfoChanged(MerchainInfo merchainInfo) {
        if (merchainInfo != null && !TextUtils.isEmpty(merchainInfo.getRemarks())) {
            updateUserDes(merchainInfo.getRemarks());
        }

    }

    public void showCommentView() {
        showBottomView(false);

    }

    protected void showBottomView(boolean isShow) {
        if (isShow) {
            mVShadow.setVisibility(View.GONE);
            mIlvComment.setVisibility(View.GONE);
            mIlvComment.clearFocus();
            mIlvComment.setSendButtonVisiable(false);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        } else {
            mVShadow.setVisibility(View.VISIBLE);
            mIlvComment.setVisibility(View.VISIBLE);
            mIlvComment.getFocus();
            mIlvComment.setSendButtonVisiable(true);
            DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
    }

    @Override
    public void handleFollow() {
        mUserInfoRepository.handleFollow(mUserInfoBean);
        updateUseFollow();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onItemCommentClick(int pos, DynamicDetailBeanV2 detailBeanV2, String dynamicType) {
        mDynamicDetailBeanV2 = detailBeanV2;
        if (CAN_COMMENT == detailBeanV2.getCan_comment()) {
            if(detailBeanV2.getComments().isEmpty()){
                // 直接评论
                showCommentView();
                mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
            } else {
                // 还未发送成功的动态列表不查看详情
                if (detailBeanV2.getId() == null || detailBeanV2.getId() == 0) {
                    return;
                }
                Intent commentListIntent = new Intent(getActivity(), DynamicDetailActivity.class);
                Bundle commentListBundle = new Bundle();
                commentListBundle.putParcelable(DynamicDetailFragment.DYNAMIC_DETAIL_DATA, detailBeanV2);
                commentListBundle.putString(DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE, dynamicType);
                commentListIntent.putExtras(commentListBundle);
                startActivity(commentListIntent);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.keep_on);
            }
        } else {
            showSnackWarningMessage(getString(R.string.dynamic_not_support_comment));
        }
    }

    private void initInputView() {
        mVShadow.setOnClickListener(v -> closeInputView());
        mIlvComment.setOnSendClickListener(this);
    }

    public void closeInputView() {
        if (mIlvComment.getVisibility() == View.VISIBLE) {
            mIlvComment.setVisibility(View.GONE);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
        mVShadow.setVisibility(View.GONE);
    }

    /**
     * comment send
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, final String text) {
        com.zhiyicx.imsdk.utils.common.DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        sendCommentV2(mDynamicDetailBeanV2, 0, text);
        showBottomView(true);
    }

    public void sendCommentV2(DynamicDetailBeanV2 detailBeanV2, long replyToUserId, String commentContent) {
        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(detailBeanV2.getFeed_mark());
        String comment_mark = AppApplication.getMyUserIdWithdefault() + "" + System
                .currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        //当回复动态的时候
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id(replyToUserId);
        creatComment.setReplyUser(userInfoBean);
        creatComment.setUser_id(AppApplication.getMyUserIdWithdefault());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        detailBeanV2.setFeed_comment_count(detailBeanV2.getFeed_comment_count() + 1);
        detailBeanV2.getComments().add(creatComment);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(detailBeanV2);
        mBaseDynamicRepository.sendCommentV2(commentContent, detailBeanV2.getId(),
                replyToUserId, creatComment.getComment_mark());
        mTBMainDynamicFragment.refreshData();
    }
}
