package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeActivity;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragment extends TSListFragment<MessageContract.Presenter, MessageItemBean> implements MessageContract.View, MessageAdapter.OnSwipItemClickListener {
    private static final int ITEM_TYPE_COMMNETED = 0;
    private static final int ITEM_TYPE_LIKED = 1;

    private View mHeaderView;

    @Inject
    protected MessagePresenter mMessagePresenter;

    private int mLastClickPostion = -1;// 纪录上次聊天 item ,用于单条刷新

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initHeaderView();
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    /**
     * 是否需要上拉加载
     *
     * @return true 是需要
     */
    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initData() {
        DaggerMessageComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messagePresenterModule(new MessagePresenterModule(this))
                .build()
                .inject(this);
        super.initData();// 需要在 dagger 注入后
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLastClickPostion != -1) {
            // 刷新当条信息内容
            mPresenter.refreshLastClicikPostion(mLastClickPostion);
            mLastClickPostion = -1;
        } else {
            refreshData();
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {

//        MessageSwipeAdapter commonAdapter =new MessageSwipeAdapter(getContext(),mListDatas);
        MessageAdapter commonAdapter = new MessageAdapter(getActivity(), R.layout.item_message_list, mListDatas);
        commonAdapter.setOnSwipItemClickListener(this);
        return commonAdapter;
    }

    /**
     * 初始化头信息（评论的、赞过的）
     */
    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.view_header_message_list, null);
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    /**
     * 更新 hederview 数据
     *
     * @param headerview
     */
    private void updateHeaderViewData(View headerview, MessageItemBean commentItemData, MessageItemBean likedItemData) {
        View rlCritical = null;
        View liked;
        TextView tvHeaderCommentContent = null;
        TextView tvHeaderCommentTime = null;
        BadgeView tvHeaderCommentTip = null;
        TextView tvHeaderLikeContent = null;
        TextView tvHeaderLikeTime = null;
        BadgeView tvHeaderLikeTip = null;
        if (rlCritical == null) {
            rlCritical = headerview.findViewById(R.id.rl_critical);
            RxView.clicks(rlCritical)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            toCommentList();
                            mPresenter.readMessageByKey(ApiConfig.FLUSHMESSAGES_KEY_COMMENTS);
                        }
                    });
            liked = headerview.findViewById(R.id.rl_liked);
            RxView.clicks(liked)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            toLikeList();
                            mPresenter.readMessageByKey(ApiConfig.FLUSHMESSAGES_KEY_DIGGS);
                        }
                    });
            tvHeaderCommentContent = (TextView) headerview.findViewById(R.id.tv_header_comment_content);
            tvHeaderCommentTime = (TextView) headerview.findViewById(R.id.tv_header_comment_time);
            tvHeaderCommentTip = (BadgeView) headerview.findViewById(R.id.tv_header_comment_tip);

            tvHeaderLikeContent = (TextView) headerview.findViewById(R.id.tv_header_like_content);
            tvHeaderLikeTime = (TextView) headerview.findViewById(R.id.tv_header_like_time);
            tvHeaderLikeTip = (BadgeView) headerview.findViewById(R.id.tv_header_like_tip);
        }
        tvHeaderCommentContent.setText(commentItemData.getConversation().getLast_message().getTxt());
        if(commentItemData.getConversation().getLast_message_time()==0){
            tvHeaderCommentTime.setVisibility(View.INVISIBLE);
        }else {
            tvHeaderCommentTime.setVisibility(View.VISIBLE);
            tvHeaderCommentTime.setText(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(commentItemData.getConversation().getLast_message_time())));
        }
        tvHeaderCommentTip.setBadgeCount(commentItemData.getUnReadMessageNums());

        tvHeaderLikeContent.setText(likedItemData.getConversation().getLast_message().getTxt());
        if(likedItemData.getConversation().getLast_message_time()==0){
            tvHeaderLikeTime.setVisibility(View.INVISIBLE);
        }else {
            tvHeaderLikeTime.setVisibility(View.VISIBLE);
            tvHeaderLikeTime.setText(TimeUtils.getTimeFriendlyNormal(TimeUtils.millis2String(likedItemData.getConversation().getLast_message_time())));
        }
        tvHeaderLikeTip.setBadgeCount(likedItemData.getUnReadMessageNums());
        refreshData();
    }


    /**
     * 前往评论列表
     */
    private void toCommentList() {
        Intent to = new Intent(getActivity(), MessageCommentActivity.class);
        startActivity(to);
    }

    /**
     * 前往点赞列表
     */
    private void toLikeList() {
        Intent to = new Intent(getActivity(), MessageLikeActivity.class);
        startActivity(to);
    }

    @Override
    public void updateCommnetItemData(MessageItemBean messageItemBean) {
//        mListDatas.set(ITEM_TYPE_COMMNETED, messageItemBean); 以 item 的形式呈现
        updateHeaderViewData(mHeaderView, mPresenter.updateCommnetItemData(), mPresenter.updateLikeItemData());
        refreshData();
    }

    @Override
    public void updateLikeItemData(MessageItemBean messageItemBean) {
//        mListDatas.set(ITEM_TYPE_LIKED, messageItemBean);
        updateHeaderViewData(mHeaderView, mPresenter.updateCommnetItemData(), mPresenter.updateLikeItemData());
        refreshData();
    }

    @Override
    public void refreshData() {
        super.refreshData();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void refreshData(int index) {
        mHeaderAndFooterWrapper.notifyItemChanged(index);
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }


    /**
     * 进入聊天页
     *
     * @param messageItemBean 当前 item 内容
     * @param positon         当前点击位置
     */
    private void toChat(MessageItemBean messageItemBean, int positon) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        to.putExtras(bundle);
        startActivity(to);
        mLastClickPostion = positon;//
    }

    @Override
    public void onLeftClick(int position) {
        position = position - 1;// 减去 header
        toChat(mListDatas.get(position), position);
    }

    @Override
    public void onRightClick(int position) {
        position = position - 1;// 减去 header
        mPresenter.deletConversation(mListDatas.get(position));
        mListDatas.remove(position);
        refreshData();
    }
}
