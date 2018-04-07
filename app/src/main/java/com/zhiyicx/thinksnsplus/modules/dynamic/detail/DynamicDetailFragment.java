package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.InputLimitView.OnSendClickListener;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.DynamicCommentEmptyItem;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_2;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_3;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailFragment extends TSListFragment<DynamicDetailContract.Presenter, DynamicCommentBean>
        implements DynamicDetailContract.View, OnUserInfoClickListener, OnCommentTextClickListener,
        OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, DynamicDetailHeader.OnImageClickLisenter,
        TextViewUtils.OnSpanTextClickListener, DynamicDetailCommentItem.OnCommentResendListener {
    public static final String DYNAMIC_DETAIL_DATA = "dynamic_detail_data";
    public static final String DYNAMIC_LIST_NEED_REFRESH = "dynamic_list_need_refresh";
    public static final String DYNAMIC_UPDATE_TOLL = "dynamic_update_toll";
    public static final String DYNAMIC_DETAIL_DATA_TYPE = "dynamic_detail_data_type";
    public static final String DYNAMIC_DETAIL_DATA_POSITION = "dynamic_detail_data_position";
    public static final String LOOK_COMMENT_MORE = "look_comment_more";
    // 动态详情列表，各个item的位置

    @BindView(R.id.behavior_demo_coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.iv_user_portrait)
    UserAvatarView mIvUserPortrait;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.ll_bottom_menu_container)
    ViewGroup mLLBottomMenuContainer;

    private List<RewardsListBean> mRewardsListBeens = new ArrayList<>();
    private DynamicDetailBeanV2 mDynamicBean;// 上一个页面传进来的数据
    private boolean mIsLookMore = false;
    private DynamicDetailHeader mDynamicDetailHeader;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mOtherDynamicPopWindow;
    private ActionPopupWindow mMyDynamicPopWindow;
    private PayPopWindow mPayImagePopWindow;

    private ActionPopupWindow mReSendCommentPopWindow;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTvToolbarRight;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean needCenterLoadingDialog() {
        return true;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        if (mDynamicBean == null) {
            mPresenter.getCurrentDynamicDetail(getArguments().getLong(MessageCommentAdapter
                    .BUNDLE_SOURCE_ID), 0);
        } else {
            mPresenter.getDetailAll(mDynamicBean.getId(), DEFAULT_PAGE_MAX_ID, mDynamicBean
                    .getUser_id() + "", mDynamicBean.getTop());
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        //initToolbarTopBlankHeight();
        initBottomToolUI();
        initBottomToolListener();
        initHeaderView();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mCoordinatorLayout.setEnabled(false);
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> getActivity().finish());
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mPresenter.handleFollowUser(mDynamicBean.getUserInfoBean());
                });
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mIlvComment.setVisibility(View.GONE);
                    mIlvComment.clearFocus();
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                    mLLBottomMenuContainer.setVisibility(View.VISIBLE);
                    mVShadow.setVisibility(View.GONE);

                });
        /*RxView.clicks(mTvToolbarCenter)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mDynamicBean.getUserInfoBean()));*/
        RxView.clicks(mIvUserPortrait)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mDynamicBean.getUserInfoBean()));
        mIlvComment.setOnSendClickListener(this);

    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mDynamicDetailHeader = new DynamicDetailHeader(getContext(), mPresenter.getAdvert());
        mDynamicDetailHeader.setOnImageClickLisenter(this);
        mDynamicDetailHeader.setOnUserInfoClickLisenter(this);
        mDynamicDetailHeader.setOnFollowlistener((data, followView) -> {
            followViewClick(data, followView);
        });
        mHeaderAndFooterWrapper.addHeaderView(mDynamicDetailHeader.getDynamicDetailHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        /*mDynamicDetailHeader.setReWardViewVisible(mSystemConfigBean.getSite().getReward().hasStatus()//设置打赏内容
                && mSystemConfigBean.getFeed().hasReward() ? VISIBLE : GONE);*/
    }

    protected void followViewClick(DynamicDetailBeanV2 data, TextView followView) {
        if (mPresenter.handleTouristControl()) {
            return;
        }
        if (!data.getUserInfoBean().getFollower()) {
            // 关注
            mPresenter.handleFollowUser(data.getUserInfoBean());
            data.getUserInfoBean().setFollower(true);
            refreshData();
        } else {
            // 更多
            initOtherDynamicPopupWindow(data, followView);
            mOtherDynamicPopWindow.show();

        }
    }

    @Override
    protected void initData() {
        // 处理上个页面传过来的动态数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsLookMore = bundle.getBoolean(LOOK_COMMENT_MORE);
            mDynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            if (mDynamicBean == null) {
                mPresenter.getCurrentDynamicDetail(bundle.getLong(MessageCommentAdapter
                        .BUNDLE_SOURCE_ID), 0);
            } else {
                mPresenter.getCurrentDynamicDetail(mDynamicBean.getId(), mDynamicBean.getTop());
            }
        }
    }

    @Override
    public void initDynamicDetial(DynamicDetailBeanV2 dynamicBean) {
        mDynamicBean = dynamicBean;
        if (mPresenter.checkCurrentDynamicIsDeleted(mDynamicBean.getUser_id(), mDynamicBean.getFeed_mark())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
            return;
        }
        if (mDynamicBean.getDigUserInfoList() == null) {
            mPresenter.getDetailAll(mDynamicBean.getId(), DEFAULT_PAGE_MAX_ID, mDynamicBean
                    .getUser_id() + "", mDynamicBean.getTop());
        } else {
            allDataReady();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDynamicBean != null && mPresenter.checkCurrentDynamicIsDeleted(mDynamicBean.getUser_id(), mDynamicBean.getFeed_mark())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
        }
    }

    @Override
    protected MultiItemTypeAdapter<DynamicCommentBean> getAdapter() {
        MultiItemTypeAdapter<DynamicCommentBean> adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        DynamicDetailCommentItem dynamicDetailCommentItem = new DynamicDetailCommentItem();
        dynamicDetailCommentItem.setOnUserInfoClickListener(this);
        dynamicDetailCommentItem.setOnCommentTextClickListener(this);
        dynamicDetailCommentItem.setOnCommentResendListener(this);
        adapter.addItemViewDelegate(dynamicDetailCommentItem);
        DynamicCommentEmptyItem dynamicCommentEmptyItem = new DynamicCommentEmptyItem(R.mipmap.def_information_prompt);
        adapter.addItemViewDelegate(dynamicCommentEmptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onImageClick(int iamgePosition, long amount, int note) {
        initImageCenterPopWindow(iamgePosition, amount, note, R.string.buy_pay_words_desc, true);
    }

    public static DynamicDetailFragment initFragment(Bundle bundle) {
        DynamicDetailFragment dynamicDetailFragment = new DynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

    /**
     * 设置toolBar上面的用户头像,关注状态
     */
    private void setToolBarUser(DynamicDetailBeanV2 dynamicBean) {
        // 设置用户头像，名称
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        UserInfoBean userInfoBean = dynamicBean.getUserInfoBean();// 动态所属用户的信息
        mTvToolbarCenter.setText(userInfoBean.getName());
        ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvUserPortrait);
    }

    @Override
    public void setLike(boolean isLike) {
        mDdDynamicTool.setItemIsChecked(isLike, ITEM_POSITION_2);
    }

    @Override
    public void setCollect(boolean isCollect) {
        mDdDynamicTool.setItemIsChecked(isCollect, ITEM_POSITION_3);
    }

    @Override
    public void setDigHeadIcon(List<DynamicDigListBean> userInfoBeanList) {
        mDynamicBean.setDigUserInfoList(userInfoBeanList);
        updateCommentCountAndDig();
    }

    @Override
    public void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead) {
        initImageCenterPopWindow(position, amount,
                note, R.string.buy_pay_words_desc, false);
    }

    @Override
    public void upDateFollowFansState(UserInfoBean userInfoBean) {
        //setToolBarRightFollowState(userInfoBean);
    }

    @Override
    public DynamicDetailBeanV2 getCurrentDynamic() {
        return mDynamicBean;
    }

    @Override
    public void updateDynamic(DynamicDetailBeanV2 detailBeanV2) {
        mDynamicBean = detailBeanV2;
        //mDynamicDetailHeader.updateImage(mDynamicBean);
    }

    @Override
    public void setRewardListBeans(List<RewardsListBean> rewardsListBeens) {
        if (rewardsListBeens == null) {
            return;
        }
        mRewardsListBeens.clear();
        mRewardsListBeens.addAll(rewardsListBeens);
    }

    @Override
    public Bundle getArgumentsBundle() {
        return getArguments();
    }

    @Override
    public void updateCommentCountAndDig() {
        mDynamicDetailHeader.updateHeaderViewData(mDynamicBean);
    }

    @Override
    public void refreshData() {
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }


    @Override
    public void refreshData(int position) {
        mHeaderAndFooterWrapper.notifyItemChanged(position);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<DynamicCommentBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            DynamicCommentBean emptyData = new DynamicCommentBean();
            data.add(emptyData);
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void allDataReady() {
        closeLoadingView();
        mCoordinatorLayout.setEnabled(true);
        setAllData();
        mPresenter.allDataReady();
    }

    @Override
    public void loadAllError() {
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        mTvToolbarRight.setVisibility(View.GONE);
        //mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadError();
    }

    @Override
    public void dynamicHasBeDeleted() {
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        mTvToolbarRight.setVisibility(View.GONE);
        //mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadErrorDisableClick();
    }

    private void setAllData() {
        setToolBarUser(mDynamicBean);// 设置标题用户
        initBottomToolData(mDynamicBean);// 初始化底部工具栏数据
//        设置动态详情列表数据
        mDynamicDetailHeader.setDynamicDetial(mDynamicBean);
        updateReward();
        updateCommentCountAndDig();
        onNetResponseSuccess(mDynamicBean.getComments(), false);
        if (mIsLookMore && getListDatas().size() >= DynamicListCommentView.SHOW_MORE_COMMENT_SIZE_LIMIT) {
            mRvList.post(() -> ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(0, -mDynamicDetailHeader.scrollCommentToTop()));
        }
        // 如果当前动态所属用户，就是当前用户，隐藏关注按钮
        /*long user_id = mDynamicBean.getUser_id();
        if (AppApplication.getmCurrentLoginAuth() != null && user_id == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            mTvToolbarRight.setVisibility(View.GONE);
        } else {
            // 获取用户关注状态
            mTvToolbarRight.setVisibility(View.VISIBLE);
            setToolBarRightFollowState(mDynamicBean.getUserInfoBean());
        }*/

    }

    @Override
    public void updateReward() {
        /*if (mDynamicBean.getReward() != null && !TextUtils.isEmpty(mDynamicBean.getReward().getAmount())) {
            mDynamicBean.getReward().setAmount("" + PayConfig.realCurrency2GameCurrency(Double.parseDouble(mDynamicBean.getReward().getAmount()),
                    mPresenter.getRatio()));
        }
        mDynamicDetailHeader.updateReward(mDynamicBean.getId(), mRewardsListBeens, mDynamicBean.getReward(),
                RewardType.DYNAMIC, mPresenter.getIntegrationGoldName());*/
    }


    /**
     * 设置底部工具栏UI
     */
    private void initBottomToolUI() {
        // 初始化底部工具栏数据share
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.ico_zan
                , R.mipmap.home_ico_more
        });
        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.ico_zan_on
                , R.mipmap.home_ico_more
        });
        mDdDynamicTool.setButtonText(new int[]{R.string.share, R.string.comment
                , R.string.dynamic_like, R.string.more});

    }

    /**
     * 进入页面，设置底部工具栏的数据
     */
    private void initBottomToolData(DynamicDetailBeanV2 dynamicBean) {
        // 设置是否喜欢
        mDdDynamicTool.setItemIsChecked(dynamicBean.getHas_digg(), ITEM_POSITION_2);
        //设置是否收藏
        mDdDynamicTool.setItemIsChecked(dynamicBean.getHas_collect(), DynamicDetailMenuView.ITEM_POSITION_3);
    }

    /**
     * 设置底部工具栏的点击事件
     */
    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick((parent, v, postion) -> {
            mDdDynamicTool.getTag(R.id.view_data);
            switch (postion) {
                case DynamicDetailMenuView.ITEM_POSITION_0:
                    // 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_1:
                    // 评论
                    showCommentView();
                    mReplyUserId = 0;
                    mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
                    break;
                case ITEM_POSITION_2:
                    // 处理喜欢逻辑，包括服务器，数据库，ui
                    if(mDynamicBean.isHas_digg()){
                        break;
                    } else {
                        mPresenter.handleLike(!mDynamicBean.isHas_digg(),
                                mDynamicBean.getId(), mDynamicBean);
                        break;
                    }
                case DynamicDetailMenuView.ITEM_POSITION_3:
                    if (mDynamicBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
                        initMyDynamicPopupWindow(mDynamicBean, mDynamicBean.getHas_collect());
                        mMyDynamicPopWindow.show();
                    } else {
                        initOtherDynamicPopupWindow(mDynamicBean, mDynamicBean.getHas_collect());
                        mOtherDynamicPopWindow.show();
                    }
                    break;
                default:
            }
        });
    }

    public void showCommentView() {
        mLLBottomMenuContainer.setVisibility(View.INVISIBLE);
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    /**
     * 设置toolBar上面的关注状态
     */
    private void setToolBarRightFollowState(UserInfoBean userInfoBean1) {
        mTvToolbarRight.setVisibility(View.VISIBLE);
        if (userInfoBean1.isFollowing() && userInfoBean1.isFollower()) {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed_eachother),
                    null);
        } else if (userInfoBean1.isFollower()) {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed), null);
        } else {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_follow), null);
        }
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendCommentV2(mReplyUserId, text);
        mLLBottomMenuContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void reSendComment(DynamicCommentBean dynamicCommentBean) {
        initReSendCommentPopupWindow(dynamicCommentBean, getCurrentDynamic().getId());
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        handleItemClick(position);
    }

    private void handleItemClick(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            if (mListDatas.get(position).getComment_id() != null) {
                initDeleteComentPopupWindow(mListDatas.get(position).getComment_id(), position);
                mDeletCommentPopWindow.show();
            }
        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            showCommentView();
            String contentHint = getString(R.string.default_input_hint);
            if (mListDatas.get(position).getUser_id() != AppApplication.getmCurrentLoginAuth().getUser_id()) {
                contentHint = getString(R.string.reply, mListDatas.get(position).getCommentUser().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        goReportComment(position);

        return false;
    }

    private void goReportComment(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        // 举报
        if (mListDatas.get(position).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(mListDatas.get(position).getCommentUser(), mListDatas.get
                    (position).getComment_id().toString(),
                    null, null, mListDatas.get(position).getComment_content(), ReportType.COMMENT));

        } else {

        }
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        // 游客处理
        if (!TouristConfig.USER_INFO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
        if (userInfoBean.getUser_id().intValue() == -1) {
            return;
        }
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param comment_id      dynamic comment id
     * @param commentPosition current comment position
     */
    private void initDeleteComentPopupWindow(final long comment_id, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL ? getString(R.string.dynamic_list_top_comment) : null)
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    StickTopFragment.startSticTopActivity(getActivity(), StickTopFragment.TYPE_DYNAMIC, getCurrentDynamic().getId(), comment_id);
                    mDeletCommentPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteCommentV2(comment_id, commentPosition);
                    }, true);
                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    protected void initOtherDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, View followView) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(dynamicBean.getUserInfoBean().getFollower() ? getString(R.string.cancel_follow) : "")
                .item2Str(dynamicBean.getFeed_from() == -1 ? "" : getString(R.string.report))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 取消关注
                    mPresenter.handleFollowUser(dynamicBean.getUserInfoBean());
                    dynamicBean.getUserInfoBean().setFollower(false);
                    followView.setVisibility(View.VISIBLE);
                    ((TextView) followView).setCompoundDrawables(null, null, null,
                            null);
                    ((TextView) followView).setText(getString(R.string.add_follow));
                    followView.setBackgroundResource(R.drawable.shape_radus_box_themecolor);
                    refreshData();
                    mOtherDynamicPopWindow.hide();
                })
                .item2ClickListener(() -> {                    // 举报帖子
                    if (mPresenter.handleTouristControl()) {
                        return;
                    }

                    String img = "";
                    if (dynamicBean.getImages() != null && !dynamicBean.getImages().isEmpty()) {
                        img = ImageUtils.imagePathConvertV2(dynamicBean.getImages().get(0).getFile(), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img),
                                100);
                    }
                    ReportResourceBean reportResourceBean = new ReportResourceBean(dynamicBean.getUserInfoBean()
                            , String.valueOf(dynamicBean.getId())
                            , "", img,
                            dynamicBean.getFeed_content()
                            , ReportType.DYNAMIC);
                    reportResourceBean.setDesCanlook(dynamicBean.getPaid_node() == null || dynamicBean
                            .getPaid_node().isPaid());
                    ReportActivity.startReportActivity(mActivity, reportResourceBean);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .build();
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

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    private void initOtherDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, boolean isCollected) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
//                .item1Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
///                .item2Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .item3Str(getString(R.string.report))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                /*.item1ClickListener(() -> {
                    // 收藏
                    mPresenter.handleCollect(dynamicBean);
                    mOtherDynamicPopWindow.hide();
                })*/
                .item2ClickListener(() -> {
                    // 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
                    mOtherDynamicPopWindow.hide();
                })
                .item3ClickListener(() -> {
                    // 举报
                    String img = "";
                    if (dynamicBean.getImages() != null && !dynamicBean.getImages().isEmpty()) {
                        img = ImageUtils.imagePathConvertV2(dynamicBean.getImages().get(0).getFile(), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img),
                                100);
                    }
                    ReportResourceBean reportResourceBean = new ReportResourceBean(dynamicBean.getUserInfoBean(), String.valueOf(dynamicBean
                            .getId()),
                            "", img, dynamicBean.getFeed_content(), ReportType.DYNAMIC);

                    reportResourceBean.setDesCanlook(dynamicBean.getPaid_node() == null || dynamicBean
                            .getPaid_node().isPaid());
                    ReportActivity.startReportActivity(mActivity, reportResourceBean);
                    mOtherDynamicPopWindow.hide();
                })
                .bottomClickListener(() -> mOtherDynamicPopWindow.hide())
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param dynamicBean curent dynamic
     */
    private void initMyDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, boolean isCollected) {
        mMyDynamicPopWindow = ActionPopupWindow.builder()
///                .item1Str(getString(R.string.dynamic_list_share_dynamic))
//                .item2Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item3Str(getString(R.string.dynamic_list_top_dynamic))
                .item4Str(getString(R.string.dynamic_list_delete_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item3ClickListener(() -> {
                    // 置顶
                    StickTopFragment.startSticTopActivity(getActivity(), StickTopFragment.TYPE_DYNAMIC, dynamicBean.getId());
                    mMyDynamicPopWindow.hide();
                })
                .item4ClickListener(() -> {
                    // 删除
                    mMyDynamicPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.dynamic_list_delete_dynamic), () -> {
                        mPresenter.setNeedDynamicListRefresh(false);
                        EventBus.getDefault().post(dynamicBean, DYNAMIC_LIST_DELETE_UPDATE);
                        getActivity().finish();
                    }, true);
                })
                /*.item2ClickListener(() -> {
                    // 收藏
                    mPresenter.handleCollect(dynamicBean);
                    mMyDynamicPopWindow.hide();
                })*/
                .item1ClickListener(() -> {
                    // 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
                    mMyDynamicPopWindow.hide();
                })
                .bottomClickListener(() -> {
                    //取消
                    mMyDynamicPopWindow.hide();
                })
                .build();
    }

    /**
     * @param imagePosition 图片位置
     * @param amout         费用
     * @param note          支付节点
     * @param strRes        文字说明
     * @param isImage       是否是图片收费
     */
    private void initImageCenterPopWindow(final int imagePosition, long amout,
                                          final int note, int strRes, final boolean isImage) {
        mPayImagePopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes) + getString(R
                        .string.buy_pay_member), amout, mPresenter.getIntegrationGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_integration), amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPresenter.payNote(imagePosition, note, isImage);
                    mPayImagePopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayImagePopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayImagePopWindow.show();
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final DynamicCommentBean commentBean, final long
            feed_id) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mReSendCommentPopWindow.hide();
                    mPresenter.reSendComment(commentBean, feed_id);
                })
                .bottomClickListener(() -> mReSendCommentPopWindow.hide())
                .build();
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onCommentTextClick(int position) {
        handleItemClick(position);
    }

    @Override
    public void onCommentTextLongClick(int position) {
        goReportComment(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RewardType.DYNAMIC.id) {
                if (mDynamicBean != null) {
                    mPresenter.updateRewardData(mDynamicBean.getId());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mDeletCommentPopWindow);
        dismissPop(mOtherDynamicPopWindow);
        dismissPop(mMyDynamicPopWindow);
        dismissPop(mPayImagePopWindow);
        dismissPop(mReSendCommentPopWindow);
    }
}
