package com.zhiyicx.thinksnsplus.modules.circle.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListItemForZeroImage;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.ItemChannelDetailHeader;
import com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.GroupDynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.GroupDynamicEmptyItem;
import com.zhiyicx.thinksnsplus.widget.comment.GroupDynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.GroupDynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.ItemChannelDetailHeader.STATUS_RGB;
import static com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.ItemChannelDetailHeader.TOOLBAR_BLACK_ICON;
import static com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.ItemChannelDetailHeader.TOOLBAR_DIVIDER_RGB;
import static com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.ItemChannelDetailHeader.TOOLBAR_WHITE_ICON;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeFragment.GROUP_ID;
import static com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeFragment.SEND_OPTION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeFragment.TYPE;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */
public class ChannelDetailFragment extends TSListFragment<ChannelDetailContract.Presenter, GroupDynamicListBean> implements ChannelDetailContract
        .View, GroupDynamicListBaseItem.OnReSendClickListener,
        GroupDynamicNoPullRecycleView.OnCommentStateClickListener<GroupDynamicCommentListBean>, GroupDynamicListCommentView.OnCommentClickListener,
        GroupDynamicListBaseItem.OnMenuItemClickLisitener, GroupDynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        GroupDynamicListCommentView.OnMoreCommentClickListener, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener
        , PhotoSelectorImpl.IPhotoBackListener {

    public static final String CHANNEL_HEADER_INFO_DATA = "channel_header_info_data";

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
    protected LinearLayout mLlToolbarContainerParent;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.btn_send_dynamic)
    protected ImageView mBtnSendDynamic;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;

    private ItemChannelDetailHeader mItemChannelDetailHeader;
    //    private ChannelSubscripBean mChannelSubscripBean;// 从上一个页面传过来的频道信息
    private GroupInfoBean mGroupInfoBean;
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
        mItemChannelDetailHeader = new ItemChannelDetailHeader(getActivity(), mRvList, mHeaderAndFooterWrapper, mLlToolbarContainerParent,
                mPresenter);
        mItemChannelDetailHeader.initHeaderView(false, setHeadShow());

        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 255);
        //mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 255);
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 255);
        mItemChannelDetailHeader.setToolbarIconColor(Color.argb(255, TOOLBAR_BLACK_ICON[0],
                TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
        mIvSubscribBtn.setVisibility(View.GONE);// 隐藏订阅按钮
        initListener();
//        setOverScroll(false, null);
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mLlToolbarContainerParent;
    }

    @Override
    protected void initData() {
        mGroupInfoBean = getArguments().getParcelable(CHANNEL_HEADER_INFO_DATA);
        initSubscribState(mGroupInfoBean);
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
        super.initData();
    }

    @Override
    protected boolean setUseCenterLoadingAnimation() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
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
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        mPresenter.requestNetData(0L, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        setAdapter(adapter, new GroupDynamicListItemForZeroImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new GroupDynamicListItemForNineImage(getContext()));
        GroupDynamicEmptyItem emptyItem = new GroupDynamicEmptyItem();
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

    protected void setAdapter(MultiItemTypeAdapter adapter, GroupDynamicListBaseItem dynamicListBaseItem) {
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
    public void allDataReady(GroupZipBean groupZipBean) {
        closeLoadingView();
        mGroupInfoBean = groupZipBean.getGroupInfoBean();
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 0);
        //mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 0);
        mItemChannelDetailHeader.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 0);
        mItemChannelDetailHeader.setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0]
                , TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
        mItemChannelDetailHeader.setScrollListenter();
        // 状态栏文字设为白色
        //StatusBarUtils.statusBarDarkMode(mActivity);
        //initGroupState();// 尝试显示订阅按钮
        mItemChannelDetailHeader.initHeaderViewData(mGroupInfoBean);
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
    public long getGroupId() {
        if (mGroupInfoBean == null) {
            return -1L;
        }
        return mGroupInfoBean.getId();
    }

    @Override
    public void subscribChannelState(boolean stateSuccess, GroupInfoBean groupInfoBean, String message) {
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        if (stateSuccess && isJoined) {
            // 订阅成功
        } else if (!stateSuccess && isJoined) {
            // 订阅失败
            showSnackErrorMessage(message);
        } else if (stateSuccess && !isJoined) {
            // 取消订阅成功
        } else if (!stateSuccess && !isJoined) {
            // 取消订阅失败
        }
        if (stateSuccess) {
            // 操作成功，需要刷新订阅数量
            mItemChannelDetailHeader.refreshSubscribeData(groupInfoBean);
        }
//        initSubscribState(groupInfoBean);
    }

    @Override
    public void sendDynamic() {
        showSnackSuccessMessage(getString(R.string.had_send_dynamic_to_channel));
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<GroupDynamicListBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty() && getGroupId() >= 0) { // 增加空数据，用于显示占位图
            GroupDynamicListBean emptyData = new GroupDynamicListBean();
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
    public void onImageClick(ViewHolder holder, GroupDynamicListBean dynamicBean, int position) {
        if (!TouristConfig.DYNAMIC_BIG_PHOTO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }

        List<GroupDynamicListBean.ImagesBean> task = dynamicBean.getImages();
        List<ImageBean> imageBeanList = new ArrayList<>();
        ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        for (int i = 0; i < task.size(); i++) {
            int id = UIUtils.getResourceByName("siv_" + i, "id", getContext());
            ImageView imageView = holder.getView(id);
            ImageBean imageBean = new ImageBean();
            imageBean.setStorage_id(task.get(i).getFile_id());
            imageBean.setWidth(task.get(i).getWidth());
            imageBean.setHeight(task.get(i).getHeight());
            imageBeanList.add(imageBean);
            AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
            animationRectBeanArrayList.add(rect);
        }

        GalleryActivity.startToGallery(getContext(), position, imageBeanList,
                animationRectBeanArrayList);
    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        dataPosition -= mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
        mCurrentPostion = dataPosition;

        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            case 0: // 喜欢
                // 还未发送成功的动态列表不查看详情
                if ((!TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 1: // 评论
                if ((!TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
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
                Bitmap shareBitMap = null;
                try {
                    ImageView imageView = (ImageView) layoutManager.findViewByPosition
                            (dataPosition + mHeaderAndFooterWrapper.getHeadersCount()).findViewById(R.id.siv_0);
                    shareBitMap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), imageView
                            .getDrawable(), R.mipmap.icon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int user_id = mListDatas.get(dataPosition).getUser_id().intValue();
                int current_id = (int) AppApplication.getMyUserIdWithdefault();
                if (user_id == current_id) {
                    initMyDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, mListDatas.get(dataPosition)
                            .getHas_collection(), shareBitMap);
                    mMyDynamicPopWindow.show();
                } else {
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, mListDatas.get(dataPosition)
                            .getHas_collection(), shareBitMap);
                    mOtherDynamicPopWindow.show();
                }
                break;
            default:
                onItemClick(null, null, (dataPosition + 1)); // 加上 header
        }
    }

    @Override
    public void onReSendClick(int position) {
        position = position - mHeaderAndFooterWrapper.getHeadersCount();// 去掉 header
        initReSendDynamicPopupWindow(position);
        mReSendDynamicPopWindow.show();
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    @Override
    public void onCommentContentClick(GroupDynamicListBean dynamicBean, int position) {
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
        if (dynamicBean.getNew_comments().get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            initDeletCommentPopWindow(dynamicBean, mCurrentPostion, position);
            mDeletCommentPopWindow.show();
        } else {
            showCommentView();
            mReplyToUserId = dynamicBean.getNew_comments().get(position).getUser_id();
            String contentHint = getString(R.string.default_input_hint);
            if (dynamicBean.getNew_comments().get(position).getReply_to_user_id() != dynamicBean.getUser_id()) {
                contentHint = getString(R.string.reply, dynamicBean.getNew_comments().get(position).getCommentUser().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public void onMoreCommentClick(View view, GroupDynamicListBean dynamicBean) {
        int position = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
        goDynamicDetail(position, true);
    }

    @Override
    public void onCommentStateClick(GroupDynamicCommentListBean dynamicCommentBean, int position) {
        initReSendCommentPopupWindow(dynamicCommentBean, mListDatas.get(mPresenter.getCurrenPosiotnInDataList(dynamicCommentBean.getId())).getId());
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
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
        mListDatas.get(dataPosition).setHas_like(!mListDatas.get(dataPosition).getHas_like());
        mListDatas.get(dataPosition).setDiggs(!mListDatas.get(dataPosition).getHas_like() ?
                mListDatas.get(dataPosition).getDiggs() - 1 : mListDatas.get(dataPosition).getDiggs() + 1);
        refreshData(dataPosition);
        mPresenter.handleLike(mListDatas.get(dataPosition).getHas_like(),
                mListDatas.get(dataPosition).getGroup_id(), mListDatas.get(dataPosition).getId(), dataPosition);
    }

    /**
     * 收藏
     *
     * @param dataPosition
     */
    private void handleCollect(int dataPosition) {
        // 先更新界面，再后台处理
        mPresenter.handleCollect(mListDatas.get(dataPosition));
        boolean is_collection = mListDatas.get(dataPosition).getHas_collection();// 旧状态
        mListDatas.get(dataPosition).setHas_collection(!is_collection);
        refreshData(dataPosition);
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
    private void initDeletDynamicPopupWindow(final GroupDynamicListBean dynamicBean, final int position) {
        mDeletDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_dynamic))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mDeletDynamicPopWindow.hide();

                    mPresenter.deleteDynamic(dynamicBean, position);
                })
                .bottomClickListener(() -> mDeletDynamicPopWindow.hide())
                .build();
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final GroupDynamicCommentListBean commentBean, final long feed_id) {
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
                    //mCurrentPostion, mReplyToUserId, text
                    mPresenter.reSendComment(commentBean, feed_id);
                })
                .bottomClickListener(() -> mReSendCommentPopWindow.hide())
                .build();
    }

    /**
     * 初始化重发动态选择弹框
     */
    private void initReSendDynamicPopupWindow(final int position) {
        mReSendDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.resend))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mReSendDynamicPopWindow.hide();
                    mPresenter.reSendDynamic(position);
                })
                .bottomClickListener(() -> mReSendDynamicPopWindow.hide())
                .build();
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param dynamicBean     curent dynamic
     * @param dynamicPositon  dynamic comment position
     * @param commentPosition current comment position
     */
    private void initDeletCommentPopWindow(final GroupDynamicListBean dynamicBean, final int dynamicPositon, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteComment(dynamicBean, dynamicPositon, dynamicBean.getNew_comments().get(commentPosition).getId(),
                                commentPosition);
                    }, true);

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    private void goDynamicDetail(int position, boolean isLookMoreComment) {
        // 还未发送成功的动态列表不查看详情
        if (mListDatas.get(position).getId() == null || mListDatas.get(position).getId() == 0) {
            return;
        }
        mPresenter.handleViewCount(mListDatas.get(position).getId(), position);
        Intent intent = new Intent(getActivity(), GroupDynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mListDatas.get(position));
        bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, position);
        bundle.putBoolean(LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initToolBar() {
        if (!setUseStatusView()) {
            // toolBar 设置状态栏高度的 marginTop
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height) + DeviceUtils.getStatuBarHeight(getContext()) + getResources
                    ().getDimensionPixelSize(R.dimen.divider_line);
            CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            mLlToolbarContainerParent.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        if (setUseSatusbar()) {
            return 0;
        }
        return super.getstatusbarAndToolbarHeight();
    }

    private void initListener() {
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mIlvComment.setVisibility(View.GONE);
                    mIlvComment.clearFocus();
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                    mVShadow.setVisibility(View.GONE);

                });

        RxView.clicks(mIvSubscribBtn)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (!TouristConfig.CHEENAL_CAN_SUBSCRIB && mPresenter.handleTouristControl
                            ()) {
                        return;
                    }
                    // 进行订阅
                    mPresenter.handleGroupSubscrib(mGroupInfoBean);
                    // 处理订阅ui逻辑：先处理ui,并未可订阅状态的ui，不可点击发送动态
                    mIvSubscribBtn.setVisibility(View.GONE);
                    mBtnSendDynamic.setVisibility(View.VISIBLE);
                    setBtnSendDynamicClickState(false);
                });

        RxView.clicks(mBtnSendDynamic)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    // 跳转到发送动态页面
                    clickSendPhotoTextDynamic();
                });
        longClickSendTextDynamic();
        mIlvComment.setOnSendClickListener(this);

    }

    /**
     * 处理订阅状态对应的不同逻辑:
     */
    private void initSubscribState(GroupInfoBean groupInfoBean) {
        if (groupInfoBean == null) {
            return;
        }
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        if (isJoined) {
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

    /**
     * 处理订阅状态对应的不同逻辑:
     */
    private void initGroupState() {
        Boolean isJoined = mGroupInfoBean.getIs_member() == 1;
        if (isJoined) {
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
        Intent intent = new Intent(getActivity(), SelectDynamicTypeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(GROUP_ID, mGroupInfoBean.getId());
        bundle.putInt(TYPE, SendDynamicDataBean.GROUP_DYNAMIC);
        intent.putExtra(SEND_OPTION, bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.zoom_in, 0);
//        mPhotoSelector.getPhotoListFromSelector(9, null);
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
        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.GROUP_DYNAMIC);
        sendDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
        sendDynamicDataBean.setDynamicPrePhotos(photoList);
        sendDynamicDataBean.setDynamicChannlId(mGroupInfoBean.getId());
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
    private void initOtherDynamicPopupWindow(final GroupDynamicListBean dynamicBean, int position, boolean isCollected, final
    Bitmap shareBitmap) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item2Str(getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item1Str(getString(R.string.dynamic_list_share_dynamic))
//                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {// 收藏
                    handleCollect(position);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .item1ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(dynamicBean, shareBitmap);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initMyDynamicPopupWindow(final GroupDynamicListBean dynamicBean, final int position, boolean isCollected,
                                          final Bitmap shareBitMap) {
        Long feed_id = dynamicBean.getId();
        boolean feedIdIsNull = feed_id == null || feed_id == 0;
        mMyDynamicPopWindow = ActionPopupWindow.builder()
                .item2Str(getString(feedIdIsNull ? R.string.empty : isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string
                        .dynamic_list_collect_dynamic))
                .item3Str(getString(R.string.dynamic_list_delete_dynamic))
                .item1Str(getString(feedIdIsNull ? R.string.empty : R.string.dynamic_list_share_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {// 收藏
                    mMyDynamicPopWindow.hide();
                    handleCollect(position);
                    showBottomView(true);
                })
                .item3ClickListener(() -> {// 删除
                    mMyDynamicPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.dynamic_list_delete_dynamic), () -> {
                        mPresenter.deleteDynamic(dynamicBean, position);
                        showBottomView(true);
                    }, true);

                })
                .item1ClickListener(() -> {// 分享
                    mPresenter.shareDynamic(dynamicBean, shareBitMap);
                    mMyDynamicPopWindow.hide();
                })
                .bottomClickListener(() -> {//取消
                    mMyDynamicPopWindow.hide();
                    showBottomView(true);
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
        mBtnSendDynamic.setOnLongClickListener(v -> {
            // 跳转到发送动态页面
            SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
            sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.GROUP_DYNAMIC);
            sendDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
            sendDynamicDataBean.setDynamicChannlId(mGroupInfoBean.getId());
            SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
            return true;
        });
    }

    protected boolean setHeadShow() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissPop(mDeletCommentPopWindow);
        dismissPop(mDeletDynamicPopWindow);
        dismissPop(mReSendCommentPopWindow);
        dismissPop(mReSendDynamicPopWindow);
        dismissPop(mOtherDynamicPopWindow);
        dismissPop(mMyDynamicPopWindow);
    }

}
