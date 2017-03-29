package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoCommentAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.DynamicDetailMenuView.ITEM_POSITION_0;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment
        .BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment
        .BUNDLE_INFO_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/08
 * @Email Jliuer@aliyun.com
 * @Description 资讯详情
 */
public class InfoDetailsFragment extends TSListFragment<InfoDetailsConstract.Presenter,
        InfoCommentListBean> implements InfoDetailsConstract.View, InputLimitView
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

    private ActionPopupWindow mDeletCommentPopWindow;

    /**
     * 传入的资讯信息
     */
    private InfoListBean.ListBean mInfoMation;

    private int mReplyUserId;// 被评论者的 id ,评论动态 id = 0

    public static InfoDetailsFragment newInstance(Bundle params) {
        InfoDetailsFragment fragment = new InfoDetailsFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        InfoCommentAdapter adapter = new InfoCommentAdapter(getActivity(), mListDatas);
        adapter.setOnWebEventListener(new WebEvent());
        return adapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        mInfoMation = (InfoListBean.ListBean) getArguments().getSerializable(BUNDLE_INFO);

        mTvToolbarCenter.setVisibility(View.VISIBLE);
        mTvToolbarCenter.setText(getString(R.string.info_details));
        mTvToolbarCenter.setCompoundDrawables(null, null, null, null);

        initBottomToolStyle();
        initBottomToolListener();
        initListener();
        setCollect(mInfoMation.getIs_collection_news() == 1);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return getResources().getDimensionPixelSize(R.dimen.toolbar_and_statusbar_height);
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
    public void setPresenter(InfoDetailsConstract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public Long getNewsId() {
        return (long) mInfoMation.getId();
    }

    @Override
    public int getInfoType() {
        return Integer.valueOf(getArguments().getString(BUNDLE_INFO_TYPE));
    }

    @Override
    public InfoListBean.ListBean getCurrentInfo() {
        return mInfoMation;
    }

    @Override
    public void setCollect(boolean isCollected) {
        mDdDynamicTool.setItemIsChecked(isCollected, ITEM_POSITION_0);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            InfoCommentListBean position_zero = new InfoCommentListBean();
            position_zero.setId(mInfoMation.getId());
            data.add(0, position_zero);
            if (data.isEmpty()) {
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

    private void initBottomToolStyle() {
        mDdDynamicTool.setButtonText(new int[]{R.string.info_collect, R.string.comment,
                R.string.share, R.string.more});
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.detail_ico_good_uncollect,
                R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal, R.mipmap
                .home_ico_more});

        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.detail_ico_collect,
                R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal, R.mipmap
                .home_ico_more});
    }

    private void initBottomToolListener() {
        mDdDynamicTool.setItemOnClick(new DynamicDetailMenuView.OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View v, int postion) {
                mDdDynamicTool.getTag(R.id.view_data);
                switch (postion) {
                    case DynamicDetailMenuView.ITEM_POSITION_0:
                        mPresenter.handleCollect(mInfoMation.getIs_collection_news() == 0,
                                mInfoMation.getId() + "");
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_1:
                        showCommentView();
                        mReplyUserId = 0;
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_2:
                        mPresenter.shareInfo();
                        break;
                    case DynamicDetailMenuView.ITEM_POSITION_3:
                        ToastUtils.showToast("more");
                        break;
                }
            }
        });
    }

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
    private void initLoginOutPopupWindow(final InfoCommentListBean data) {
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
                        mPresenter.deleteComment(data);
                        mDeletCommentPopWindow.hide();
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

    private class WebEvent implements InfoCommentAdapter.OnWebEventListener {
        @Override
        public void onWebImageLongClick(String mLongClickUrl) {

        }

        @Override
        public void onWebImageClick(String url, List<String> mImageList) {

        }

        @Override
        public void onLoadFinish() {
            closeLoading();
        }

        @Override
        public void onLoadStart() {
            showLoading();
        }

        @Override
        public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth()
                    .getUser_id()) {// 自己的评论
                if (mListDatas.get(position).getId() != -1) {
                    initLoginOutPopupWindow(mListDatas.get(position));
                    mDeletCommentPopWindow.show();
                } else {
                    return;
                }
            } else {
                mReplyUserId = (int) mListDatas.get(position).getUser_id();
                showCommentView();
                String contentHint = getString(R.string.default_input_hint);
                if (mListDatas.get(position).getReply_to_user_id() != mInfoMation.getId()) {
                    contentHint = getString(R.string.reply, mListDatas.get(position).getUser_id()
                            + "");
                }
                mIlvComment.setEtContentHint(contentHint);
            }
        }
    }
}
