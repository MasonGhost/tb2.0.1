package com.zhiyicx.thinksnsplus.modules.channel.detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.ZoomView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForZeroImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.DynamicEmptyItem;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.TOOLBAR_RIGHT_WHITE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.STATUS_RGB;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.TOOLBAR_BLACK_ICON;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.TOOLBAR_DIVIDER_RGB;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.TOOLBAR_RGB;
import static com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader.TOOLBAR_WHITE_ICON;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailFragment extends TSListFragment<ChannelDetailContract.Presenter, DynamicBean> implements ChannelDetailContract.View, DynamicListBaseItem.OnReSendClickListener,
        DynamicNoPullRecycleView.OnCommentStateClickListener, DynamicListCommentView.OnCommentClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        DynamicListCommentView.OnMoreCommentClickListener, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener
        , PhotoSelectorImpl.IPhotoBackListener {

    public static final String CHANNEL_HEADER_INFO_DATA = "channel_header_info_data";

    @BindView(R.id.tv_top_tip_text)
    TextView mTvTopTipText;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_channel_name)
    TextView mTvChannelName;
    @BindView(R.id.iv_subscrib_btn)
    TextView mIvSubscribBtn;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    @BindView(R.id.v_horizontal_line)
    View mVHorizontalLine;
    @BindView(R.id.ll_toolbar_container_parent)
    LinearLayout mLlToolbarContainerParent;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.btn_send_dynamic)
    ImageView mBtnSendDynamic;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;

    private ItemChannelDetailHeader mItemChannelDetailHeader;
    private ChannelSubscripBean mChannelSubscripBean;// 从上一个页面传过来的频道信息
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDeletDynamicPopWindow;
    private ActionPopupWindow mReSendCommentPopWindow;
    private ActionPopupWindow mReSendDynamicPopWindow;

    private ActionPopupWindow mOtherDynamicPopWindow;
    private ActionPopupWindow mMyDynamicPopWindow;

    private int mCurrentPostion;// 当前评论的动态位置
    private long mReplyToUserId;// 被评论者的 id
    private PhotoSelectorImpl mPhotoSelector;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        initToolBar();
        initPhotoPicker();
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mItemChannelDetailHeader = new ItemChannelDetailHeader(getActivity(), mRvList, mHeaderAndFooterWrapper, mLlToolbarContainerParent, mPresenter);
        mItemChannelDetailHeader.initHeaderView(false);

        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 255);
        //mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 255);
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 255);
        mItemChannelDetailHeader.setToolbarIconColor(Color.argb(255, TOOLBAR_BLACK_ICON[0],
                TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
        mIvSubscribBtn.setVisibility(View.GONE);// 隐藏订阅按钮
        initListener();
        setOverScroll(false, null);
    }

    @Override
    protected View getLeftViewOfMusicWindow() {
        return mIvSubscribBtn;
    }

    @Override
    protected void initData() {
        mChannelSubscripBean = getArguments().getParcelable(CHANNEL_HEADER_INFO_DATA);
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
        super.initData();
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
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        mPresenter.requestNetData(0l, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        setAdapter(adapter, new DynamicListItemForZeroImage(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        DynamicEmptyItem emptyItem = new DynamicEmptyItem();
        adapter.addItemViewDelegate(emptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_channel_detail;
    }

    public static ChannelDetailFragment newInstance(Bundle bundle) {
        ChannelDetailFragment channelDetailFragment = new ChannelDetailFragment();
        channelDetailFragment.setArguments(bundle);
        return channelDetailFragment;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        dynamicListBaseItem.setOnMoreCommentClickListener(this);
        dynamicListBaseItem.setOnCommentClickListener(this);
        dynamicListBaseItem.setOnCommentStateClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }

    @Override
    public void allDataReady() {
        closeLoadingView();
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 0);
        //mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 0);
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 0);
        mItemChannelDetailHeader.setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0]
                , TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
        mItemChannelDetailHeader.setScrollListenter();
        // 状态栏文字设为白色
        //StatusBarUtils.statusBarDarkMode(mActivity);
        initSubscribState(mChannelSubscripBean);// 尝试显示订阅按钮
        mItemChannelDetailHeader.initHeaderViewData(mChannelSubscripBean);
    }

    @Override
    public void loadAllError() {
        showLoadViewLoadError();
        // 网络数据请求结束
        mItemChannelDetailHeader.refreshEnd();
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
        // 网络数据请求结束
        mItemChannelDetailHeader.refreshEnd();
    }

    @Override
    public long getChannelId() {
        return mChannelSubscripBean.getId();
    }

    @Override
    public void subscribChannelState(boolean stateSuccess, ChannelSubscripBean channelSubscripBean, String message) {
        boolean subscribState = channelSubscripBean.getChannelSubscriped();// 操作后的订阅状态
        if (stateSuccess && subscribState) {
            // 订阅成功
        } else if (!stateSuccess && subscribState) {
            // 订阅失败
            showSnackErrorMessage(message);
        } else if (stateSuccess && !subscribState) {
            // 取消订阅成功
        } else if (!stateSuccess && !subscribState) {
            // 取消订阅失败
        }
        if (stateSuccess) {
            // 操作成功，需要刷新订阅数量
            mItemChannelDetailHeader.refreshSubscribeData(channelSubscripBean.getChannelInfoBean());
        }
        initSubscribState(channelSubscripBean);
    }

    @Override
    public void sendDynamic() {
        showSnackSuccessMessage(getString(R.string.had_send_dynamic_to_channel));
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<DynamicBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            DynamicBean emptyData = new DynamicBean();
            data.add(emptyData);
        }
        super.onNetResponseSuccess(data, isLoadMore);
        // 网络数据请求结束
        mItemChannelDetailHeader.refreshEnd();
    }

    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        super.onResponseError(throwable, isLoadMore);
        // 网络数据请求结束
        mItemChannelDetailHeader.refreshEnd();
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        getActivity().finish();
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mCurrentPostion, mReplyToUserId, text);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position) {
        List<ImageBean> imageBeanList = dynamicBean.getFeed().getStorages();
        ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<AnimationRectBean>();
        for (int i = 0; i < imageBeanList.size(); i++) {
            int id = UIUtils.getResourceByName("siv_" + i, "id", getContext());
            ImageView imageView = holder.getView(id);
            AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
            animationRectBeanArrayList.add(rect);
            LogUtils.i("dynamic_" + i + rect.toString());
        }

        GalleryActivity.startToGallery(getContext(), position, imageBeanList, animationRectBeanArrayList);
    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        dataPosition = dataPosition - 1;// 减去 header
        mCurrentPostion = dataPosition;
        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            case 0: // 喜欢
                // 还未发送成功的动态列表不查看详情
                if (mListDatas.get(dataPosition).getFeed_id() == null || mListDatas.get(dataPosition).getFeed_id() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 1: // 评论
                // 还未发送成功的动态列表不查看详情
                if (mListDatas.get(dataPosition).getFeed_id() == null || mListDatas.get(dataPosition).getFeed_id() == 0) {
                    return;
                }
                showCommentView();
                mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
                mCurrentPostion = dataPosition;
                mReplyToUserId = 0;// 0 代表评论动态
                break;

            case 2: // 浏览
                onItemClick(null, null, (dataPosition + 1)); // 加上 header
                break;

            case 3: // 更多
                dataPosition += 1;
                if (mListDatas.get(dataPosition).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
                    initMyDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, mListDatas.get(dataPosition)
                            .getTool().getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED);
                    mMyDynamicPopWindow.show();
                } else {
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), mListDatas.get(dataPosition)
                            .getTool().getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED);
                    mOtherDynamicPopWindow.show();
                }
                break;
            default:
                onItemClick(null, null, (dataPosition + 1)); // 加上 header
        }
    }

    @Override
    public void onReSendClick(int position) {
        position = position - 1;// 去掉 header
        initReSendDynamicPopupWindow(position);
        mReSendDynamicPopWindow.show();
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    @Override
    public void onCommentContentClick(DynamicBean dynamicBean, int position) {
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        if (dynamicBean.getComments().get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            initDeletCommentPopWindow(dynamicBean, mCurrentPostion, position);
            mDeletCommentPopWindow.show();
        } else {
            showCommentView();
            mReplyToUserId = dynamicBean.getComments().get(position).getUser_id();
            String contentHint = getString(R.string.default_input_hint);
            if (dynamicBean.getComments().get(position).getReply_to_user_id() != dynamicBean.getUser_id()) {
                contentHint = getString(R.string.reply, dynamicBean.getComments().get(position).getCommentUser().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public void onMoreCommentClick(View view, DynamicBean dynamicBean) {
        int position = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        goDynamicDetail(position, true);
    }

    @Override
    public void onCommentStateClick(DynamicCommentBean dynamicCommentBean, int position) {
        initReSendCommentPopupWindow(dynamicCommentBean, mListDatas.get(mPresenter.getCurrenPosiotnInDataList(dynamicCommentBean.getFeed_mark())).getFeed_id());
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        position = position - 1;// 减去 header
        mCurrentPostion = position;
        goDynamicDetail(position, false);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /**
     * 喜欢
     *
     * @param dataPosition
     */
    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
        mListDatas.get(dataPosition).getTool().setIs_digg_feed(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ? DynamicToolBean.STATUS_DIGG_FEED_CHECKED : DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED);
        mListDatas.get(dataPosition).getTool().setFeed_digg_count(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ?
                mListDatas.get(dataPosition).getTool().getFeed_digg_count() - 1 : mListDatas.get(dataPosition).getTool().getFeed_digg_count() + 1);
        refreshData();
        mPresenter.handleLike(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED,
                mListDatas.get(dataPosition).getFeed().getFeed_id(), dataPosition);
    }

    private void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    /**
     * 初始化动态删除选择弹框
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initDeletDynamicPopupWindow(final DynamicBean dynamicBean, final int position) {
        mDeletDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_dynamic))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mDeletDynamicPopWindow.hide();
                        mPresenter.deleteDynamic(dynamicBean, position);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mDeletDynamicPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final DynamicCommentBean commentBean, final long feed_id) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mReSendCommentPopWindow.hide();
                        mPresenter.reSendComment(commentBean, feed_id);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mReSendCommentPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化重发动态选择弹框
     */
    private void initReSendDynamicPopupWindow(final int position) {
        mReSendDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_dynamic))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mReSendDynamicPopWindow.hide();
                        mListDatas.get(position).setState(DynamicBean.SEND_ING);
                        refreshData();
                        mPresenter.reSendDynamic(position);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mReSendDynamicPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param dynamicBean     curent dynamic
     * @param dynamicPositon  dynamic comment position
     * @param commentPosition current comment position
     */
    private void initDeletCommentPopWindow(final DynamicBean dynamicBean, final int dynamicPositon, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        mDeletCommentPopWindow.hide();
                        mPresenter.deleteComment(dynamicBean, dynamicPositon, dynamicBean.getComments().get(commentPosition).getComment_id(), commentPosition);
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

    private void goDynamicDetail(int position, boolean isLookMoreComment) {
        // 还未发送成功的动态列表不查看详情
        if (mListDatas.get(position).getFeed_id() == null || mListDatas.get(position).getFeed_id() == 0) {
            return;
        }
        mPresenter.handleViewCount(mListDatas.get(position).getFeed_id(), position);
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mListDatas.get(position));
        bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, position);
        bundle.putBoolean(LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initToolBar() {
        // toolBar 设置状态栏高度的 marginTop
        int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height) + DeviceUtils.getStatuBarHeight(getContext()) + getResources().getDimensionPixelSize(R.dimen.divider_line);
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        mLlToolbarContainerParent.setLayoutParams(layoutParams);

    }

    private void initListener() {
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mIlvComment.setVisibility(View.GONE);
                        mIlvComment.clearFocus();
                        DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                        mVShadow.setVisibility(View.GONE);

                    }
                });

        RxView.clicks(mIvSubscribBtn)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 进行订阅
                        mPresenter.handleChannelSubscrib(mChannelSubscripBean);
                        // 处理订阅ui逻辑：先处理ui,并未可订阅状态的ui，不可点击发送动态
                        mIvSubscribBtn.setVisibility(View.GONE);
                        mBtnSendDynamic.setVisibility(View.VISIBLE);
                        setBtnSendDynamicClickState(false);
                    }
                });

        RxView.clicks(mBtnSendDynamic)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 跳转到发送动态页面
                        clickSendPhotoTextDynamic();
                    }
                });
        longClickSendTextDynamic();
        mIlvComment.setOnSendClickListener(this);

    }

    /**
     * 处理订阅状态对应的不同逻辑:
     */
    private void initSubscribState(ChannelSubscripBean channelSubscripBean) {
        Boolean subscribState = channelSubscripBean.getChannelSubscriped();
        if (subscribState) {
            // 订阅后，显示发送动态按钮，隐藏订阅按钮
            mIvSubscribBtn.setVisibility(View.GONE);
            mBtnSendDynamic.setVisibility(View.VISIBLE);
            setBtnSendDynamicClickState(true);
        } else {
            // 未订阅，隐藏发送动态的按钮，显示订阅按钮
            mIvSubscribBtn.setVisibility(View.VISIBLE);
            mBtnSendDynamic.setVisibility(View.GONE);
            setBtnSendDynamicClickState(false);
        }
    }

    private void setBtnSendDynamicClickState(boolean clickable) {
        mBtnSendDynamic.setClickable(clickable);
    }

    /**
     * 点击动态发送按钮，进入文字图片的动态发布
     */
    private void clickSendPhotoTextDynamic() {
        mPhotoSelector.getPhotoListFromSelector(9, null);
    }

    private void initPhotoPicker() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 跳转到发送动态页面
        SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.CHANNEL_DYNAMIC);
        sendDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
        sendDynamicDataBean.setDynamicPrePhotos(photoList);
        sendDynamicDataBean.setDynamicChannlId(mChannelSubscripBean.getId());
        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    private void initOtherDynamicPopupWindow(final DynamicBean dynamicBean, boolean isCollected) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item2Str(getString(R.string.dynamic_list_share_dynamic))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
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
                        showBottomView(true);
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {// 分享
                        mPresenter.shareDynamic(dynamicBean);
                        mOtherDynamicPopWindow.hide();
                        showBottomView(true);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mOtherDynamicPopWindow.hide();
                        showBottomView(true);
                    }
                })
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initMyDynamicPopupWindow(final DynamicBean dynamicBean, final int position, boolean isCollected) {
        mMyDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item2Str(getString(R.string.dynamic_list_delete_dynamic))
                .item3Str(getString(R.string.dynamic_list_share_dynamic))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {// 收藏
                        mMyDynamicPopWindow.hide();
                        showBottomView(true);
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItem2Clicked() {// 删除
                        mMyDynamicPopWindow.hide();
                        mPresenter.deleteDynamic(dynamicBean, position);
                        showBottomView(true);
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItem3Clicked() {// 分享
                        mMyDynamicPopWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {//取消
                        mMyDynamicPopWindow.hide();
                        showBottomView(true);
                    }
                })
                .build();
    }

    private void showBottomView(boolean isShow) {
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
     * 长按动态发送按钮，进入纯文字的动态发布
     */
    private void longClickSendTextDynamic() {
        mBtnSendDynamic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 跳转到发送动态页面
                SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
                sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.CHANNEL_DYNAMIC);
                sendDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                sendDynamicDataBean.setDynamicChannlId(mChannelSubscripBean.getId());
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
                return true;
            }
        });
    }
}
