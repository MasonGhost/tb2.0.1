package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.nineoldandroids.view.ViewHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GaussianBlurTrasnform;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListBaseItem;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.CirclePostListItemForZeroImage;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.PostTypeChoosePopAdapter;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.widget.CirclePostEmptyItem;
import com.zhiyicx.thinksnsplus.widget.comment.CirclePostListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.CirclePostNoPullRecyclerView;
import com.zhiyicx.thinksnsplus.widget.coordinatorlayout.AppBarLayoutOverScrollViewBehavior;
import com.zhiyicx.thinksnsplus.widget.popwindow.TypeChoosePopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter.PostTypeChoosePopAdapter.MyPostTypeEnum.LATEST_POST;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;

/**
 * @author Jliuer
 * @Date 2017/11/21/9:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailFragment extends TSListFragment<CircleDetailContract.Presenter, CirclePostListBean>
        implements CircleDetailContract.View, CirclePostListBaseItem.OnReSendClickListener,
        CirclePostNoPullRecyclerView.OnCommentStateClickListener<CirclePostCommentBean>, CirclePostListCommentView.OnCommentClickListener,
        CirclePostListBaseItem.OnMenuItemClickLisitener, CirclePostListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        CirclePostListCommentView.OnMoreCommentClickListener, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener
        , PhotoSelectorImpl.IPhotoBackListener, PostTypeChoosePopAdapter.OnTypeChoosedListener {

    public static final String CIRCLE_ID = "circle_id";

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.circle_title_layout)
    RelativeLayout mTitleContainerParent;
    @BindView(R.id.circle_appbar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;
    @BindView(R.id.uc_zoomiv)
    ImageView mIvCircleHeadBg;
    @BindView(R.id.iv_circle_head)
    ImageView mIvCircleHead;
    @BindView(R.id.tv_dynamic_count)
    TextView mTvCirclePostCount;
    @BindView(R.id.tv_type)
    TextView mTvCirclePostOrder;
    @BindView(R.id.tv_circle_title)
    TextView mTvCircleTitle;
    @BindView(R.id.tv_circle_member)
    TextView mTvCircleMember;
    @BindView(R.id.tv_circle_dec)
    TextView mTvCircleDec;
    @BindView(R.id.tv_circle_subscrib)
    CheckBox mTvCircleSubscrib;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_serach)
    ImageView mIvSerach;
    @BindView(R.id.iv_share)
    ImageView mIvShare;
    @BindView(R.id.iv_setting)
    ImageView mIvSetting;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_send_post)
    ImageView mBtnSendPost;
    @BindView(R.id.tv_circle_member_count)
    TextView mTvCircleMemberCount;
    @BindView(R.id.ll_member_container)
    LinearLayout mLlMemberContainer;
    @BindView(R.id.ll_detail_container)
    LinearLayout mLlDetailContainer;
    @BindView(R.id.ll_earnings_container)
    LinearLayout mLlEarningsContainer;
    @BindView(R.id.ll_permission_container)
    LinearLayout mLlPermissionContainer;
    @BindView(R.id.ll_report_container)
    LinearLayout mLlReportContainer;
    @BindView(R.id.ll_circle_navigation_container)
    LinearLayout mLlCircleNavigationContainer;
    @BindView(R.id.ll_dynamic_count_container)
    LinearLayout mLlDynamicCountContainer;
    @BindView(R.id.tv_top_tip_text)
    TextView mTvTopTipText;
    @BindView(R.id.fl_top_tip_container)
    FrameLayout mFlTopTipContainer;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshlayout;
    @BindView(R.id.empty_view)
    EmptyView mEmptyView;
    @BindView(R.id.container)
    CoordinatorLayout mContainer;

    @Inject
    CircleDetailPresenter mCircleDetailPresenter;

    private ActionBarDrawerToggle mToggle;

    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDeletPostPopWindow;
    private ActionPopupWindow mReSendCommentPopWindow;
    private ActionPopupWindow mReSendPostPopWindow;

    private ActionPopupWindow mOtherPostPopWindow;
    private ActionPopupWindow mMyPostPopWindow;

    // 类型选择框
    private TypeChoosePopupWindow mTypeChoosePopupWindow;

    private int mCurrentPostion;// 当前评论的动态位置
    private long mReplyToUserId;// 被评论者的 id
    private PhotoSelectorImpl mPhotoSelector;

    private AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir;

    private boolean updateHeadImg;

    private PostTypeChoosePopAdapter.MyPostTypeEnum mPostTypeEnum = LATEST_POST;

    public static CircleDetailFragment newInstance(long circle_id) {
        CircleDetailFragment circleDetailFragment = new CircleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CIRCLE_ID, circle_id);
        circleDetailFragment.setArguments(bundle);
        return circleDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerCircleDetailComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleDetailPresenterModule(new CircleDetailPresenterModule(this))
                .shareModule(new ShareModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_circle_detail;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int setToolBarBackgroud() {
        return android.R.color.transparent;
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
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        mPresenter.requestNetData(0L, false);
    }

    @Override
    public long getCircleId() {
        return getArguments().getLong(CIRCLE_ID);
    }

    @Override
    public String getType() {
        return LATEST_POST.value;
    }

    @Override
    public void allDataReady(CircleZipBean circleZipBean) {
        closeLoadingView();
        myAppBarLayoutBehavoir.setRefreshing(false);
        ((AnimationDrawable) mIvRefresh.getDrawable()).stop();
        mIvRefresh.setVisibility(View.INVISIBLE);

        CircleInfoDetail detail = circleZipBean.getCircleInfoDetail();
        mTvCircleTitle.setText(detail.getName());
        mTvCircleSubscrib.setVisibility(detail.getJoined() != null ? View.GONE : View.VISIBLE);

        mTvCircleDec.setText(String.format(Locale.getDefault(), getString(R.string.circle_detail_location), detail.getLocation()));
        mTvCircleMember.setText(String.format(Locale.getDefault(), getString(R.string.circle_detail_usercount), detail.getUsers_count()));
        mTvCirclePostCount.setText(String.format(Locale.getDefault(), getString(R.string.circle_detail_postcount), detail.getPosts_count()));

        if (!updateHeadImg) {
            updateHeadImg = true;

            Glide.with(mActivity)
                    .load(detail.getAvatar())
                    .error(R.drawable.shape_default_image)
                    .placeholder(R.drawable.shape_default_image)
                    .into(mIvCircleHead);

            Glide.with(mActivity)
                    .load(detail.getAvatar())
                    .placeholder(R.drawable.shape_default_image)
                    .transform(new GaussianBlurTrasnform(mActivity))
                    .error(R.drawable.shape_default_image)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mIvCircleHeadBg.setImageDrawable(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            mIvCircleHeadBg.setImageDrawable(errorDrawable);
                        }
                    });
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        setAdapter(adapter, new CirclePostListItemForZeroImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForOneImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForTwoImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForThreeImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForFourImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForFiveImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForSixImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForSevenImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForEightImage(getContext()));
        setAdapter(adapter, new CirclePostListItemForNineImage(getContext()));
        CirclePostEmptyItem emptyItem = new CirclePostEmptyItem();
        adapter.addItemViewDelegate(emptyItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CirclePostListBean> data, boolean isLoadMore) {
        // 增加空数据，用于显示占位图
        if (!isLoadMore && data.isEmpty() && getCircleId() >= 0) {
            CirclePostListBean emptyData = new CirclePostListBean();
            emptyData.setId(null);
            data.add(emptyData);
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onCacheResponseSuccess(List<CirclePostListBean> data, boolean isLoadMore) {
        super.onCacheResponseSuccess(data, isLoadMore);
        if (!data.isEmpty()) {
            closeLoadingView();
        }
    }

    @Override
    protected void initData() {
        mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
        super.initData();
        initTypePop(mPostTypeEnum);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        AndroidBug5497Workaround.assistActivity(getActivity());
        initToolBar();
        initLisener();
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onCommentStateClick(CirclePostCommentBean dynamicCommentBean, int position) {
        initReSendCommentPopupWindow(dynamicCommentBean, mListDatas.get(mPresenter.getCurrenPosiotnInDataList(dynamicCommentBean.getId())).getId());
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        goPostDetail(position);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onMoreCommentClick(View view, CirclePostListBean dynamicBean) {
        int position = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
        goPostDetail(position);
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    @Override
    public void onCommentContentClick(CirclePostListBean dynamicBean, int position) {
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
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

    private void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mCurrentPostion, mReplyToUserId, text);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {

    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onImageClick(ViewHolder holder, CirclePostListBean circlePostListBean, int position) {
        if (!TouristConfig.DYNAMIC_BIG_PHOTO_CAN_LOOK && mPresenter.handleTouristControl()) {
            return;
        }
        List<CirclePostListBean.ImagesBean> task = circlePostListBean.getImages();
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
        boolean canNotDeal;
        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            // 喜欢
            case 0:
                // 还未发送成功的动态列表不查看详情
                canNotDeal = (!TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0;
                if (canNotDeal) {
                    return;
                }
                handleLike(dataPosition);
                break;
            // 评论
            case 1:
                canNotDeal = (!TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0;
                if (canNotDeal) {
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
                    LogUtils.d("have't image");
                }
                int user_id = mListDatas.get(dataPosition).getUser_id().intValue();
                int current_id = (int) AppApplication.getMyUserIdWithdefault();
                if (user_id == current_id) {
                    initMyDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, mListDatas.get(dataPosition)
                            .hasCollected(), shareBitMap);
                    mMyPostPopWindow.show();
                } else {
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), dataPosition, mListDatas.get(dataPosition)
                            .hasCollected(), shareBitMap);
                    mOtherPostPopWindow.show();
                }
                break;
            default:
                onItemClick(null, null, (dataPosition + 1)); // 加上 header
        }
    }

    @Override
    public void onReSendClick(int position) {

    }

    @Override
    public void onChoosed(PostTypeChoosePopAdapter.MyPostTypeEnum type) {
        switch (type) {
            case ALL:
                mTvCirclePostOrder.setText(mActivity.getString(R.string.post_typpe_all));
                break;
            case LATEST_POST:
                mTvCirclePostOrder.setText(mActivity.getString(R.string.post_typpe_new));
                break;
            case LATEST_COMMENT:
                mTvCirclePostOrder.setText(mActivity.getString(R.string.post_typpe_reply));

                break;
            default:

        }
        if (mTypeChoosePopupWindow != null) {
            mTypeChoosePopupWindow.dismiss();
        }
        requestNetData(0L, false);
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final CirclePostCommentBean commentBean, final long feed_id) {
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
     * 初始化评论删除选择弹框
     *
     * @param circlePostListBean curent dynamic
     * @param dynamicPositon     dynamic comment position
     * @param commentPosition    current comment position
     */
    private void initDeletCommentPopWindow(final CirclePostListBean circlePostListBean, final int dynamicPositon, final int commentPosition) {


        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .item1Str(BuildConfig.USE_TOLL && circlePostListBean.getState() == CirclePostListBean
                        .SEND_SUCCESS && !circlePostListBean
                        .getComments().get(commentPosition).getPinned() ? getString(R
                        .string.dynamic_list_top_comment) : null)
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mDeletCommentPopWindow.hide();

                    Bundle bundle = new Bundle();
                    bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_POST);// 资源类型
                    bundle.putLong(StickTopFragment.PARENT_ID, circlePostListBean.getId());// 资源id
                    bundle.putLong(StickTopFragment.CHILD_ID, circlePostListBean
                            .getComments().get(commentPosition).getId());// 该资源的评论id,非评论置顶不传这个
                    Intent intent = new Intent(getActivity(), StickTopActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    showBottomView(true);

                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteComment(circlePostListBean, dynamicPositon, circlePostListBean.getComments().get(commentPosition).getId(), commentPosition);
                        showBottomView(true);
                    }, true);
                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    /**
     * 初始化我的动态操作弹窗
     *
     * @param circlePostListBean curent dynamic
     * @param position           curent dynamic postion
     */
    private void initMyDynamicPopupWindow(final CirclePostListBean circlePostListBean, final int position, boolean isCollected,
                                          final Bitmap shareBitMap) {
        Long feed_id = circlePostListBean.getId();
        boolean feedIdIsNull = feed_id == null || feed_id == 0;
        mMyPostPopWindow = ActionPopupWindow.builder()
                .item2Str(getString(feedIdIsNull ? R.string.empty : isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .item3Str(getString(R.string.dynamic_list_delete_dynamic))
                .item1Str(getString(feedIdIsNull ? R.string.empty : R.string.dynamic_list_share_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {// 收藏
                    mMyPostPopWindow.hide();
                    handleCollect(position);
                    showBottomView(true);
                })
                .item3ClickListener(() -> {// 删除
                    mMyPostPopWindow.hide();
                    mPresenter.deletePost(circlePostListBean, position);
                    showBottomView(true);
                })
                .item1ClickListener(() -> {// 分享
                    mPresenter.sharePost(circlePostListBean, shareBitMap);
                    mMyPostPopWindow.hide();
                })
                .bottomClickListener(() -> {//取消
                    mMyPostPopWindow.hide();
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
     * 喜欢
     *
     * @param dataPosition
     */
    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
        mListDatas.get(dataPosition).setLiked(!mListDatas.get(dataPosition).hasLiked());
        mListDatas.get(dataPosition).setLikes_count(!mListDatas.get(dataPosition).hasLiked() ?
                mListDatas.get(dataPosition).getLikes_count() - 1 : mListDatas.get(dataPosition).getLikes_count() + 1);
        refreshData(dataPosition);
        mPresenter.handleLike(mListDatas.get(dataPosition).hasLiked(), mListDatas.get(dataPosition).getId(), dataPosition);
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param circlePostListBean curent dynamic
     */
    private void initOtherDynamicPopupWindow(final CirclePostListBean circlePostListBean, int position, boolean isCollected, final
    Bitmap shareBitmap) {
        mOtherPostPopWindow = ActionPopupWindow.builder()
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
                    mOtherPostPopWindow.hide();
                    showBottomView(true);
                })
                .item1ClickListener(() -> {// 分享
                    mPresenter.sharePost(circlePostListBean, shareBitmap);
                    mOtherPostPopWindow.hide();
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mOtherPostPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    /**
     * 收藏
     *
     * @param dataPosition
     */
    private void handleCollect(int dataPosition) {
        // 先更新界面，再后台处理
        mPresenter.handleCollect(mListDatas.get(dataPosition));
        boolean is_collection = mListDatas.get(dataPosition).hasCollected();// 旧状态
        mListDatas.get(dataPosition).setCollected(!is_collection);
        refreshData(dataPosition);
    }

    protected void setAdapter(MultiItemTypeAdapter adapter, CirclePostListBaseItem circlePostListBaseItem) {
        circlePostListBaseItem.setOnImageClickListener(this);
        circlePostListBaseItem.setOnUserInfoClickListener(this);
        circlePostListBaseItem.setOnMenuItemClickLisitener(this);
        circlePostListBaseItem.setOnReSendClickListener(this);
        circlePostListBaseItem.setOnMoreCommentClickListener(this);
        circlePostListBaseItem.setOnCommentClickListener(this);
        circlePostListBaseItem.setOnCommentStateClickListener(this);
        adapter.addItemViewDelegate(circlePostListBaseItem);
    }

    private void initLisener() {
        mDrawer.setClipToPadding(false);
        mDrawer.setClipChildren(false);
        mDrawer.setScrimColor(Color.TRANSPARENT);
        mToggle = new ActionBarDrawerToggle(getActivity(), mDrawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                View mContent = mDrawer.getChildAt(0);
                ViewHelper.setTranslationX(mContent,
                        -drawerView.getMeasuredWidth() * slideOffset);
            }
        };

        RxView.clicks(mBtnSendPost)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> startActivity(new Intent(getActivity(), MarkdownActivity.class)));

        RxView.clicks(mTvCirclePostOrder)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mTypeChoosePopupWindow.show());


        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();

        myAppBarLayoutBehavoir.setOnRefreshChangeListener(new AppBarLayoutOverScrollViewBehavior.onRefreshChangeListener() {
            @Override
            public void onRefreshShow() {
                mIvRefresh.setVisibility(View.VISIBLE);
                ((AnimationDrawable) mIvRefresh.getDrawable()).start();
            }

            @Override
            public void doRefresh() {
                mPresenter.requestNetData(0L, false);
            }
        });
        mIlvComment.setOnSendClickListener(this);
    }

    private void initToolBar() {
        if (setUseStatusView()) {
            // toolBar 设置状态栏高度的 marginTop
            int height = getResources().getDimensionPixelSize(R.dimen.spacing_large);
            CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
            layoutParams.setMargins(0, height, 0, 0);
            mToolbar.setLayoutParams(layoutParams);
        }
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mLlCircleNavigationContainer.getLayoutParams();
        params.width = DeviceUtils.getScreenWidth(getActivity()) / 2;
        mLlCircleNavigationContainer.setLayoutParams(params);

    }


    private void initTypePop(PostTypeChoosePopAdapter.MyPostTypeEnum postType) {
        CommonAdapter commonAdapter = new PostTypeChoosePopAdapter(mActivity, Arrays.asList(mActivity.getResources().getStringArray(R.array
                .post_typpe_array)), postType, this);
        mTypeChoosePopupWindow = TypeChoosePopupWindow.Builder()
                .with(mActivity)
                .adapter(commonAdapter)
                .asVertical()
                .alpha(1.0f)
                .itemSpacing(mActivity.getResources().getDimensionPixelOffset(R.dimen.divider_line))
                .parentView(mTvCirclePostOrder)
                .build();

    }

    private void goPostDetail(int position) {
        CirclePostDetailActivity.startActivity(getActivity(), mListDatas.get(position));
        mPresenter.handleViewCount(mListDatas.get(position).getId(), position);
    }

    @OnClick({R.id.ll_member_container, R.id.ll_detail_container, R.id.ll_earnings_container,
            R.id.ll_permission_container, R.id.ll_report_container, R.id.iv_back, R.id.iv_serach,
            R.id.iv_share, R.id.iv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_member_container:
                break;
            case R.id.ll_detail_container:
                break;
            case R.id.ll_earnings_container:
                break;
            case R.id.ll_permission_container:
                break;
            case R.id.ll_report_container:
                break;

            case R.id.iv_back:
                setLeftClick();
                break;
            case R.id.iv_serach:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_setting:
                boolean isOpen = mDrawer.isDrawerOpen(mLlCircleNavigationContainer);
                if (isOpen) {
                    return;
                }
                mDrawer.openDrawer(Gravity.RIGHT);
                break;
            default:
        }
    }

    @Override
    public boolean isNeedHeaderInfo() {
        return true;
    }

    @Override
    public BaseCircleRepository.CircleMinePostType getCircleMinePostType() {
        return BaseCircleRepository.CircleMinePostType.PUBLISH;
    }

    @Override
    public String getSearchInput() {
        return "";
    }

}
