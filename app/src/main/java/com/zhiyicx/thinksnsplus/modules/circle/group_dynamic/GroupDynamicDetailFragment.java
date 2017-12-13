package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.adapter.GroupDynamicDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.GroupDynamicCommentEmptyItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_3;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_COMMENT_ID;
import static com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopFragment.TOP_DYNAMIC_ID;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailFragment extends TSListFragment<GroupDynamicDetailContract.Presenter, GroupDynamicCommentListBean>
        implements GroupDynamicDetailContract.View, OnUserInfoClickListener, OnCommentTextClickListener,
        InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, GroupDynamicDetailHeader.OnImageClickLisenter,
        TextViewUtils.OnSpanTextClickListener {
    public static final String DYNAMIC_DETAIL_DATA = "dynamic_detail_data";
    public static final String DYNAMIC_LIST_NEED_REFRESH = "dynamic_list_need_refresh";
    public static final String DYNAMIC_DETAIL_DATA_TYPE = "dynamic_detail_data_type";
    public static final String DYNAMIC_DETAIL_DATA_POSITION = "dynamic_detail_data_position";
    public static final String LOOK_COMMENT_MORE = "look_comment_more";
    // 动态详情列表，各个item的位置
    private static final int DYNAMIC_ITEM_CONTENT = 0;
    private static final int DYNAMIC_ITEM_DIG = 1;
    //private static final int DYNAMIC_ITEM_COMMENT >1;

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

    private GroupDynamicListBean mGroupDynamicListBean;// 上一个页面传进来的数据
    private boolean mIsLookMore = false;
    private GroupDynamicDetailHeader mDynamicDetailHeader;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mOtherDynamicPopWindow;
    private ActionPopupWindow mMyDynamicPopWindow;

    private ActionPopupWindow mReSendCommentPopWindow;

    private PayPopWindow mPayImagePopWindow;

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
        if (mGroupDynamicListBean == null) {
            mPresenter.getCurrentDynamicDetail(getArguments().getLong(MessageCommentAdapter
                    .BUNDLE_SOURCE_ID), 0, true);
        } else {
            mPresenter.getDetailAll(mGroupDynamicListBean.getGroup_id(), mGroupDynamicListBean.getId(), DEFAULT_PAGE_MAX_ID, mGroupDynamicListBean
                    .getUser_id() + "");
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //initToolbar();
        //initToolbarTopBlankHeight();
        initBottomToolUI();
        initBottomToolListener();
        initHeaderView();
        initListener();
//        setOverScroll(false, false);
    }

    private void initToolbar() {
        mToolbar.setPadding(0, DeviceUtils.getStatuBarHeight(getContext()), 0, 0);
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
                    mPresenter.handleFollowUser(mGroupDynamicListBean.getUserInfoBean());
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
        RxView.clicks(mTvToolbarCenter)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mGroupDynamicListBean.getUserInfoBean()));
        RxView.clicks(mIvUserPortrait)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> onUserInfoClick(mGroupDynamicListBean.getUserInfoBean()));
        mIlvComment.setOnSendClickListener(this);
        mToolbar.setOnSystemUiVisibilityChangeListener(visibility -> {

        });
    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mDynamicDetailHeader = new GroupDynamicDetailHeader(getContext(), mPresenter.getAdvert());
        mDynamicDetailHeader.setOnImageClickLisenter(this);
        mHeaderAndFooterWrapper.addHeaderView(mDynamicDetailHeader.getDynamicDetailHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        // 处理上个页面传过来的动态数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            mIsLookMore = bundle.getBoolean(LOOK_COMMENT_MORE);
            mGroupDynamicListBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            if (mGroupDynamicListBean == null) {
                mPresenter.getCurrentDynamicDetail(bundle.getLong(MessageCommentAdapter
                        .BUNDLE_SOURCE_ID), 0, true);
            } else {
                mPresenter.getDetailAll(mGroupDynamicListBean.getGroup_id(), mGroupDynamicListBean.getId(),
                        DEFAULT_PAGE_MAX_ID, mGroupDynamicListBean.getUser_id() + "");
                mPresenter.getCurrentDynamicDetail(mGroupDynamicListBean.getGroup_id(), mGroupDynamicListBean.getId(), true);
            }
        }
    }

    @Override
    public void initDynamicDetail(GroupDynamicListBean dynamicBean) {
        mGroupDynamicListBean = dynamicBean;
        if (mPresenter.checkCurrentDynamicIsDeleted(mGroupDynamicListBean.getUser_id(), mGroupDynamicListBean.getId())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
            return;
        }
        if (mGroupDynamicListBean.getMGroupDynamicLikeListBeanList() == null) {
            mPresenter.getDetailAll(mGroupDynamicListBean.getGroup_id(), mGroupDynamicListBean.getId(), DEFAULT_PAGE_MAX_ID, String.valueOf(mGroupDynamicListBean
                    .getUser_id()));
        } else {
            allDataReady();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGroupDynamicListBean != null && mPresenter.checkCurrentDynamicIsDeleted(mGroupDynamicListBean.getUser_id(), mGroupDynamicListBean.getId())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
        }
    }

    @Override
    protected MultiItemTypeAdapter<GroupDynamicCommentListBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        GroupDynamicDetailCommentItem dynamicDetailCommentItem = new GroupDynamicDetailCommentItem();
        dynamicDetailCommentItem.setOnUserInfoClickListener(this);
        dynamicDetailCommentItem.setOnCommentTextClickListener(this);
        adapter.addItemViewDelegate(dynamicDetailCommentItem);
        GroupDynamicCommentEmptyItem dynamicCommentEmptyItem = new GroupDynamicCommentEmptyItem();
        adapter.addItemViewDelegate(dynamicCommentEmptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onImageClick(int iamgePosition, double amount, int note) {
        initImageCenterPopWindow(iamgePosition, (float) amount, note, R.string.buy_pay_words_desc, true);
    }

    public static GroupDynamicDetailFragment initFragment(Bundle bundle) {
        GroupDynamicDetailFragment dynamicDetailFragment = new GroupDynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

    /**
     * 设置toolBar上面的用户头像,关注状态
     */
    private void setToolBarUser(GroupDynamicListBean dynamicBean) {
        // 设置用户头像，名称
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        UserInfoBean userInfoBean = dynamicBean.getUserInfoBean();// 动态所属用户的信息
        mTvToolbarCenter.setText(userInfoBean.getName());
        ImageUtils.loadCircleUserHeadPic(userInfoBean, mIvUserPortrait);
//        final int headIconWidth = getResources().getDimensionPixelSize(R.dimen.headpic_for_assist);
//        Glide.with(getContext())
//                .load(ImageUtils.getUserAvatar(dynamicBean.getUserInfoBean()))
//                .bitmapTransform(new GlideCircleTransform(getContext()))
//                .placeholder(R.mipmap.pic_default_portrait1)
//                .error(R.mipmap.pic_default_portrait1)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        resource.setBounds(0, 0, headIconWidth, headIconWidth);
//                        mTvToolbarCenter.setCompoundDrawables(resource, null, null, null);
//                    }
//                });
    }

    @Override
    public void setLike(boolean isLike) {
        mDdDynamicTool.setItemIsChecked(isLike, ITEM_POSITION_0);
    }

    @Override
    public void setCollect(boolean isCollect) {
        mDdDynamicTool.setItemIsChecked(isCollect, ITEM_POSITION_3);
    }

    @Override
    public void setDigHeadIcon(List<DynamicDigListBean> userInfoBeanList) {
        mGroupDynamicListBean.setMGroupDynamicLikeListBeanList(userInfoBeanList);
        updateCommentCountAndDig();
    }

    @Override
    public void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead) {
        initImageCenterPopWindow(position, (float) amount,
                note, R.string.buy_pay_words_desc, false);
    }

    @Override
    public void upDateFollowFansState(UserInfoBean userInfoBean) {
        setToolBarRightFollowState(userInfoBean);
    }

    @Override
    public void initFollowState(UserInfoBean userInfoBean) {
        setToolBarRightFollowState(userInfoBean);
    }

    @Override
    public GroupDynamicListBean getCurrentDynamic() {
        return mGroupDynamicListBean;
    }

    @Override
    public void updateDynamic(GroupDynamicListBean detailBeanV2) {
        mGroupDynamicListBean = detailBeanV2;
        mDynamicDetailHeader.updateImage(mGroupDynamicListBean);
    }

    @Override
    public Bundle getArgumentsBundle() {
        return getArguments();
    }

    @Override
    public void updateCommentCountAndDig() {
        mDynamicDetailHeader.updateHeaderViewData(mGroupDynamicListBean);
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
    public void onNetResponseSuccess(@NotNull List<GroupDynamicCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            GroupDynamicCommentListBean emptyData = new GroupDynamicCommentListBean();
            data.add(emptyData);
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void allDataReady() {
        closeLoadingView();
        mCoordinatorLayout.setEnabled(true);
        setAllData();
    }

    @Override
    public void loadAllError() {
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadError();
    }

    @Override
    public void dynamicHasBeDeleted() {
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadErrorDisableClick();
    }

    private void setAllData() {
        setToolBarUser(mGroupDynamicListBean);// 设置标题用户
        initBottomToolData(mGroupDynamicListBean);// 初始化底部工具栏数据
//        设置动态详情列表数据
        mDynamicDetailHeader.setDynamicDetial(mGroupDynamicListBean);
        updateCommentCountAndDig();
        onNetResponseSuccess(mGroupDynamicListBean.getCommentslist(), false);
        if (mIsLookMore) {
            mRvList.scrollToPosition(1);
        }
        // 如果当前动态所属用户，就是当前用户，隐藏关注按钮
        long user_id = mGroupDynamicListBean.getUser_id();
        if (AppApplication.getmCurrentLoginAuth() != null && user_id == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            mTvToolbarRight.setVisibility(View.GONE);
        } else {
            // 获取用户关注状态
            mTvToolbarRight.setVisibility(View.VISIBLE);
            setToolBarRightFollowState(mGroupDynamicListBean.getUserInfoBean());
        }
    }


    /**
     * 设置底部工具栏UI
     */
    private void initBottomToolUI() {
        // 初始化底部工具栏数据
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.home_ico_good_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_more
        });
        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.home_ico_good_high
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.home_ico_more
        });
        mDdDynamicTool.setButtonText(new int[]{R.string.dynamic_like, R.string.comment
                , R.string.share, R.string.more});

    }

    /**
     * 进入页面，设置底部工具栏的数据
     */
    private void initBottomToolData(GroupDynamicListBean dynamicBean) {
        // 设置是否喜欢
        mDdDynamicTool.setItemIsChecked(dynamicBean.getHas_like(), DynamicDetailMenuView.ITEM_POSITION_0);
        //设置是否收藏
        mDdDynamicTool.setItemIsChecked(dynamicBean.getHas_collection(), DynamicDetailMenuView.ITEM_POSITION_3);
    }

    /**
     * 设置底部工具栏的点击事件
     */
    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick((parent, v, postion) -> {
            mDdDynamicTool.getTag(R.id.view_data);
            switch (postion) {
                case DynamicDetailMenuView.ITEM_POSITION_0:
                    // 处理喜欢逻辑，包括服务器，数据库，ui
                    boolean isLike = mGroupDynamicListBean.getHas_like();
                    mPresenter.handleLike(!isLike, mGroupDynamicListBean.getGroup_id(),
                            mGroupDynamicListBean.getId(), mGroupDynamicListBean);
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_1:
                    // 评论
                    showCommentView();
                    mReplyUserId = 0;
                    mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_2:
                    // 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_3:
                    // 处理喜欢逻辑，包括服务器，数据库，ui
                    if (mGroupDynamicListBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
                        initMyDynamicPopupWindow(mGroupDynamicListBean, mGroupDynamicListBean.getHas_collection());
                        mMyDynamicPopWindow.show();
                    } else {
                        initOtherDynamicPopupWindow(mGroupDynamicListBean, mGroupDynamicListBean.getHas_collection());
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
            mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed_eachother), null);
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
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        handleItemClick(position);
    }

    private void handleItemClick(int position) {
        position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            if (mListDatas.get(position).getId() != null) {
                initDeleteComentPopupWindow(mListDatas.get(position).getId(), position);
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
        return false;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param comment_id      dynamic comment id
     * @param commentPosition current comment position
     */
    private void initDeleteComentPopupWindow(final long comment_id, final int commentPosition) {
        if (mDeletCommentPopWindow != null && mDeletCommentPopWindow.isShowing()) {
            mDeletCommentPopWindow.dismiss();
        } else {
            mDeletCommentPopWindow = ActionPopupWindow.builder()
                    .item1Str(false ? getString(R.string.dynamic_list_top_comment) : null)
                    .item2Str(getString(R.string.dynamic_list_delete_comment))
                    .item1Color(ContextCompat.getColor(getContext(), R.color.important_for_theme))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item1ClickListener(() -> {
                        Intent intent = new Intent(getActivity(), DynamicCommentTopActivity.class);
                        intent.putExtra(TOP_DYNAMIC_COMMENT_ID, comment_id);
                        intent.putExtra(TOP_DYNAMIC_ID, getCurrentDynamic().getId());
                        mDeletCommentPopWindow.hide();
                        startActivity(intent);
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
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    private void initOtherDynamicPopupWindow(final GroupDynamicListBean dynamicBean, boolean isCollected) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
//                .item2Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {// 收藏
                    mPresenter.handleCollect(dynamicBean);
                    mOtherDynamicPopWindow.hide();
                })
                .item2ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
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
    private void initMyDynamicPopupWindow(final GroupDynamicListBean dynamicBean, boolean isCollected) {
        mMyDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item2Str(getString(R.string.dynamic_list_delete_dynamic))
//                .item3Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {// 收藏
                    mPresenter.handleCollect(dynamicBean);
                    mMyDynamicPopWindow.hide();
                })
                .item2ClickListener(() -> {// 删除
                    mMyDynamicPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.dynamic_list_delete_dynamic), () -> {
                        EventBus.getDefault().post(dynamicBean, DYNAMIC_LIST_DELETE_UPDATE);
                        getActivity().finish();
                    }, true);
                })
                .item3ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(getCurrentDynamic(), mDynamicDetailHeader.getSharBitmap());
                    mMyDynamicPopWindow.hide();
                })
                .bottomClickListener(() -> {//取消
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
    private void initImageCenterPopWindow(final int imagePosition, float amout,
                                          final int note, int strRes, final boolean isImage) {
//        if (mPayImagePopWindow != null) {
//            mPayImagePopWindow.show();
//            return;
//        }
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
                        .string.buy_pay_member), amout, mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_money), amout))
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
     * 为了防止toolbar移动，导致状态栏透明，添加一个白色的色块
     */
    private void initToolbarTopBlankHeight() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolbarTopBlank.getLayoutParams();
        layoutParams.height = DeviceUtils.getStatuBarHeight(getContext());
    }

    @Override
    protected void onOverScrolled() {
        super.onOverScrolled();
//        mLLBottomMenuContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCommentTextClick(int position) {
        handleItemClick(position);
    }

    @Override
    public void onCommentTextLongClick(int position) {

    }
}
