package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailWebItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.adapter.AnswerDetailHeaderView;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/08
 * @Email Jliuer@aliyun.com
 * @Description 资讯详情
 */
public class AnswerDetailsFragment extends TSListFragment<AnswerDetailsConstract.Presenter,
        AnswerCommentListBean> implements AnswerDetailsConstract.View, InputLimitView
        .OnSendClickListener {

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

    private AnswerDetailHeaderView mAnswerDetailHeaderView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;

    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDealInfoMationPopWindow;

    /**
     * 传入的资讯信息
     */
    private AnswerInfoBean mAnswerInfoBean;
    private boolean isFirstIn = true;

    private int mReplyUserId;// 被评论者的 id ,评论动态 id = 0

    /**
     * 打赏
     */
    private List<RewardsListBean> mRewardsListBeen = new ArrayList<>();
    private RewardsCountBean mRewardsCountBean;

    public static AnswerDetailsFragment newInstance(Bundle params) {
        AnswerDetailsFragment fragment = new AnswerDetailsFragment();
        fragment.setArguments(params);
        return fragment;
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
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        InfoDetailCommentItem infoDetailCommentItem = new InfoDetailCommentItem(new
                ItemOnCommentListener());
        multiItemTypeAdapter.addItemViewDelegate(infoDetailCommentItem);
        multiItemTypeAdapter.addItemViewDelegate(new InfoDetailCommentEmptyItem());
        return multiItemTypeAdapter;
    }

    @Override
    public void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> datas) {
        this.mRewardsCountBean = rewardsCountBean;
        this.mRewardsListBeen.clear();
        this.mRewardsListBeen.addAll(datas);
        mAnswerDetailHeaderView.updateReward(mAnswerInfoBean.getId(), mRewardsListBeen, mRewardsCountBean, RewardType.INFO);
    }

    @Override
    public void updateAnswerHeader(AnswerInfoBean answerInfoBean) {
        closeLoadingView();
        mCoordinatorLayout.setEnabled(true);
        this.mAnswerInfoBean = answerInfoBean;
        mAnswerDetailHeaderView.setDetail(answerInfoBean);
        mAnswerDetailHeaderView.updateDigList(answerInfoBean);
//        onNetResponseSuccess(infoDetailBean.getCommentList(), false);
    }

    @Override
    public void deleteInfo(boolean deleting, boolean success, String message) {
        if (deleting) {
            showSnackLoadingMessage(getString(R.string.info_deleting));
        } else {
            if (success) {
                EventBus.getDefault().post(mAnswerInfoBean, "");
                getActivity().finish();
            } else {
                showSnackErrorMessage(message);
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
        mAnswerInfoBean = (AnswerInfoBean) getArguments().getSerializable(BUNDLE_INFO);
        if (mAnswerInfoBean == null) {
            mAnswerInfoBean = new AnswerInfoBean();
            Long ids = getArguments().getLong(BUNDLE_SOURCE_ID);
            mAnswerInfoBean.setId(ids);
        }

        mTvToolbarCenter.setVisibility(View.VISIBLE);
        mTvToolbarCenter.setText(getString(R.string.info_details));
        initHeaderView();
        initBottomToolStyle();
        initBottomToolListener();
        initListener();
        setDigg(mPresenter.isDiged());
    }

    @Override
    public void loadAllError() {
        setLoadViewHolderImag(R.mipmap.img_default_internet);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadError();
    }

    @Override
    public void infoMationHasBeDeleted() {
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        mTvToolbarRight.setVisibility(View.GONE);
        mTvToolbarCenter.setVisibility(View.GONE);
        showLoadViewLoadErrorDisableClick();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_detail;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public void setPresenter(AnswerDetailsConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Long getAnswerId() {
        return mAnswerInfoBean.getId();
    }

    @Override
    public int getInfoType() {
        return Integer.valueOf(getArguments().getString(BUNDLE_INFO_TYPE, "-100"));
    }

    @Override
    public AnswerInfoBean getAnswerInfo() {
        return mAnswerInfoBean;
    }

    @Override
    public void setCollect(boolean isCollected) {
        mDdDynamicTool.setItemIsChecked(isCollected, ITEM_POSITION_0);
    }

    @Override
    public void setDigg(boolean isDigged) {
        mDdDynamicTool.setItemIsChecked(isDigged, ITEM_POSITION_0);
        if (mAnswerInfoBean.getLikes() != null) {
            mAnswerDetailHeaderView.updateDigList(mAnswerInfoBean);
        }
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<AnswerCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) { // 空白展位图
                AnswerCommentListBean emptyData = new AnswerCommentListBean();
                data.add(emptyData);
            }
        }
        super.onNetResponseSuccess(data, isLoadMore);
        if (!isLoadMore) {
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe(aLong -> {
                        mPresenter.reqReWardsData(mAnswerInfoBean.getId().intValue());// 刷新打赏
                    });

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
    public void refreshData() {
        super.refreshData();
        mAdapter.notifyDataSetChanged();
        mAnswerDetailHeaderView.updateCommentView(mAnswerInfoBean);
    }

    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mAnswerDetailHeaderView = new AnswerDetailHeaderView(getContext(), mPresenter.getAdvert());
        mHeaderAndFooterWrapper.addHeaderView(mAnswerDetailHeaderView.getAnswerDetailHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
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
            switch (position) {
                case DynamicDetailMenuView.ITEM_POSITION_0:// 点赞
                    mPresenter.handleLike(!mAnswerInfoBean.getLiked(),
                            mAnswerInfoBean.getId() + "");
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_1:// 评论
                    showCommentView();
                    mReplyUserId = 0;
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_2:// 分享
                    Bitmap bitmap = FileUtils.readImgFromFile(getActivity(), "info_share");

                    mPresenter.shareInfo(bitmap);
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_3:// 更多
                    initDealAnswerPopupWindow(mAnswerInfoBean, mAnswerInfoBean.getCollected());
                    mDealInfoMationPopWindow.show();
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
                .subscribe(aVoid -> {
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

    /**
     * 初始化评论删除选择弹框
     */
    private void initDeleteCommentPopupWindow(final AnswerCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL ? getString(R.string.dynamic_list_top_comment) : null)
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 跳转置顶页面
                    Bundle bundle = new Bundle();
                    bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_INFO);// 资源类型
                    bundle.putLong(StickTopFragment.PARENT_ID, data.getId());// 资源id
                    bundle.putLong(StickTopFragment.CHILD_ID,data.getId());
                    Intent intent = new Intent(getActivity(), StickTopActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                })
                .item2ClickListener(() -> {
                    mPresenter.deleteComment(data);
                    mDeletCommentPopWindow.hide();

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    /**
     * 初始化他人动态操作选择弹框
     *
     * @param answerInfoBean curent answerInfoBean
     */
    private void initDealAnswerPopupWindow(final AnswerInfoBean answerInfoBean, boolean isCollected) {
        boolean isMine = answerInfoBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
        mDealInfoMationPopWindow = ActionPopupWindow.builder()
                .item1Str(isMine ? getString(R.string.info_apply_for_top) : "")
                .item2Str(isMine ? getString(R.string.info_delete) : getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string.dynamic_list_collect_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {// 收藏
                    // 如果是自己发布的，则不能收藏只能删除
                    if (isMine) {
                        mPresenter.deleteInfo();
                    } else {
                        mPresenter.handleCollect(!answerInfoBean.getCollected(),
                                answerInfoBean.getId() + "");
                    }
                    mDealInfoMationPopWindow.hide();
                })
                .item1ClickListener(() -> {// 申请置顶
//                    if (infoMation.is_pinned()) {
//                        showSnackErrorMessage(getString(R.string.info_alert_reapply_for_top));
//                    } else {
//                        // 跳转置顶页面
//                        Bundle bundle = new Bundle();
//                        bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_INFO);// 资源类型
//                        bundle.putLong(StickTopFragment.PARENT_ID, infoMation.getId());// 资源id
//                        Intent intent = new Intent(getActivity(), StickTopActivity.class);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
                    mDealInfoMationPopWindow.hide();
                })
                .bottomClickListener(() -> mDealInfoMationPopWindow.hide())
                .build();
    }

    class ItemOnCommentListener implements InfoDetailCommentItem.OnCommentItemListener {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
            AnswerCommentListBean infoCommentListBean = mListDatas.get(position);
            if (infoCommentListBean != null && !TextUtils.isEmpty(infoCommentListBean.getBody())) {
                if (infoCommentListBean.getUser_id() == AppApplication.getmCurrentLoginAuth()
                        .getUser_id()) {// 自己的评论
//                if (mListDatas.get(position).getId() != -1) {
                    initDeleteCommentPopupWindow(infoCommentListBean);
                    mDeletCommentPopWindow.show();
//                } else {
//
//                    return;
//                }
                } else {
                    mReplyUserId = infoCommentListBean.getUser_id().intValue();
                    showCommentView();
                    String contentHint = getString(R.string.default_input_hint);
                    if (infoCommentListBean.getReply_user() != infoCommentListBean.getId()) {
                        contentHint = getString(R.string.reply, infoCommentListBean.getFromUserInfoBean().getName());
                    }
                    mIlvComment.setEtContentHint(contentHint);
                }
            }
        }

        @Override
        public void onUserInfoClick(UserInfoBean userInfoBean) {
            PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RewardType.INFO.id) {
                mPresenter.reqReWardsData(mAnswerInfoBean.getId().intValue());
            }
        }

    }
}
