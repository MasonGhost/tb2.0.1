package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.InputLimitView.OnSendClickListener;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.DynamicCommentEmptyItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
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

public class DynamicDetailFragment extends TSListFragment<DynamicDetailContract.Presenter, DynamicCommentBean> implements DynamicDetailContract.View, OnUserInfoClickListener, OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener {
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

    private DynamicBean mDynamicBean;// 上一个页面传进来的数据
    private FollowFansBean mFollowFansBean;// 用户关注状态
    private boolean mIsLookMore = false;
    private DynamicDetailHeader mDynamicDetailHeader;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mOtherDynamicPopWindow;
    private ActionPopupWindow mMyDynamicPopWindow;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected View getLeftViewOfMusicWindow() {
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
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        if (mDynamicBean == null) {
            mPresenter.getCurrentDynamic(getArguments().getLong(MessageCommentAdapter.BUNDLE_SOURCE_ID));
        } else {
            mPresenter.getDetailAll(mDynamicBean.getFeed_id(), DEFAULT_PAGE_MAX_ID, mDynamicBean.getUser_id() + "");
        }
    }

    /**
     * 特别修改
     *
     * @return
     */
    @Override
    protected int getstatusbarAndToolbarHeight() {
        return getResources().getDimensionPixelSize(R.dimen.toolbar_height_include_line_height) + DeviceUtils.getStatuBarHeight(getContext());
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolbar();
        initToolbarTopBlankHeight();
        initBottomToolUI();
        initBottomToolListener();
        initHeaderView();
        initListener();
        setOverScroll(false, false);
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
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        getActivity().finish();
                    }
                });
        RxView.clicks(mTvToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mFollowFansBean != null) {
                            mPresenter.handleFollowUser(mFollowFansBean);
                        }
                    }
                });
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mIlvComment.setVisibility(View.GONE);
                        mIlvComment.clearFocus();
                        DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                        mLLBottomMenuContainer.setVisibility(View.VISIBLE);
                        mVShadow.setVisibility(View.GONE);

                    }
                });
        RxView.clicks(mTvToolbarCenter)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        onUserInfoClick(mDynamicBean.getUserInfoBean());
                    }
                });
        mIlvComment.setOnSendClickListener(this);
        mToolbar.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {

            }
        });
    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mDynamicDetailHeader = new DynamicDetailHeader(getContext());
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
            mDynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            if (mDynamicBean == null) {
                mPresenter.getCurrentDynamic(bundle.getLong(MessageCommentAdapter.BUNDLE_SOURCE_ID));
            } else {
                mPresenter.getCurrentDynamic(mDynamicBean.getFeed_id());
            }
        }
    }

    @Override
    public void initDynamicDetial(DynamicBean dynamicBean) {
        mDynamicBean = dynamicBean;
        if (mPresenter.checkCurrentDynamicIsDeleted(mDynamicBean.getUser_id(), mDynamicBean.getFeed_mark())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
            return;
        }
        if (mDynamicBean.getDigUserInfoList() == null) {
            mPresenter.getDetailAll(mDynamicBean.getFeed_id(), DEFAULT_PAGE_MAX_ID, mDynamicBean.getUser_id() + "");
        } else {
            allDataReady();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDynamicBean != null && mPresenter.checkCurrentDynamicIsDeleted(mDynamicBean.getUser_id(), mDynamicBean.getFeed_mark())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
            return;
        }
    }

    @Override
    protected MultiItemTypeAdapter<DynamicCommentBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        DynamicDetailCommentItem dynamicDetailCommentItem = new DynamicDetailCommentItem();
        dynamicDetailCommentItem.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicDetailCommentItem);
        DynamicCommentEmptyItem dynamicCommentEmptyItem = new DynamicCommentEmptyItem();
        adapter.addItemViewDelegate(dynamicCommentEmptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    public static DynamicDetailFragment initFragment(Bundle bundle) {
        DynamicDetailFragment dynamicDetailFragment = new DynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

    /**
     * 设置toolBar上面的用户头像,关注状态
     */
    private void setToolBarUser(DynamicBean dynamicBean) {
        // 设置用户头像，名称
        mTvToolbarCenter.setVisibility(View.VISIBLE);
        UserInfoBean userInfoBean = dynamicBean.getUserInfoBean();// 动态所属用户的信息
        mTvToolbarCenter.setText(userInfoBean.getName());
        Glide.with(getContext())
                .load(ImageUtils.imagePathConvert(dynamicBean.getUserInfoBean().getAvatar(), ImageZipConfig.IMAGE_26_ZIP))
                .bitmapTransform(new GlideCircleTransform(getContext()))
                .placeholder(R.mipmap.pic_default_portrait1)
                .error(R.mipmap.pic_default_portrait1)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        int headIconWidth = getResources().getDimensionPixelSize(R.dimen.headpic_for_assist);
                        resource.setBounds(0, 0, headIconWidth, headIconWidth);
                        mTvToolbarCenter.setCompoundDrawables(resource, null, null, null);
                    }
                });
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
    public void setDigHeadIcon(List<FollowFansBean> userInfoBeanList) {
        mDynamicBean.setDigUserInfoList(userInfoBeanList);
        updateCommentCountAndDig();
    }

    @Override
    public void upDateFollowFansState(int followState) {
        setToolBarRightFollowState(followState);
    }

    @Override
    public void initFollowState(FollowFansBean mFollowFansBean) {
        this.mFollowFansBean = mFollowFansBean;
        setToolBarRightFollowState(mFollowFansBean.getFollowState());
    }

    @Override
    public DynamicBean getCurrentDynamic() {
        return mDynamicBean;
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
        setToolBarUser(mDynamicBean);// 设置标题用户
        initBottomToolData(mDynamicBean);// 初始化底部工具栏数据
//        设置动态详情列表数据
        mDynamicDetailHeader.setDynamicDetial(mDynamicBean);
        updateCommentCountAndDig();
        onNetResponseSuccess(mDynamicBean.getComments(), false);
        if (mIsLookMore) {
            mRvList.scrollToPosition(1);
        }
        // 如果当前动态所属用户，就是当前用户，隐藏关注按钮
        long user_id = mDynamicBean.getUser_id();
        if (AppApplication.getmCurrentLoginAuth() != null && user_id == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            mTvToolbarRight.setVisibility(View.GONE);
        } else {
            // 获取用户关注状态
            mPresenter.getUserFollowState(user_id + "");
            mTvToolbarRight.setVisibility(View.VISIBLE);
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
    private void initBottomToolData(DynamicBean dynamicBean) {
        DynamicToolBean dynamicToolBean = dynamicBean.getTool();
        // 设置是否喜欢
        mDdDynamicTool.setItemIsChecked(dynamicToolBean.getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED, DynamicDetailMenuView.ITEM_POSITION_0);
        //设置是否收藏
        mDdDynamicTool.setItemIsChecked(dynamicToolBean.getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED, DynamicDetailMenuView.ITEM_POSITION_3);
    }

    /**
     * 设置底部工具栏的点击事件
     */
    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick(new DynamicDetailMenuView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int postion) {
                mDdDynamicTool.getTag(R.id.view_data);
                switch (postion) {
                    case DynamicDetailMenuView.ITEM_POSITION_0:
                        // 处理喜欢逻辑，包括服务器，数据库，ui
                        mPresenter.handleLike(mDynamicBean.getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED,
                                mDynamicBean.getFeed_id(), mDynamicBean.getTool());
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_1:
                        // 评论
                        showCommentView();
                        mReplyUserId = 0;
                        mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_2:
                        // 分享
                        mPresenter.shareDynamic();
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_3:
                        // 处理喜欢逻辑，包括服务器，数据库，ui
                        if (mDynamicBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()){
                            initMyDynamicPopupWindow(mDynamicBean,mDynamicBean.getTool().getIs_collection_feed() ==
                                    DynamicToolBean.STATUS_COLLECT_FEED_CHECKED);
                            mMyDynamicPopWindow.show();
                        }else{
                            initOtherDynamicPopupWindow(mDynamicBean,mDynamicBean.getTool().getIs_collection_feed() ==
                                    DynamicToolBean.STATUS_COLLECT_FEED_CHECKED);
                            mOtherDynamicPopWindow.show();
                        }
                        break;
                }
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
    private void setToolBarRightFollowState(int state) {
        mTvToolbarRight.setVisibility(View.VISIBLE);
        switch (state) {
            case FollowFansBean.UNFOLLOWED_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_follow), null);
                break;
            case FollowFansBean.IFOLLOWED_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed), null);
                break;
            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                mTvToolbarRight.setCompoundDrawables(null, null, UIUtils.getCompoundDrawables(getContext(), R.mipmap.detail_ico_followed_eachother), null);
                break;
            default:
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
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        position = position - 1;// 减去 header
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            if (mListDatas.get(position).getComment_id() != null) {
                initLoginOutPopupWindow(mListDatas.get(position).getComment_id(), position);
                mDeletCommentPopWindow.show();
            } else {
                return;
            }
        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            showCommentView();
            String contentHint = getString(R.string.default_input_hint);
            if (mListDatas.get(position).getReply_to_user_id() != mDynamicBean.getUser_id()) {
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
    private void initLoginOutPopupWindow(final long comment_id, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.important_for_theme))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mDeletCommentPopWindow.hide();
                        mPresenter.deleteComment(comment_id, commentPosition);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mDeletCommentPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    private void initOtherDynamicPopupWindow(final DynamicBean dynamicBean, boolean isCollected) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected?R.string.dynamic_list_uncollect_dynamic:R.string.dynamic_list_collect_dynamic))
//                .item2Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {// 收藏
                        mPresenter.handleCollect(dynamicBean);
                        mOtherDynamicPopWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {// 分享
                        mPresenter.shareDynamic();
                        mOtherDynamicPopWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mOtherDynamicPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param dynamicBean curent dynamic
     */
    private void initMyDynamicPopupWindow(final DynamicBean dynamicBean,boolean isCollected) {
        mMyDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected?R.string.dynamic_list_uncollect_dynamic:R.string.dynamic_list_collect_dynamic))
                .item2Str(getString(R.string.dynamic_list_delete_dynamic))
//                .item3Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {// 收藏
                        mPresenter.handleCollect(dynamicBean);
                        mMyDynamicPopWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {// 删除
                        EventBus.getDefault().post(dynamicBean,DYNAMIC_LIST_DELETE_UPDATE);
                        mMyDynamicPopWindow.hide();
                        getActivity().finish();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItem3Clicked() {// 分享
                        mPresenter.shareDynamic();
                        mMyDynamicPopWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {//取消
                        mMyDynamicPopWindow.hide();
                    }
                })
                .build();
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
}
