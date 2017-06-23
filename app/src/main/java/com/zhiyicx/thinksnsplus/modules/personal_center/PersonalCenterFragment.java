package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
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
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListForZeroImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterDynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
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
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository.checkHelperUrl;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;
import static com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem.STATUS_RGB;
import static com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem.TOOLBAR_BLACK_ICON;
import static com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem.TOOLBAR_DIVIDER_RGB;
import static com.zhiyicx.thinksnsplus.modules.personal_center.adapter.PersonalCenterHeaderViewItem.TOOLBAR_WHITE_ICON;

/**
 * @author LiuChao
 * @describe 用户个人中心页面
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterFragment extends TSListFragment<PersonalCenterContract.Presenter, DynamicDetailBeanV2> implements PersonalCenterContract.View, DynamicListBaseItem.OnReSendClickListener,
        DynamicNoPullRecycleView.OnCommentStateClickListener, DynamicListCommentView.OnCommentClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        DynamicListCommentView.OnMoreCommentClickListener, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, PhotoSelectorImpl.IPhotoBackListener {

    public static final String PERSONAL_CENTER_DATA = "personal_center_data";

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_more)
    ImageView mIvMore;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    @BindView(R.id.tv_follow)
    TextView mTvFollow;
    @BindView(R.id.ll_follow_container)
    LinearLayout mLlFollowContainer;
    @BindView(R.id.ll_chat_container)
    LinearLayout mLlChatContainer;
    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;
    @BindView(R.id.ll_toolbar_container_parent)
    LinearLayout mLlToolbarContainerParent;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;


    private PersonalCenterHeaderViewItem mPersonalCenterHeaderViewItem;
    // 关注状态
    private FollowFansBean mFollowFansBean;
    // 上一个页面传过来的用户信息
    private UserInfoBean mUserInfoBean;
    private PhotoSelectorImpl mPhotoSelector;
    private String imagePath;// 上传的封面图片的本地路径
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDeletDynamicPopWindow;
    private ActionPopupWindow mReSendCommentPopWindow;
    private ActionPopupWindow mReSendDynamicPopWindow;
    private int mCurrentPostion;// 当前评论的动态位置
    private long mReplyToUserId;// 被评论者的 id


    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        // 初始化图片选择器
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_RCTANGLE))
                .build().photoSelectorImpl();
        initToolBar();
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mPersonalCenterHeaderViewItem = new PersonalCenterHeaderViewItem(getActivity(), mPhotoSelector, mRvList, mHeaderAndFooterWrapper, mLlToolbarContainerParent);
        mPersonalCenterHeaderViewItem.initHeaderView(false);
        mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 255);
        //mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 255);
        mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 255);
        mPersonalCenterHeaderViewItem.setToolbarIconColor(Color.argb(255, TOOLBAR_BLACK_ICON[0],
                TOOLBAR_BLACK_ICON[1], TOOLBAR_BLACK_ICON[2]));
        mIvMore.setVisibility(View.GONE);
        setOverScroll(false, null);
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected View getLeftViewOfMusicWindow() {
        return mIvMore;
    }

    private void initListener() {
        // 添加关注点击事件
        RxView.clicks(mLlFollowContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 表示第一次进入界面加载正确的关注状态，后续才能进行关注操作
                        if (mFollowFansBean != null) {
                            mPresenter.handleFollow(mFollowFansBean);
                        }
                    }
                });
        // 添加聊天点击事件
        RxView.clicks(mLlChatContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        MessageItemBean messageItemBean = new MessageItemBean();
                        messageItemBean.setUserInfo(mUserInfoBean);
                        Intent to = new Intent(getActivity(), ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
                        to.putExtras(bundle);
                        startActivity(to);
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
                        mVShadow.setVisibility(View.GONE);

                    }
                });

        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    protected boolean setStatusbarGrey() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mUserInfoBean.getUser_id());
    }

    @Override
    protected List<DynamicDetailBeanV2> requestCacheData(Long maxId, boolean isLoadMore) {
//        return mPresenter.requestCacheData(maxId, isLoadMore, mUserInfoBean.getUser_id());
        return null;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        if (userInfoBean.getUser_id() != mUserInfoBean.getUser_id()) {// 如果当前页面的主页已经是当前这个人了，不就用跳转了
            PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
        }
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_personal_center;
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
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        requestData();
    }

    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        // 按照添加顺序，先判断成功后，后面的item就不会继续判断了，类似if else
        setAdapter(adapter, new PersonalCenterDynamicListForZeroImage(getContext()));
        //setAdapter(adapter, new PersonalCenterDynamicListBaseItem(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new PersonalCenterDynamicListItemForNineImage(getContext()));
        DynamicEmptyItem emptyItem = new DynamicEmptyItem();
        adapter.addItemViewDelegate(emptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected void initData() {
        mUserInfoBean = getArguments().getParcelable(PERSONAL_CENTER_DATA);
        if (mUserInfoBean != null) {
            requestData();
        }
        super.initData();
    }

    /**
     * 获取服务器数据
     */
    private void requestData() {
        // 获取个人主页用户信息，显示在headerView中
        mPresenter.setCurrentUserInfo(mUserInfoBean.getUser_id());
        // 获取关注状态
        mPresenter.initFollowState(mUserInfoBean.getUser_id());
        // 获取动态列表
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false, mUserInfoBean.getUser_id());
    }

    @Override
    public void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position) {
        if (!TouristConfig.DYNAMIC_BIG_PHOTO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
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
        Bitmap shareBitMap = null;
        try {
            ImageView imageView = (ImageView) layoutManager.findViewByPosition(dataPosition + 1).findViewById(R.id.siv_0);
            shareBitMap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), imageView.getDrawable(), R.mipmap.icon_256);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            case 0: // 喜欢
                // 还未发送成功的动态列表不查看详情
                if (mListDatas.get(dataPosition).getId() == null || mListDatas.get(dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 1: // 评论
                // 还未发送成功的动态列表不查看详情
                if (mListDatas.get(dataPosition).getId() == null || mListDatas.get(dataPosition).getId() == 0) {
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
//                initDeletDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, shareBitMap);
                mDeletDynamicPopWindow.show();
                break;
            default:
                onItemClick(null, null, (dataPosition + 1)); // 加上 header
        }
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
    public void allDataReady() {
        closeLoadingView();
        mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent, STATUS_RGB, 0);
        // mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.rl_toolbar_container), TOOLBAR_RGB, 0);
        mPersonalCenterHeaderViewItem.setViewColorWithAlpha(mLlToolbarContainerParent.findViewById(R.id.v_horizontal_line), TOOLBAR_DIVIDER_RGB, 0);
        mPersonalCenterHeaderViewItem.setToolbarIconColor(Color.argb(255, TOOLBAR_WHITE_ICON[0]
                , TOOLBAR_WHITE_ICON[1], TOOLBAR_WHITE_ICON[2]));
        mIvMore.setVisibility(View.VISIBLE);
        mPersonalCenterHeaderViewItem.setScrollListenter();
        // 状态栏文字设为白色
        //StatusBarUtils.statusBarDarkMode(mActivity);
        initListener();
        // 进入页面尝试设置头部信息
        setHeaderInfo(mUserInfoBean);
    }

    @Override
    public void loadAllError() {
        showLoadViewLoadError();
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mCurrentPostion, mReplyToUserId, text);
    }

    @Override
    public void onReSendClick(int position) {
        position = position - 1;// 去掉 header
        initReSendDynamicPopupWindow(position);
        mReSendDynamicPopWindow.show();
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

    @OnClick({R.id.iv_back, R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.iv_more:
                mPresenter.shareUserInfo(mUserInfoBean);

                break;
        }
    }

    @Override
    public void setHeaderInfo(UserInfoBean userInfoBean) {
        if (userInfoBean != null) {
            this.mUserInfoBean = userInfoBean;
            setBottomVisible(userInfoBean.getUser_id());
            mPersonalCenterHeaderViewItem.initHeaderViewData(userInfoBean);
        }
    }

    @Override
    public void setFollowState(FollowFansBean followFansBean) {
        mFollowFansBean = followFansBean;
        setBottomFollowState(followFansBean.getFollowState());
    }

    @Override
    public void setUpLoadCoverState(boolean upLoadState, int taskId) {
        if (upLoadState) {
            // 封面图片上传成功
            // 通知服务器，更改用户信息
            mPresenter.changeUserCover(mUserInfoBean, taskId, imagePath);
        } else {
            TSnackbar.make(mSnackRootView, R.string.cover_uploadFailure, TSnackbar.LENGTH_SHORT)
                    .setPromptThemBackground(Prompt.ERROR)
                    .show();
        }
    }

    @Override
    public void setChangeUserCoverState(boolean changeSuccess) {
        TSnackbar.make(mSnackRootView, changeSuccess ? R.string.cover_change_success : R.string.cover_change_failure, TSnackbar.LENGTH_SHORT)
                .setPromptThemBackground(changeSuccess ? Prompt.SUCCESS : Prompt.ERROR)
                .show();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        // 选择图片完毕后，开始上传封面图片
        ImageBean imageBean = photoList.get(0);
        imagePath = imageBean.getImgUrl();
        // 上传本地图片
        mPresenter.uploadUserCover(imagePath);
        // 加载本地图片
        mPersonalCenterHeaderViewItem.upDateUserCover(imagePath);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onCommentStateClick(DynamicCommentBean dynamicCommentBean, int position) {
        initReSendCommentPopupWindow(dynamicCommentBean, mListDatas.get(mPresenter.getCurrenPosiotnInDataList(dynamicCommentBean.getFeed_mark())).getId());
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    @Override
    public void onCommentContentClick(DynamicDetailBeanV2 dynamicBean, int position) {
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        if (dynamicBean.getComments().get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
//            initDeletCommentPopWindow(dynamicBean, mCurrentPostion, position);
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

    private void initToolBar() {
        // toolBar 设置状态栏高度的 marginTop
        int height = getResources().getDimensionPixelSize(R.dimen.toolbar_height) + DeviceUtils.getStatuBarHeight(getContext()) + getResources().getDimensionPixelSize(R.dimen.divider_line);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        mLlToolbarContainerParent.setLayoutParams(layoutParams);
    }

    /**
     * 设置底部 view 的关注状态
     */
    private void setBottomFollowState(int state) {
        switch (state) {
            case FollowFansBean.UNFOLLOWED_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_follow), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
                mTvFollow.setText(R.string.follow);
                break;
            case FollowFansBean.IFOLLOWED_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_followed), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mTvFollow.setText(R.string.followed);
                break;
            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                mTvFollow.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), R.mipmap.ico_me_followed_eachother), null, null, null);
                mTvFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
                mTvFollow.setText(R.string.followed_eachother);
                break;
            default:
        }
    }

    /**
     * 跳转到当前的个人中心页面
     */
    public static void startToPersonalCenter(Context context, UserInfoBean userInfoBean) {
        String tsHelperUrl = checkHelperUrl(context, userInfoBean.getUser_id());
        if (!TextUtils.isEmpty(tsHelperUrl)) {
            CustomWEBActivity.startToWEBActivity(context, tsHelperUrl);
        } else {
            Intent intent = new Intent(context, PersonalCenterActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, userInfoBean);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }

    }

    /**
     * 设置底部 view 的可见性;如果进入了当前登录用户的主页，需要隐藏底部状态栏
     *
     * @param currentUserID
     */
    private void setBottomVisible(long currentUserID) {
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        mLlBottomContainer.setVisibility((authBean != null && authBean.getUser_id() == currentUserID) ? View.GONE : View.VISIBLE);
    }

    public static PersonalCenterFragment initFragment(Bundle bundle) {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        personalCenterFragment.setArguments(bundle);
        return personalCenterFragment;
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


    /**
     * 喜欢
     *
     * @param dataPosition
     */
    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
//        mListDatas.get(dataPosition).getTool().setIs_digg_feed(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ? DynamicToolBean.STATUS_DIGG_FEED_CHECKED : DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED);
//        mListDatas.get(dataPosition).getTool().setFeed_digg_count(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ?
//                mListDatas.get(dataPosition).getTool().getFeed_digg_count() - 1 : mListDatas.get(dataPosition).getTool().getFeed_digg_count() + 1);
        refreshData();
//        mPresenter.handleLike(mListDatas.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED,
//                mListDatas.get(dataPosition).getFeed().getFeed_id(), dataPosition);
    }


    private void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }


    private void goDynamicDetail(int position, boolean isLookMoreComment) {
        // 还未发送成功的动态列表不查看详情
        if (mListDatas.get(position).getId() == null || mListDatas.get(position).getId() == 0) {
            return;
        }
        mPresenter.handleViewCount(mListDatas.get(position).getId(), position);
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mListDatas.get(position));
        bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, position);
        bundle.putBoolean(LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        startActivity(intent);
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
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mDeletCommentPopWindow.hide();
                        mPresenter.deleteComment(dynamicBean, dynamicPositon, dynamicBean.getComments().get(commentPosition).getComment_id(), commentPosition);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mDeletCommentPopWindow.hide();
                    }
                })
                .build();
    }

    /**
     * 初始化动态删除选择弹框
     *
     * @param dynamicBean curent dynamic
     * @param position    curent dynamic postion
     */
    private void initDeletDynamicPopupWindow(final DynamicBean dynamicBean, final int position, final Bitmap shareBitmap) {
        boolean isCollected = dynamicBean.getTool().getIs_collection_feed() == DynamicToolBean.STATUS_COLLECT_FEED_CHECKED;
        Long feed_id = dynamicBean.getFeed_id();
        boolean feedIdIsNull = feed_id == null || feed_id == 0;
        mDeletDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(feedIdIsNull ? R.string.empty : (isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic)))
                .item2Str(getString(R.string.dynamic_list_delete_dynamic))
                .item3Str(getString(feedIdIsNull ? R.string.empty : R.string.dynamic_list_share_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPresenter.handleCollect(dynamicBean);
                        mDeletDynamicPopWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mDeletDynamicPopWindow.hide();
                        updateDynamicCounts(-1);
                        mPresenter.deleteDynamic(dynamicBean, position);
                        mDeletDynamicPopWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mPresenter.shareDynamic(dynamicBean, shareBitmap);
                        mDeletDynamicPopWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mDeletDynamicPopWindow.hide();
                    }
                })
                .build();
    }

    @Override
    public void updateDynamicCounts(int changeNums) {
        int currenDynamicCounts = 0;
        try {
            currenDynamicCounts = Integer.parseInt(mUserInfoBean.getFeeds_count());
        } catch (Exception e) {
            e.printStackTrace();
        }
        currenDynamicCounts += changeNums;
        if (currenDynamicCounts < 0) {
            currenDynamicCounts = 0;
        }
        mUserInfoBean.setFeeds_count(String.valueOf(currenDynamicCounts));
        mPersonalCenterHeaderViewItem.upDateDynamicNums(currenDynamicCounts);
    }

    @Override
    public Bitmap getUserHeadPic() {
        if (mPersonalCenterHeaderViewItem.getHeadView() == null) {
            return null;
        }
        return ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), mPersonalCenterHeaderViewItem.getHeadView().getDrawable(), R.mipmap.icon_256);
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final DynamicCommentBean commentBean, final long feed_id) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mReSendCommentPopWindow.hide();
                        mPresenter.reSendComment(commentBean, feed_id);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
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
                .item1Str(getString(R.string.resend))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mReSendDynamicPopWindow.hide();
                        mListDatas.get(position).setState(DynamicBean.SEND_ING);
                        refreshData();
                        mPresenter.reSendDynamic(position);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mReSendDynamicPopWindow.hide();
                    }
                })
                .build();
    }

    @Override
    public void onMoreCommentClick(View view, DynamicDetailBeanV2 dynamicBean) {
        int position = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getFeed_mark());
        goDynamicDetail(position, true);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            DynamicDetailBeanV2 emptyData = new DynamicDetailBeanV2();
            data.add(emptyData);
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }
}
