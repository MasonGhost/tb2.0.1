package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
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
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicNoPullRecycleView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.LOOK_COMMENT_MORE;

/**
 * @Describe 动态列表
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements DynamicNoPullRecycleView.OnCommentStateClickListener, InputLimitView.OnSendClickListener, DynamicContract.View, DynamicListCommentView.OnCommentClickListener, DynamicListCommentView.OnMoreCommentClickListener, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener {
    private static final String BUNDLE_DYNAMIC_TYPE = "dynamic_type";
    public static final long ITEM_SPACING = 5L; // 单位dp
    @BindView(R.id.fl_container)
    FrameLayout mFlContainer;


    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造
    private String mDynamicType = ApiConfig.DYNAMIC_TYPE_NEW;


    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    private ActionPopupWindow mDeletCommentPopWindow;
    private int mCurrentPostion;// 当前评论的动态位置
    private long mReplyToUserId;// 被评论者的 id


    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    OnCommentClickListener mOnCommentClickListener;

    public static DynamicFragment newInstance(String dynamicType, OnCommentClickListener l) {
        DynamicFragment fragment = new DynamicFragment();
        fragment.setOnCommentClickListener(l);
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
        dynamicListBaseItem.setOnCommentStateClickListener(this);
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
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
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
        // 还未发送成功的动态列表不查看详情
        if (mAdapter.getItem(position).getFeed_id() == null || mAdapter.getItem(position).getFeed_id() == 0) {
            return;
        }
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mAdapter.getItem(position));
        bundle.putString(DYNAMIC_DETAIL_DATA_TYPE, getDynamicType());
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
                // 还未发送成功的动态列表不查看详情
                if (mAdapter.getItem(dataPosition).getFeed_id() == null || mAdapter.getItem(dataPosition).getFeed_id() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 1: // 评论
                // 还未发送成功的动态列表不查看详情
                if (mAdapter.getItem(dataPosition).getFeed_id() == null || mAdapter.getItem(dataPosition).getFeed_id() == 0) {
                    return;
                }
                showCommentView();
                mCurrentPostion = dataPosition;
                mReplyToUserId = 0;// 0 代表评论动态
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
//            showBottomView(false);
            initLoginOutPopupWindow(dynamicBean, mCurrentPostion, position);
            mDeletCommentPopWindow.show();
        } else {
            showCommentView();
            mReplyToUserId = dynamicBean.getComments().get(position).getUser_id();
            String contentHint = "";
            if (dynamicBean.getComments().get(position).getReply_to_user_id() != dynamicBean.getUser_id()) {
                contentHint = getString(R.string.reply, dynamicBean.getComments().get(position).getCommentUser().getName());
            }
            if (mOnCommentClickListener != null) {
                mOnCommentClickListener.setCommentHint(contentHint);
            }
        }

    }

    private void showCommentView() {
        showBottomView(false);

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
                        showBottomView(true);
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mDeletCommentPopWindow.hide();
                        showBottomView(true);
                    }
                })
                .build();
    }

    private void showBottomView(boolean isShow) {
        if (mOnCommentClickListener != null) {
            mOnCommentClickListener.onButtonMenuShow(isShow);
        }
    }

    /**
     * comment send
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, String text) {
        mPresenter.sendComment(mCurrentPostion, mReplyToUserId, text);
        com.zhiyicx.imsdk.utils.common.DeviceUtils.hideSoftKeyboard(getContext(), v);
        showBottomView(true);
    }

    /**
     * 重发评论
     *
     * @param dynamicCommentBean
     * @param position
     */
    @Override
    public void onCommentStateClick(DynamicCommentBean dynamicCommentBean, int position) {
        showMessage("点击了评论失败状态");
    }

    public interface OnCommentClickListener {
        void onButtonMenuShow(boolean isShow);

        void setCommentHint(String hintStr);
    }
}
