package com.zhiyicx.thinksnsplus.modules.information.infodetails;

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
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailHeaderView;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailWebItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
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
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_LIST_DELETE;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;

/**
 * @Author Jliuer
 * @Date 2017/03/08
 * @Email Jliuer@aliyun.com
 * @Description 资讯详情
 */
public class InfoDetailsFragment extends TSListFragment<InfoDetailsConstract.Presenter,
        InfoCommentListBean> implements InfoDetailsConstract.View, InputLimitView
        .OnSendClickListener, BaseWebLoad.OnWebLoadListener {

    public static final String BUNDLE_INFO_TYPE = "info_type";
    public static final String BUNDLE_INFO = "info";

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

    private InfoDetailHeaderView mInfoDetailHeader;

    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mDealInfoMationPopWindow;

    /**
     * 传入的资讯信息
     */
    private InfoListDataBean mInfoMation;
    private boolean isFirstIn = true;

    private int mReplyUserId;// 被评论者的 id ,评论动态 id = 0

    /**
     * 打赏
     */
    private List<RewardsListBean> mRewardsListBeen = new ArrayList<>();
    private RewardsCountBean mRewardsCountBean;

    public static InfoDetailsFragment newInstance(Bundle params) {
        InfoDetailsFragment fragment = new InfoDetailsFragment();
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
        if (mRewardsCountBean != null && !TextUtils.isEmpty(mRewardsCountBean.getAmount())) {
            mRewardsCountBean.setAmount("" + PayConfig.realCurrency2GameCurrency(Double.parseDouble(mRewardsCountBean.getAmount()), mPresenter
                    .getRatio()));
        }
        mInfoDetailHeader.updateReward(mInfoMation.getId(), mRewardsListBeen, mRewardsCountBean, RewardType.INFO, mPresenter.getGoldName());
    }

    @Override
    public void updateInfoHeader(InfoListDataBean infoDetailBean) {
//        closeLoadingView();
        mCoordinatorLayout.setEnabled(true);
        this.mInfoMation = infoDetailBean;
        mInfoDetailHeader.setDetail(infoDetailBean);
        mInfoDetailHeader.updateDigList(infoDetailBean);
        mInfoDetailHeader.setRelateInfo(infoDetailBean);
        onNetResponseSuccess(infoDetailBean.getCommentList(), false);
    }

    @Override
    public void deleteInfo(boolean deleting, boolean success, String message) {
        if (deleting) {
            showSnackLoadingMessage(getString(R.string.info_deleting));
        } else {
            if (success) {
                EventBus.getDefault().post(mInfoMation, EVENT_UPDATE_LIST_DELETE);
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
        mInfoMation = (InfoListDataBean) getArguments().getSerializable(BUNDLE_INFO);

        if (mInfoMation == null) {
            mInfoMation = new InfoListDataBean();
            Long ids = getArguments().getLong(BUNDLE_SOURCE_ID);
            mInfoMation.setId(ids);
        }

        mTvToolbarCenter.setVisibility(View.VISIBLE);
        mTvToolbarCenter.setText(getString(R.string.info_details));
        initHeaderView();
        initBottomToolStyle();
        initBottomToolListener();
        initListener();
        mInfoMation.setIs_collection_news(mPresenter.isCollected() ? 1 : 0);
        mInfoMation.setIs_digg_news(mPresenter.isDiged() ? 1 : 0);
        setDigg(mPresenter.isDiged());

        // 投稿中的资讯隐藏底部操作以及打赏
        mDdDynamicTool.setVisibility(mInfoMation.getAudit_status() == 0 ? View.VISIBLE : View.GONE);
        mInfoDetailHeader.setInfoReviewIng(mInfoMation.getAudit_status() == 0 ? View.VISIBLE : View.GONE);
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
    public Long getNewsId() {
        return (long) mInfoMation.getId();
    }

    @Override
    public int getInfoType() {
        return Integer.valueOf(getArguments().getString(BUNDLE_INFO_TYPE, "-100"));
    }

    @Override
    public InfoListDataBean getCurrentInfo() {
        return mInfoMation;
    }

    @Override
    public void setCollect(boolean isCollected) {
//        mDdDynamicTool.setItemIsChecked(isCollected, ITEM_POSITION_0);
    }

    @Override
    public void setDigg(boolean isDigged) {
        mDdDynamicTool.setItemIsChecked(isDigged, ITEM_POSITION_0);
        if (mInfoMation.getDigList() != null) {
            mInfoDetailHeader.updateDigList(mInfoMation);
        }
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) { // 空白展位图
                InfoCommentListBean emptyData = new InfoCommentListBean();
                data.add(emptyData);
            }
        }
        super.onNetResponseSuccess(data, isLoadMore);
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
        mInfoMation.setComment_count(TextUtils.isEmpty(mListDatas.get(0).getComment_content()) ? 0 : mListDatas.size());
        mInfoDetailHeader.updateCommentView(mInfoMation);
    }

    private void initHeaderView() {
        mInfoDetailHeader = new InfoDetailHeaderView(getContext(), mPresenter.getAdvert());
        mInfoDetailHeader.setWebLoadListener(this);
        mHeaderAndFooterWrapper.addHeaderView(mInfoDetailHeader.getInfoDetailHeader());
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
                    mPresenter.handleLike(!mInfoMation.getHas_like(),
                            mInfoMation.getId() + "");
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_1:// 评论
                    showCommentView();
                    String contentHint = getString(R.string.default_input_hint);
                    mIlvComment.setEtContentHint(contentHint);
                    mReplyUserId = 0;
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_2:// 分享
                    Bitmap bitmap = FileUtils.readImgFromFile(getActivity(), "info_share");

                    mPresenter.shareInfo(bitmap);
                    break;
                case DynamicDetailMenuView.ITEM_POSITION_3:// 更多
                    initDealInfoMationPopupWindow(mInfoMation, mInfoMation.getHas_collect());
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
    private void initDeleteCommentPopupWindow(final InfoCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL && data.getId() != -1L ? getString(R.string.dynamic_list_top_comment) : null)
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 跳转置顶页面
                    mDeletCommentPopWindow.hide();
                    Bundle bundle = new Bundle();
                    bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_INFO);// 资源类型
                    bundle.putLong(StickTopFragment.PARENT_ID, mInfoMation.getId());// 资源id
                    bundle.putLong(StickTopFragment.CHILD_ID, data.getId());
                    Intent intent = new Intent(getActivity(), StickTopActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    mDeletCommentPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteComment(data);
                    }, true);

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    private void initDealInfoMationPopupWindow(final InfoListDataBean infoMation, boolean isCollected) {
        boolean isMine = infoMation.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
        mDealInfoMationPopWindow = ActionPopupWindow.builder()
                .item1Str(isMine ? getString(R.string.info_apply_for_top) : "")
                .item2Str(isMine ? getString(R.string.info_delete) : getString(isCollected ? R.string.dynamic_list_uncollect_dynamic : R.string
                        .dynamic_list_collect_dynamic))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {
                    // 收藏
                    // 如果是自己发布的，则不能收藏只能删除
                    if (isMine) {
                        showDeleteTipPopupWindow(getString(R.string.delete_info), () -> {
                            mPresenter.deleteInfo();
                        }, true);
                    } else {
                        mPresenter.handleCollect(!infoMation.getHas_collect(),
                                mInfoMation.getId() + "");
                    }
                    mDealInfoMationPopWindow.hide();
                })
                // 申请置顶
                .item1ClickListener(() -> {
                    if (infoMation.is_pinned()) {
                        showSnackErrorMessage(getString(R.string.info_alert_reapply_for_top));
                    } else {
                        // 跳转置顶页面
                        Bundle bundle = new Bundle();
                        // 资源类型
                        bundle.putString(StickTopFragment.TYPE, StickTopFragment.TYPE_INFO);
                        // 资源id
                        bundle.putLong(StickTopFragment.PARENT_ID, infoMation.getId());
                        Intent intent = new Intent(getActivity(), StickTopActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    mDealInfoMationPopWindow.hide();
                })
                .bottomClickListener(() -> mDealInfoMationPopWindow.hide())
                .build();
    }

    @Override
    public void onLoadFinish() {
        closeLoadingView();
    }


    class ItemOnCommentListener implements InfoDetailCommentItem.OnCommentItemListener {
        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
            InfoCommentListBean infoCommentListBean = mListDatas.get(position);
            if (infoCommentListBean != null && !TextUtils.isEmpty(infoCommentListBean.getComment_content())) {
                if (infoCommentListBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {// 自己的评论
                    if (mListDatas.get(position).getId() != -1) {
                        initDeleteCommentPopupWindow(infoCommentListBean);
                        mDeletCommentPopWindow.show();
                    }
//                else {
//
//                    return;
//                }
                } else {
                    mReplyUserId = (int) infoCommentListBean.getUser_id();
                    showCommentView();
                    String contentHint = getString(R.string.default_input_hint);
                    if (infoCommentListBean.getReply_to_user_id() != mInfoMation.getId()) {
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
                mPresenter.reqReWardsData(mInfoMation.getId().intValue());
            }
        }

    }

    @Override
    public void onPause() {
        mInfoDetailHeader.getContentWebView().onPause();
        mInfoDetailHeader.getContentWebView().pauseTimers();
        mInfoDetailHeader.getContentSubWebView().onPause();
        mInfoDetailHeader.getContentSubWebView().pauseTimers();
        super.onPause();

    }

    @Override
    public void onResume() {
        mInfoDetailHeader.getContentWebView().onResume();
        mInfoDetailHeader.getContentWebView().resumeTimers();
        mInfoDetailHeader.getContentSubWebView().onResume();
        mInfoDetailHeader.getContentSubWebView().resumeTimers();
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInfoDetailHeader.destroyedWeb();
    }
}
