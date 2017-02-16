package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
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
public class MessageFragment extends TSListFragment<MessageContract.Presenter, MessageItemBean> implements MessageContract.View{
    private static final int ITEM_TYPE_COMMNETED = 0;
    private static final int ITEM_TYPE_LIKED = 1;

    private View mHeaderView;
    @Inject
    protected MessagePresenter mMessagePresenter;
    private ImageLoader mImageLoader;
    private List<MessageItemBean> mMessageItemBeen = new ArrayList<>();

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
    protected String setRightTitle() {
        return "创建对话";//测试使用
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mPresenter.createChat();
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
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        super.initData();// 需要在 dagger 注入后
        updateHeaderViewData(mHeaderView, mPresenter.updateCommnetItemData(), mPresenter.updateLikeItemData());
    }

    @Override
    protected CommonAdapter getAdapter() {
        return new CommonAdapter<MessageItemBean>(getActivity(), R.layout.item_message_list, mMessageItemBeen) {
            @Override
            protected void convert(ViewHolder holder, MessageItemBean messageItemBean, int position) {
                setItemData(holder, messageItemBean, position);
            }

        };
    }

    @Override
    protected boolean insertOrUpdateData(@NotNull List data) {
        return false;
    }



    /**
     * 初始化头信息（评论的、赞过的）
     */
    private void initHeaderView() {
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

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
                        }
                    });
            liked = headerview.findViewById(R.id.rl_liked);
            RxView.clicks(liked)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            toLikeList();
                        }
                    });
            tvHeaderCommentContent = (TextView) headerview.findViewById(R.id.tv_header_comment_content);
            tvHeaderCommentTime = (TextView) headerview.findViewById(R.id.tv_header_comment_time);
            tvHeaderCommentTip = (BadgeView) headerview.findViewById(R.id.tv_header_comment_tip);

            tvHeaderLikeContent = (TextView) headerview.findViewById(R.id.tv_header_like_content);
            tvHeaderLikeTime = (TextView) headerview.findViewById(R.id.tv_header_like_time);
            tvHeaderLikeTip = (BadgeView) headerview.findViewById(R.id.tv_header_like_tip);
        }
        tvHeaderCommentContent.setText(commentItemData.getConversation().getLast_message_text());
        tvHeaderCommentTime.setText(ConvertUtils.millis2FitTimeSpan(commentItemData.getConversation().getLast_message_time(), 3));
        tvHeaderCommentTip.setBadgeCount(commentItemData.getUnReadMessageNums());

        tvHeaderLikeContent.setText(likedItemData.getConversation().getLast_message_text());
        tvHeaderLikeTime.setText(ConvertUtils.millis2FitTimeSpan(likedItemData.getConversation().getLast_message_time(), 3));
        tvHeaderLikeTip.setBadgeCount(likedItemData.getUnReadMessageNums());
        refreshData();
    }

    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param messageItemBean 当前数据
     * @param position        当前数据位置
     */

    private void setItemData(ViewHolder holder, final MessageItemBean messageItemBean, int position) {
        switch (messageItemBean.getConversation().getType()) {
            case ChatType.CHAT_TYPE_PRIVATE:// 私聊
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .url(messageItemBean.getUserInfo().getUserIcon())
                        .transformation(new GlideCircleTransform(getContext()))
                        .errorPic(R.drawable.shape_default_image_circle)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                        .build()
                );
                holder.setText(R.id.tv_name, messageItemBean.getUserInfo().getName());     // 响应事件
                RxView.clicks(holder.getView(R.id.tv_name))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                toUserCenter();
                            }
                        });
                RxView.clicks(holder.getView(R.id.iv_headpic))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                toUserCenter();
                            }
                        });

                break;
            case ChatType.CHAT_TYPE_GROUP:// 群组
                holder.setImageResource(R.id.iv_headpic, R.drawable.shape_default_image_circle);
                holder.setText(R.id.tv_name, TextUtils.isEmpty(messageItemBean.getConversation().getName())
                        ? getString(R.string.default_message_group) : messageItemBean.getConversation().getName());
                break;
            default:
        }
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItemBean);
                    }
                });
//        }

        holder.setText(R.id.tv_content, messageItemBean.getConversation().getLast_message_text());
        holder.setText(R.id.tv_time, ConvertUtils.millis2FitTimeSpan(messageItemBean.getConversation().getLast_message_time(), 3));
        ((BadgeView) holder.getView(R.id.tv_tip)).setBadgeCount(messageItemBean.getUnReadMessageNums());

    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean
     */
    private void toChat(MessageItemBean messageItemBean) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        to.putExtras(bundle);
        startActivity(to);
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(to);
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

    // Fragment 注入 ,不需要该方法
    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void updateCommnetItemData(MessageItemBean messageItemBean) {
        mMessageItemBeen.set(ITEM_TYPE_COMMNETED, messageItemBean);
    }

    @Override
    public void updateLikeItemData(MessageItemBean messageItemBean) {
        mMessageItemBeen.set(ITEM_TYPE_LIKED, messageItemBean);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.endRefreshing();
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

}
