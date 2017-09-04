package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewFragment extends TSListFragment<MessageReviewContract.Presenter,
        TopDynamicCommentBean> implements MessageReviewContract.View, MultiItemTypeAdapter.OnItemClickListener {

    public static final String REVIEW_LIST = "review_list";

    private ActionPopupWindow mReviewPopWindow;
    private ActionPopupWindow mInstructionsPopupWindow;

    private TopDynamicCommentBean mTopDynamicCommentBean;

    public MessageReviewFragment() {
    }

    public static MessageReviewFragment newInstance(Bundle args) {
        MessageReviewFragment fragment = new MessageReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.review);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, 1, 0, 0, ContextCompat.getDrawable(getContext(), R.drawable.shape_recyclerview_divider));
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected CommonAdapter<TopDynamicCommentBean> getAdapter() {
        CommonAdapter<TopDynamicCommentBean> adapter = new MssageReviewAdapter
                (getContext(), R.layout.item_message_review_list, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        onNetResponseSuccess((ArrayList<TopDynamicCommentBean>) getArguments().getSerializable(REVIEW_LIST), isLoadMore);
    }

    @Override
    protected List<TopDynamicCommentBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        mTopDynamicCommentBean = mListDatas.get(position);
        if (mTopDynamicCommentBean.getComment() == null || mTopDynamicCommentBean.getFeed() == null) {
            if (mTopDynamicCommentBean.getComment() == null) {
                initInstructionsPop(R.string.review_comment_deleted);
            } else {
                initInstructionsPop(R.string.review_dynamic_deleted);
            }
            return;
        }
        initReviewPopWindow(mTopDynamicCommentBean);
    }

    @Override
    public TopDynamicCommentBean getCurrentComment() {
        return mTopDynamicCommentBean;
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    private void initReviewPopWindow(TopDynamicCommentBean topDynamicCommentBean) {
        mReviewPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.review_approved))
                .item2Str(getString(R.string.review_refuse))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mPresenter.approvedTopComment((long) topDynamicCommentBean.getFeed().getId(),
                            topDynamicCommentBean.getComment().getId().intValue(), topDynamicCommentBean.getId().intValue());
                    mReviewPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mPresenter.refuseTopComment(topDynamicCommentBean.getId().intValue());
                    mReviewPopWindow.hide();
                })
                .bottomClickListener(() -> mReviewPopWindow.hide())
                .build();
        mReviewPopWindow.show();
    }

    public void initInstructionsPop(int resDesStr) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .desStr(getString(resDesStr))
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.instructions))
                .desStr(getString(resDesStr))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }
}
