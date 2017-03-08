package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ImageInfo;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ParcelableSparseArray;
import com.zhiyicx.baseproject.widget.pictureviewer.core.PhotoView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
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
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryFragment;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;

/**
 * @Describe 动态列表
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements InputLimitView.OnSendClickListener, DynamicContract.View, DynamicListCommentView.OnCommentClickListener, DynamicListCommentView.OnMoreCommentClickListener, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, DynamicListBaseItem.OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener {
    private static final String BUNDLE_DYNAMIC_TYPE = "dynamic_type";
    public static final long ITEM_SPACING = 5L; // 单位dp

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;

    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;


    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造
    private String mDynamicType = ApiConfig.DYNAMIC_TYPE_NEW;
    private boolean mKeyboradIsOpen;// 软键盘是否打开

    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    private ActionPopupWindow mDeletCommentPopWindow;
    private int mCurrentPostion;// 当前评论的动态位置
    private long mReplyToUserId;// 被评论者的 id


    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        mOnImageClickListener = onImageClickListener;
    }

    OnImageClickListener mOnImageClickListener;

    public static DynamicFragment newInstance(String dynamicType, OnImageClickListener l) {
        DynamicFragment fragment = new DynamicFragment();
        fragment.setOnImageClickListener(l);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_list;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mIlvComment.setOnSendClickListener(this);
        // 软键盘控制区
        mIlvComment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect rect = new Rect();
                //获取root在窗体的可视区域
                mFlContainer.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = mFlContainer.getRootView().getHeight() - rect.bottom;
                int dispayHeight = UIUtils.getWindowHeight(getContext());
                //若不可视区域高度大于1/3屏幕高度，则键盘显示
                if (rootInvisibleHeight > (1 / 3 * dispayHeight)) {
                    mKeyboradIsOpen = true;
                } else {
                    //键盘隐藏
                    mKeyboradIsOpen = false;
                    mIlvComment.clearFocus();// 主动失去焦点
                }
                mIlvComment.setSendButtonVisiable(mKeyboradIsOpen);
            }
        });

    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    @Override
    protected boolean getPullDownRefreshEnable() {
        return true;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        setAdapter(adapter, new DynamicListBaseItem(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));

        adapter.setOnItemClickListener(this);
        return adapter;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        dynamicListBaseItem.setOnMoreCommentClickListener(this);
        dynamicListBaseItem.setOnCommentClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }


    @Override
    protected void initData() {
        DaggerDynamicComponent // 在 super.initData();之前，因为initdata 会使用到 presenter
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicPresenterModule(new DynamicPresenterModule(this))
                .build().inject(this);
        mDynamicType = getArguments().getString(BUNDLE_DYNAMIC_TYPE);

        super.initData();
    }

    @Override
    public void setPresenter(DynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
        mRefreshlayout.setLoadingMore(false);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showToast(message);
    }

    /**
     * scan imags
     *
     * @param dynamicBean
     * @param position
     */
    @Override
    public void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position) {
        List<ImageBean> imageBeanList = new ArrayList<>();
        if (dynamicBean.getFeed().getStorages() != null) {
            imageBeanList = dynamicBean.getFeed().getStorages();
        } else {
            for (int i = 0; i < dynamicBean.getFeed().getLocalPhotos().size(); i++) {
                ImageBean imageBean = new ImageBean();
                imageBean.setImgUrl(dynamicBean.getFeed().getLocalPhotos().get(i));
                imageBeanList.add(imageBean);
            }
        }
        ParcelableSparseArray<ImageInfo> imageInfoParcelableSparseArray = new ParcelableSparseArray<>();
        int size = dynamicBean.getFeed().getLocalPhotos() == null ? dynamicBean.getFeed().getStorages().size() : dynamicBean.getFeed().getLocalPhotos().size();
        switch (size) {
            case 9:
                imageInfoParcelableSparseArray.put(8, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_8)));
            case 8:
                imageInfoParcelableSparseArray.put(7, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_7)));
            case 7:
                imageInfoParcelableSparseArray.put(6, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_6)));
            case 6:
                imageInfoParcelableSparseArray.put(5, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_5)));
            case 5:
                imageInfoParcelableSparseArray.put(4, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_4)));
            case 4:
                imageInfoParcelableSparseArray.put(3, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_3)));
            case 3:
                imageInfoParcelableSparseArray.put(2, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_2)));
            case 2:
                imageInfoParcelableSparseArray.put(1, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_1)));
            case 1:
                imageInfoParcelableSparseArray.put(0, PhotoView.getImageViewInfo((PhotoView) holder.getView(R.id.siv_0)));
                break;
            default:
        }
