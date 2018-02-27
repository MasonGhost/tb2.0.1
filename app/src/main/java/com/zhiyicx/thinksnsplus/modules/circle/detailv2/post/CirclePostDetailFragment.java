package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter.PostDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter.PostDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.adapter.PostDetailHeaderView;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_3;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.POST_LIST_DELETE_UPDATE;

/**
 * @author Jliuer
 * @Date 2017/12/01/15:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailFragment extends TSListFragment<CirclePostDetailContract.Presenter, CirclePostCommentBean>
        implements CirclePostDetailContract.View, BaseWebLoad.OnWebLoadListener, InputLimitView.OnSendClickListener,
        OnUserInfoClickListener {

    public static final String CIRCLE_ID = "circle_id";
    public static final String POST_ID = "post_id";
    public static final String BAKC2CIRCLE = "bakc2circle";
    public static final String POST = "post";
    public static final String LOOK_COMMENT_MORE = "look_comment_more";

    public static final String POST_LIST_NEED_REFRESH = "post_list_need_refresh";
    public static final String POST_DATA = "post_data";


    @BindView(R.id.behavior_demo_coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.ll_bottom_menu_container)
    ViewGroup mLLBottomMenuContainer;
    @BindView(R.id.toolbar_top_blank)
    View mToolbarTopBlank;
    @BindView(R.id.iv_user_portrait)
    UserAvatarView mIvUserPortrait;

    private int mReplyUserId;// 被评论者的 id ,评论动态 id = 0

    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDealPostPopWindow;

    // 权限说明提示框
    private ActionPopupWindow mAuditTipPop;

    private CirclePostListBean mCirclePostDetailBean;

    private PostDetailHeaderView mPostDetailHeaderView;

    private RewardsCountBean mRewardsCountBean;
    private List<RewardsListBean> mRewardsListBeen = new ArrayList<>();
    private boolean mIsLookMore;

    public static CirclePostDetailFragment newInstance(Bundle bundle) {
        CirclePostDetailFragment postDetailFragment = new CirclePostDetailFragment();
        postDetailFragment.setArguments(bundle);
        return postDetailFragment;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
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
    protected int getBodyLayoutId() {
        return R.layout.fragment_post_detail;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        boolean canGotoCircle = false;
        if (mCirclePostDetailBean == null && getArguments() != null) {
            mCirclePostDetailBean = getArguments().getParcelable(POST);
            if (mCirclePostDetailBean == null) {
                mCirclePostDetailBean = new CirclePostListBean();
                mCirclePostDetailBean.setGroup_id(getArguments().getLong(CIRCLE_ID));
                mCirclePostDetailBean.setId(getArguments().getLong(POST_ID));
            }
            canGotoCircle = getArguments().getBoolean(BAKC2CIRCLE);
            mIsLookMore = getArguments().getBoolean(LOOK_COMMENT_MORE);
        }
        mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        initHeaderView(canGotoCircle);
        initBottomToolStyle();
        initBottomToolListener();
        initListener();
    }

    @Override
    public Bundle getArgumentsBundle() {
        return getArguments();
    }

    @Override
    protected void initData() {
        super.initData();
//        setToolBarInfo();
        requestNetData(0L, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        PostDetailCommentItem commentItem = new PostDetailCommentItem(new
                ItemOnCommentListener());
        multiItemTypeAdapter.addItemViewDelegate(commentItem);
        multiItemTypeAdapter.addItemViewDelegate(new PostDetailCommentEmptyItem());
        return multiItemTypeAdapter;
    }

    @Override
    public void setCollect(boolean isCollected) {
        mDdDynamicTool.setItemIsChecked(isCollected, ITEM_POSITION_3);
    }

    @Override
    public void setDigg(boolean isDigged) {
        mDdDynamicTool.setItemIsChecked(isDigged, ITEM_POSITION_0);
        if (mCirclePostDetailBean.getDigList() != null) {
            mPostDetailHeaderView.updateDigList(mCirclePostDetailBean);
        }
    }

    @Override
    public void onLoadFinish() {
        closeLoadingView();
        if (mIsLookMore && getListDatas().size() >= DynamicListCommentView
                .SHOW_MORE_COMMENT_SIZE_LIMIT) {
            mRvList.post(() -> ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset
                    (0, -mPostDetailHeaderView.scrollCommentToTop()));
        }
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mReplyUserId, text);
        mLLBottomMenuContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public long getPostId() {
        return mCirclePostDetailBean.getId();
    }

    @Override
    public long getCircleId() {
        return mCirclePostDetailBean.getGroup_id();
    }

    @Override
    public void allDataReady(CirclePostListBean data) {
        mCoordinatorLayout.setEnabled(true);
        mPostDetailHeaderView.setDetail(data);
        mPostDetailHeaderView.updateDigList(data);
        mCirclePostDetailBean = data;
        onNetResponseSuccess(data.getComments(), false);
        initBottomToolData(data);
        setToolBarInfo();
        setToolBarRightFollowState(mCirclePostDetailBean.getUserInfoBean());
    }

    @Override
    public void postHasBeDeleted() {
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadErrorDisableClick();
    }

    @Override
    public void loadAllError() {
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadError();
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public CirclePostListBean getCurrentePost() {
        return mCirclePostDetailBean;
    }

    @Override
    public void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> datas) {
        this.mRewardsCountBean = rewardsCountBean;
        this.mRewardsListBeen.clear();
        this.mRewardsListBeen.addAll(datas);
        mPostDetailHeaderView.updateReward(mCirclePostDetailBean.getId(), mRewardsListBeen,
                mRewardsCountBean, RewardType.POST, mPresenter.getIntegrationGoldName());
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CirclePostCommentBean> data, boolean
            isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) {
                CirclePostCommentBean emptyData = new CirclePostCommentBean();
                data.add(emptyData);
            }
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RewardType.POST.id) {
                if (mCirclePostDetailBean != null) {
                    mPresenter.updateRewardData();
                }
            }
        }
    }

    @Override
    public void upDateFollowFansState(UserInfoBean userInfoBean) {
        setToolBarRightFollowState(userInfoBean);
    }

    @Override
    public void updateCommentView(CirclePostListBean currentePost) {
        mPostDetailHeaderView.updateCommentView(currentePost);
    }

    class ItemOnCommentListener implements PostDetailCommentItem.OnCommentItemListener {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            position = position - mHeaderAndFooterWrapper.getHeadersCount();
            CirclePostCommentBean circlePostCommentBean = mListDatas.get(position);
            boolean isJoined = mCirclePostDetailBean.getGroup().getJoined() != null;
            boolean isBlackList = isJoined && CircleMembers.BLACKLIST.equals(mCirclePostDetailBean.getGroup().getJoined().getRole());

            if (circlePostCommentBean != null && !TextUtils.isEmpty(circlePostCommentBean
                    .getContent())) {
                if (circlePostCommentBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                    if (mListDatas.get(position).getId() != -1) {
                        initDeleteCommentPopupWindow(circlePostCommentBean, isBlackList);
                        mDeletCommentPopWindow.show();
                    }
                } else {
                    if (isBlackList) {
                        showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
                        return;
                    }
                    mReplyUserId = (int) circlePostCommentBean.getUser_id();
                    showCommentView();
                    String contentHint = getString(R.string.default_input_hint);
                    if (circlePostCommentBean.getReply_to_user_id() != mCirclePostDetailBean
                            .getId()) {
                        contentHint = getString(R.string.reply, circlePostCommentBean
                                .getCommentUser().getName());
                    }
                    mIlvComment.setEtContentHint(contentHint);
                }
            }
        }

        @Override
        public void onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
            goReportComment(position);
        }

        @Override
        public void onUserInfoClick(UserInfoBean userInfoBean) {
            PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
        }
    }

    /**
     * 举报
     *
     * @param position
     */
    private void goReportComment(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();

        boolean isJoined = mCirclePostDetailBean.getGroup().getJoined() != null;
        boolean isBlackList = isJoined && CircleMembers.BLACKLIST.equals(mCirclePostDetailBean.getGroup().getJoined().getRole());

        if (isBlackList) {
            showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
            return;
        }
        boolean isNormalMember = isJoined && CircleMembers.MEMBER.equals(mCirclePostDetailBean.getGroup().getJoined().getRole());
        boolean isMine = mListDatas.get(position).getUser_id() == AppApplication.getMyUserIdWithdefault();

        // 举报
        if (!isMine && isNormalMember) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(mListDatas.get
                    (position).getCommentUser(), mListDatas.get
                    (position).getId().toString(),
                    null, null, mListDatas.get(position).getContent(), ReportType.CIRCLE_COMMENT));

        } else {
            // 删除
            initDeleteCommentPopupWindow(mListDatas.get(position), !isMine);
            mDeletCommentPopWindow.show();
        }
    }


    private void initHeaderView(boolean canGotoCircle) {
        mPostDetailHeaderView = new PostDetailHeaderView(getContext(), mPresenter.getAdvert());
        mPostDetailHeaderView.setWebLoadListener(this);
        mPostDetailHeaderView.setCanGotoCircle(canGotoCircle);
        mHeaderAndFooterWrapper.addHeaderView(mPostDetailHeaderView.getInfoDetailHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initBottomToolStyle() {
        mDdDynamicTool.setButtonText(new int[]{R.string.dynamic_like, R.string.comment,
                R.string.share, R.string.more});
        mDdDynamicTool.setImageNormalResourceIds(new int[]{
                R.mipmap.home_ico_good_normal, R.mipmap.home_ico_comment_normal,
                R.mipmap.detail_ico_share_normal, R.mipmap.home_ico_more});

        mDdDynamicTool.setImageCheckedResourceIds(new int[]{
                R.mipmap.home_ico_good_high, R.mipmap.home_ico_comment_normal,
                R.mipmap.detail_ico_share_normal, R.mipmap.home_ico_more, R.mipmap.home_ico_more});
        mDdDynamicTool.setData();
    }

    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick((parent, v, position) -> {
            mDdDynamicTool.getTag(R.id.view_data);
            boolean isJoined = mCirclePostDetailBean.getGroup().getJoined() != null;
            boolean isBlackList = isJoined && CircleMembers.BLACKLIST.equals(mCirclePostDetailBean.getGroup().getJoined().getRole());

            switch (position) {
                // 点赞
                case DynamicDetailMenuView.ITEM_POSITION_0:
                    if (isBlackList) {
                        showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
                        return;
                    }
                    mPresenter.handleLike(!mCirclePostDetailBean.getLiked(),
                            mCirclePostDetailBean.getId());
                    break;
                // 评论
                case DynamicDetailMenuView.ITEM_POSITION_1:
                    if (isBlackList) {
                        showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
                        return;
                    }
                    showCommentView();
                    String contentHint = getString(R.string.default_input_hint);
                    mIlvComment.setEtContentHint(contentHint);
                    mReplyUserId = 0;
                    break;
                // 分享
                case DynamicDetailMenuView.ITEM_POSITION_2:
                    if (isBlackList) {
                        showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
                        return;
                    }
                    Bitmap bitmap = FileUtils.readImgFromFile(getActivity(), "info_share");
                    mPresenter.shareInfo(bitmap);
                    break;
                // 更多
                case ITEM_POSITION_3:
                    if (isBlackList && mCirclePostDetailBean.getUser_id() != AppApplication.getMyUserIdWithdefault()) {
                        showAuditTipPopupWindow(getString(R.string.circle_member_added_blacklist));
                        return;
                    }
                    initDealPostPopupWindow(mCirclePostDetailBean, mCirclePostDetailBean
                            .getCollected());
                    mDealPostPopWindow.show();
                    break;
                default:
                    break;
            }
        });
    }

    private void initListener() {
        mCoordinatorLayout.setEnabled(false);
        RxView.clicks(mTvToolbarLeft)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> getActivity().finish());
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> mPresenter.handleFollowUser(mCirclePostDetailBean.getUserInfoBean()));
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mIlvComment.setVisibility(View.GONE);
                    mIlvComment.clearFocus();
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                    mLLBottomMenuContainer.setVisibility(View.VISIBLE);
                    mVShadow.setVisibility(View.GONE);

                });
        RxView.clicks(mTvToolbarCenter)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mCirclePostDetailBean.getUserInfoBean()));
        RxView.clicks(mIvUserPortrait)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mCirclePostDetailBean.getUserInfoBean()));
        mIlvComment.setOnSendClickListener(this);
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

    private void setToolBarInfo() {
        if (mCirclePostDetailBean.getUserInfoBean() == null) {
            return;
        }
        initBottomToolData(mCirclePostDetailBean);
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        UserInfoBean userInfoBean = mCirclePostDetailBean.getUserInfoBean();
        mTvToolbarCenter.setText(userInfoBean.getName());
        ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvUserPortrait);
    }

    /**
     * 设置toolBar上面的关注状态
     */
    private void setToolBarRightFollowState(UserInfoBean userInfoBean1) {
        mTvToolbarRight.setVisibility(userInfoBean1.getUser_id() != AppApplication.getMyUserIdWithdefault() ? View.VISIBLE : View.GONE);
        if (userInfoBean1.isFollowing() && userInfoBean1.isFollower()) {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed_eachother),
                    null);
        } else if (userInfoBean1.isFollower()) {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed), null);
        } else {
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_follow), null);
        }
    }

    private void initBottomToolData(CirclePostListBean circlePostDetailBean) {
        // 设置是否喜欢
        mDdDynamicTool.setItemIsChecked(circlePostDetailBean.getLiked(), DynamicDetailMenuView
                .ITEM_POSITION_0);
        //设置是否收藏
        mDdDynamicTool.setItemIsChecked(circlePostDetailBean.getCollected(), ITEM_POSITION_3);
    }

    /**
     * 初始化评论删除选择弹框
     */
    private void initDeleteCommentPopupWindow(final CirclePostCommentBean data, boolean isBlackList) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL && data.getId() != -1L && !isBlackList ? getString(R.string
                        .dynamic_list_top_comment) : null)
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 跳转置顶页面
                    mDeletCommentPopWindow.hide();
                    Bundle bundle = new Bundle();
                    bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_POST);
                    bundle.putLong(StickTopFragment.PARENT_ID, mCirclePostDetailBean.getId());
                    // 资源id
                    bundle.putLong(StickTopFragment.CHILD_ID, data.getId());
                    Intent intent = new Intent(getActivity(), StickTopActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    mDeletCommentPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mPresenter.deleteComment(data);
                    mDeletCommentPopWindow.hide();

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    private void initDealPostPopupWindow(final CirclePostListBean circlePostListBean, boolean
            isCollected) {
        boolean isMine = circlePostListBean.getUser_id() == AppApplication.getMyUserIdWithdefault();
        boolean isManager = false;
        if (circlePostListBean.getGroup() != null && circlePostListBean.getGroup().getJoined() != null) {
            isManager = CircleMembers.FOUNDER.equals(circlePostListBean.getGroup().getJoined().getRole()) ||
                    CircleMembers.ADMINISTRATOR.equals(circlePostListBean.getGroup().getJoined().getRole());
        }
        boolean isPinned = circlePostListBean.getPinned();
        boolean isBlackList = circlePostListBean.getGroup().getJoined() != null && CircleMembers.BLACKLIST.equals(mCirclePostDetailBean.getGroup().getJoined().getRole());

        mDealPostPopWindow = ActionPopupWindow.builder()
                .item1Str(isMine && !isBlackList && !isManager ? getString(R.string.post_apply_for_top) : "")
                .item2Str(getString(isManager ? (isPinned ? R.string.post_undo_top : R.string.post_apply_top) : R.string.empty))
                .item3Str(isMine ? getString(R.string.delete_post) : (!isBlackList ? getString(isCollected ? R
                        .string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic) : null))
                .item4Str(getString(isManager && !isMine ? R.string.delete_post : R.string.empty))
                .item5Str(isMine || isBlackList || isManager ? getString(R.string.empty) : getString(R.string.report))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 申请置顶
                    if (circlePostListBean.hasPinned()) {
                        showSnackErrorMessage(getString(R.string.info_alert_reapply_for_top));
                    } else {
                        // 跳转置顶页面
                        Bundle bundle = new Bundle();
                        // 资源类型
                        bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_POST);
                        // 资源id
                        bundle.putLong(StickTopFragment.PARENT_ID, circlePostListBean.getId());
                        Intent intent = new Intent(getActivity(), StickTopActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    mDealPostPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 管理员置顶操作
                    if (isPinned) {
                        mPresenter.undoTopPost(circlePostListBean.getId());
                    } else {
                        managerStickTop(circlePostListBean.getId());
                    }
                    mDealPostPopWindow.hide();
                })
                .item3ClickListener(() -> {
                    // 收藏
                    // 如果是自己发布的，则不能收藏只能删除
                    if (isMine) {
                        showDeleteTipPopupWindow(getString(R.string.delete_post), true, circlePostListBean);
                    } else {
                        mPresenter.handleCollect(!circlePostListBean.getCollected(),
                                circlePostListBean.getId());
                    }
                    mDealPostPopWindow.hide();
                })
                .item4ClickListener(() -> {
                    // 管理员删除
                    showDeleteTipPopupWindow(getString(R.string.delete_post), true, circlePostListBean);
                    mDealPostPopWindow.hide();
                })
                .item5ClickListener(() -> {
                    // 举报
                    if (!mPresenter.handleTouristControl()) {
                        String img = "";
                        if (circlePostListBean.getImages() != null && !circlePostListBean.getImages()
                                .isEmpty()) {
                            img = ImageUtils.imagePathConvertV2(circlePostListBean.getImages().get(0)
                                            .getFile_id(), getResources()
                                            .getDimensionPixelOffset(R.dimen.report_resource_img),
                                    getResources()
                                            .getDimensionPixelOffset(R.dimen.report_resource_img),
                                    100);
                        }

                        ReportActivity.startReportActivity(mActivity, new ReportResourceBean
                                (circlePostListBean.getUser(), String.valueOf
                                        (circlePostListBean.getId()),
                                        circlePostListBean.getTitle(), img, circlePostListBean.getSummary(), ReportType.CIRCLE_POST));
                        mDealPostPopWindow.hide();
                    }
                })
                .bottomClickListener(() -> mDealPostPopWindow.hide())
                .build();
    }

    @Override
    public void onPause() {
        mPostDetailHeaderView.getContentWebView().onPause();
        mPostDetailHeaderView.getContentWebView().pauseTimers();
        mPostDetailHeaderView.getContentSubWebView().onPause();
        mPostDetailHeaderView.getContentSubWebView().pauseTimers();
        super.onPause();

    }

    @Override
    public void onResume() {
        mPostDetailHeaderView.getContentWebView().onResume();
        mPostDetailHeaderView.getContentWebView().resumeTimers();
        mPostDetailHeaderView.getContentSubWebView().onResume();
        mPostDetailHeaderView.getContentSubWebView().resumeTimers();
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPostDetailHeaderView.destroyedWeb();
    }

    /**
     * 权限说明提示弹框
     */
    private void showAuditTipPopupWindow(String tipStr) {
        mAuditTipPop = ActionPopupWindow.builder()
                .item1Str(getString(R.string.info_publish_hint))
                .desStr(tipStr)
                .bottomStr(getString(R.string.i_know))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mAuditTipPop.hide()).build();
        mAuditTipPop.show();
    }

    private void managerStickTop(Long id) {
        LinkDialog dialog = createLinkDialog();
        dialog.setListener(new LinkDialog.OnDialogClickListener() {
            @Override
            public void onConfirmButtonClick(String name, String url) {
                if (TextUtils.isEmpty(url)) {
                    dialog.setErrorMessage(getString(R.string.post_apply_top_days));
                    return;
                }
                int day = Integer.valueOf(url);
                if (day > 0 && day <= 31) {
                    mPresenter.stickTopPost(id, day);
                    dialog.dismiss();
                } else {
                    dialog.setErrorMessage(getString(R.string.post_apply_top_days));
                }
            }

            @Override
            public void onCancelButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), LinkDialog.Tag);
    }

    private LinkDialog createLinkDialog() {
        return LinkDialog.createLinkDialog()
                .setUrlHinit(getString(R.string.post_apply_top_days))
                .setTitleStr(getString(R.string.set_post_apply_top_days))
                .setNameVisible(false)
                .setNeedNumFomatFilter(true);
    }

    protected void showDeleteTipPopupWindow(String tipStr,
                                            boolean createEveryTime, final CirclePostListBean circlePostListBean) {
        super.showDeleteTipPopupWindow(tipStr, () -> {
            mPresenter.setNeedDynamicListRefresh(false);
            EventBus.getDefault().post(circlePostListBean, POST_LIST_DELETE_UPDATE);
            mActivity.finish();
        }, createEveryTime);
    }
}
