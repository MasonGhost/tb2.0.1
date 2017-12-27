package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleZipBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
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
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.widget.comment.CirclePostListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.CirclePostNoPullRecyclerView;
import com.zhiyicx.thinksnsplus.widget.popwindow.TypeChoosePopupWindow;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class BaseCircleDetailFragment extends TSListFragment<CircleDetailContract.Presenter, CirclePostListBean>
        implements CircleDetailContract.View, CirclePostListBaseItem.OnReSendClickListener,
        CirclePostNoPullRecyclerView.OnCommentStateClickListener<CirclePostCommentBean>, CirclePostListCommentView.OnCommentClickListener,
        CirclePostListBaseItem.OnMenuItemClickLisitener, CirclePostListBaseItem.OnImageClickListener, OnUserInfoClickListener,
        CirclePostListCommentView.OnMoreCommentClickListener, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener
        , PhotoSelectorImpl.IPhotoBackListener {

    public static final String CIRCLE_ID = "circle_id";
    public static final String CIRCLE_TYPE = "circle_type";

    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;

    @Inject
    CircleDetailPresenter mCircleDetailPresenter;

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

    private BaseCircleRepository.CircleMinePostType mCircleMinePostType = BaseCircleRepository.CircleMinePostType.PUBLISH;

    public static BaseCircleDetailFragment newInstance(BaseCircleRepository.CircleMinePostType circleMinePostType) {
        BaseCircleDetailFragment circleDetailFragment = new BaseCircleDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CIRCLE_TYPE, circleMinePostType);
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
        if (getArguments() != null) {
            mCircleMinePostType = (BaseCircleRepository.CircleMinePostType) getArguments().getSerializable(CIRCLE_TYPE);
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_base_circle_detail;
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
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }


    @Override
    public Long getCircleId() {
        if (getArguments() == null || getArguments().getLong(CIRCLE_ID) == 0) {
            return null;
        }
        return getArguments().getLong(CIRCLE_ID);
    }

    @Override
    public void allDataReady(CircleZipBean circleZipBean) {
        closeLoadingView();
    }

    @Override
    public String getType() {
        return "";
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
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mIlvComment.setOnSendClickListener(this);
        mVShadow.setOnClickListener(v -> closeInputView());
    }

    private void closeInputView() {
        if (mIlvComment.getVisibility() == View.VISIBLE) {
            mIlvComment.setVisibility(View.GONE);
            DeviceUtils.hideSoftKeyboard(mActivity, mIlvComment.getEtContent());
        }
        mVShadow.setVisibility(View.GONE);
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
        CirclePostDetailActivity.startActivity(getActivity(), mListDatas.get(position)
                .getGroup_id(), mListDatas.get(position).getId(),false);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onMoreCommentClick(View view, CirclePostListBean dynamicBean) {
        int position = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
        goPostDetail(position, true);
    }

    private void goPostDetail(int position, boolean b) {

    }

    @Override
    public boolean isOutsideSerach() {
        return true;
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

    @Override
    public void onCommentContentLongClick(CirclePostListBean dynamicBean, int position) {
        if (mPresenter.handleTouristControl()) {
            return;
        }
        mCurrentPostion = mPresenter.getCurrenPosiotnInDataList(dynamicBean.getId());
        // 举报
        if (dynamicBean.getComments().get(position).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(dynamicBean.getComments().get
                    (position).getCommentUser(), dynamicBean.getComments().get
                    (position).getId().toString(),
                    null, null, dynamicBean.getComments().get(position).getContent(), ReportType.CIRCLE_COMMENT));

        } else {

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
    public void loadAllError() {
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        showLoadViewLoadError();
    }

    @Override
    public void updateCircleInfo(CircleInfo circleInfo) {

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
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    mPresenter.deleteComment(circlePostListBean, dynamicPositon, circlePostListBean.getComments().get(commentPosition).getId(),
                            commentPosition);
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

    @Override
    public boolean isNeedHeaderInfo() {
        return false;
    }

    @Override
    public BaseCircleRepository.CircleMinePostType getCircleMinePostType() {
        return mCircleMinePostType;
    }

    @Override
    public String getSearchInput() {
        return "";
    }

    @Override
    public CircleInfo getCircleInfo() {
        return null;
    }
}