//        mOnImageClickListener.onImageClick(imageBeanList,imageInfoParcelableSparseArray,position);
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GalleryFragment.BUNDLE_IMAGS, (ArrayList<? extends Parcelable>) imageBeanList);
        bundle.putInt(GalleryFragment.BUNDLE_IMAGS_POSITON, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * scan user Info
     *
     * @param userInfoBean
     */
    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        System.out.println("userInfoBean.getName() = " + userInfoBean.getName());
        showMessage(userInfoBean.getName());
    }

    @Override
    public String getDynamicType() {
        return mDynamicType;
    }

    @Override
    public List<DynamicBean> getDatas() {
        return mDynamicBeens;
    }

    @Override
    public void refresh() {
        refreshData();
    }

    @Override
    public void refresh(int position) {
        LogUtils.d(TAG, "mDynamicBeens    position  = " + mDynamicBeens.toString());
        refreshData(position);
    }

    /**
     * resend click
     *
     * @param position
     */
    @Override
    public void onReSendClick(int position) {
        mDynamicBeens.get(position).setState(DynamicBean.SEND_ING);
        refresh();
        mPresenter.reSendDynamic(position);
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        goDynamicDetail(position, false);
    }

    private void goDynamicDetail(int position, boolean isLookMoreComment) {
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mAdapter.getItem(position));
        bundle.putInt(DYNAMIC_DETAIL_DATA_POSITION, position);
        bundle.putBoolean(LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            case 0: // 喜欢
                handleLike(dataPosition);
                break;

            case 1: // 评论
                mIlvComment.setVisibility(View.VISIBLE);
                mIlvComment.setFocusable(true);
                ActivityUtils.dimBackground(getActivity(), 1.0f, 0.5f);
                break;

            case 2: // 浏览
                onItemClick(null, null, dataPosition);
                break;

            case 3: // 更多
                showMessage("点击了跟多");

                break;
            default:
                onItemClick(null, null, dataPosition);
        }

    }

    /**
     * 喜欢
     *
     * @param dataPosition
     */
    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
        mDynamicBeens.get(dataPosition).getTool().setIs_digg_feed(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ? DynamicToolBean.STATUS_DIGG_FEED_CHECKED : DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED);
        mDynamicBeens.get(dataPosition).getTool().setFeed_digg_count(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ?
                mDynamicBeens.get(dataPosition).getTool().getFeed_digg_count() - 1 : mDynamicBeens.get(dataPosition).getTool().getFeed_digg_count() + 1);
        refresh();
        mPresenter.handleLike(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED,
                mDynamicBeens.get(dataPosition).getFeed().getFeed_id(), dataPosition);
    }

    @Override
    public void onCommentUserInfoClick(UserInfoBean userInfoBean) {
        onUserInfoClick(userInfoBean);
    }

    /**
     * comment has been clicked
     *
     * @param dynamicBean current dynamic
     * @param position    this position of comment
     */
    @Override
    public void onCommentContentClick(DynamicBean dynamicBean, int position) {
        mCurrentPostion = mAdapter.getDatas().indexOf(dynamicBean);
        if (dynamicBean.getComments().get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            initLoginOutPopupWindow(dynamicBean, mCurrentPostion, position);
            mDeletCommentPopWindow.show();
        } else {
            mIlvComment.setVisibility(View.VISIBLE);
            mIlvComment.setFocusable(true);
            if (dynamicBean.getComments().get(position).getReply_to_user_id() != dynamicBean.getUser_id()) {
                mIlvComment.setEtContentHint(String.format(getString(R.string.reply), dynamicBean.getComments().get(position).getCommentUser().getName()));
            } else {
                mIlvComment.setEtContentHint("");
            }
            mReplyToUserId = dynamicBean.getComments().get(position).getReply_to_user_id();
            DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
            mIlvComment.getFocus();
            ActivityUtils.dimBackground(getActivity(), 1.0f, 0.8f);
        }

    }

    @Override
    public void onMoreCommentClick(View view, DynamicBean dynamicBean) {
        goDynamicDetail(mAdapter.getDatas().indexOf(dynamicBean), true);
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param dynamicBean     curent dynamic
     * @param dynamicPositon  dynamic comment position
     * @param commentPosition current comment position
     */
    private void initLoginOutPopupWindow(final DynamicBean dynamicBean, final int dynamicPositon, final int commentPosition) {
        if (mDeletCommentPopWindow != null) {
            return;
        }
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

    /**
     * comment send
     *
     * @param text
     */
    @Override
    public void onSendClick(String text) {
        mPresenter.sendComment(mCurrentPostion, mReplyToUserId, mIlvComment.getInputContent());
    }

    public interface OnImageClickListener {
        void onImageClick(List<ImageBean> images, ParcelableSparseArray<ImageInfo> infos, int position);
    }
}
