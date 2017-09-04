package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.PinnedBean;
import com.zhiyicx.thinksnsplus.data.beans.TSNotifyExtraBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;
import static com.zhiyicx.baseproject.config.ApiConfig.NOTIFICATION_KEY_NEWS_PINNED_COMMENT;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewFragment extends TSListFragment<MessageReviewContract.Presenter,
        TSPNotificationBean> implements MessageReviewContract.View, MultiItemTypeAdapter.OnItemClickListener {

    public static final String REVIEW_LIST = "review_list";

    private ActionPopupWindow mReviewPopWindow;
    private ActionPopupWindow mInstructionsPopupWindow;

    private TSPNotificationBean mTspNotificationBean;

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
    protected CommonAdapter<TSPNotificationBean> getAdapter() {
        CommonAdapter<TSPNotificationBean> adapter = new MssageReviewAdapter
                (getContext(), R.layout.item_message_review_list, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        onNetResponseSuccess((ArrayList<TSPNotificationBean>) getArguments().getSerializable(REVIEW_LIST), isLoadMore);
    }


    @Override
    protected List<TSPNotificationBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        mTspNotificationBean = mListDatas.get(position);

        String jsonString = new Gson().toJson(mTspNotificationBean.getData().getExtra(), Map.class);
        TSNotifyExtraBean extraBean =new Gson().fromJson(jsonString,TSNotifyExtraBean.class);

        DynamicDetailBeanV2 feedBean = extraBean.getFeed();
        CommentedBean commentBean = extraBean.getComment();
        PinnedBean pinnedBean = extraBean.getPinned();
        if (commentBean == null || feedBean == null) {
            if (commentBean == null) {
                initInstructionsPop(R.string.review_comment_deleted);
            } else {
                initInstructionsPop(R.string.review_dynamic_deleted);
            }
            return;
        }
        initReviewPopWindow(mTspNotificationBean.getData());
    }

    @Override
    public TSPNotificationBean getCurrentComment() {
        return mTspNotificationBean;
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    private void initReviewPopWindow(TSPNotificationBean.DataBean dataBean) {
        Long source_id;

        String jsonString = new Gson().toJson(dataBean.getExtra(), Map.class);
        TSNotifyExtraBean extraBean =new Gson().fromJson(jsonString,TSNotifyExtraBean.class);

        switch (dataBean.getChannel()) {
            case NOTIFICATION_KEY_FEED_PINNED_COMMENT:
                source_id = extraBean.getFeed().getId();
                break;
            case NOTIFICATION_KEY_NEWS_PINNED_COMMENT:
                source_id = (long) extraBean.getNews().getId();
                break;
            default:
                source_id = null;
                break;
        }
        mReviewPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.review_approved))
                .item2Str(getString(R.string.review_refuse))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mPresenter.approvedTopComment(dataBean.getChannel(), source_id,
                            extraBean.getComment().getId().intValue(), extraBean.getPinned().getId());
                    mReviewPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mPresenter.refuseTopComment(dataBean.getChannel(), extraBean.getPinned().getId());
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
