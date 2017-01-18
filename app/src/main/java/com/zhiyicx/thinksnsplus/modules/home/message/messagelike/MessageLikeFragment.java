package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItem;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.TSPRefreshViewHolder;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageLikeFragment extends TSFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final float LIST_ITEM_SPACING = 1f;

    @BindView(R.id.rv_like_list)
    RecyclerView mRvLikeList;
    @BindView(R.id.refreshlayout_message_like)
    BGARefreshLayout mRefreshlayoutMessageLike;

    private ImageLoader mImageLoader;
    private List<MessageItem> mMessageItems;

    public MessageLikeFragment() {
    }

    public static MessageLikeFragment newInstance() {
        MessageLikeFragment fragment = new MessageLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_message_like;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }


    @Override
    protected String setCenterTitle() {
        return getString(R.string.like);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
//        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
    }


    @Override
    protected void initData() {
        mRefreshlayoutMessageLike.setDelegate(this);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMessageItems = new ArrayList<>();
        initCommentAndLike(mMessageItems);
        mRvLikeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvLikeList.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), LIST_ITEM_SPACING), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvLikeList.setHasFixedSize(true);
        mRvLikeList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRvLikeList.setAdapter(new CommonAdapter<MessageItem>(getActivity(), R.layout.item_message_like_list, mMessageItems) {
            @Override
            protected void convert(ViewHolder holder, MessageItem messageItem, int position) {
                setItemData(holder, messageItem, position);
            }

        });
        mRefreshlayoutMessageLike.setRefreshViewHolder(new TSPRefreshViewHolder(getActivity(), true));
    }

    /**
     * 评论的和点赞的数据
     */
    private void initCommentAndLike(List<MessageItem> messageItems) {
        UserInfoBean testUserinfo = new UserInfoBean();
        testUserinfo.setUserIcon("http://image.xinmin.cn/2017/01/11/bedca80cdaa44849a813e7820fff8a26.jpg");
        testUserinfo.setUserName("颤三");
        testUserinfo.setUserId("123");
        MessageItem commentItem = new MessageItem();
        commentItem.setUserInfo(testUserinfo);
        Message commentMessage = new Message();
        commentMessage.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                + getString(R.string.comment_me));
        commentMessage.setCreate_time(System.currentTimeMillis());
        commentItem.setLastMessage(commentMessage);
        commentItem.setUnReadMessageNums(Math.round(15));
        messageItems.add(commentItem);

        MessageItem likedmessageItem = new MessageItem();
        likedmessageItem.setUserInfo(testUserinfo);
        Message likeMessage = new Message();
        likeMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        likeMessage.setCreate_time(System.currentTimeMillis());
        likedmessageItem.setLastMessage(likeMessage);
        likedmessageItem.setUnReadMessageNums(Math.round(15));
        messageItems.add(likedmessageItem);
        MessageItem test = new MessageItem();

        test.setUserInfo(testUserinfo);
        Message testMessage = new Message();
        testMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        testMessage.setCreate_time(System.currentTimeMillis());
        test.setLastMessage(likeMessage);
        test.setUnReadMessageNums((int) (Math.random() * 10));
        messageItems.add(test);
    }

    /**
     * 设置item 数据
     *
     * @param holder      控件管理器
     * @param messageItem 当前数据
     * @param position    当前数据位置
     */

    private void setItemData(ViewHolder holder, final MessageItem messageItem, int position) {
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(messageItem.getUserInfo().getUserIcon())
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        if (position % 2 == 0) {
            holder.setVisible(R.id.tv_deatil, View.GONE);
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(messageItem.getUserInfo().getUserIcon())
                    .imagerView((ImageView) holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
            holder.setVisible(R.id.tv_deatil, View.VISIBLE);
            holder.setText(R.id.tv_deatil, messageItem.getLastMessage().getTxt());
        }

        holder.setText(R.id.tv_name, messageItem.getUserInfo().getUserName());
        holder.setText(R.id.tv_time, ConvertUtils.millis2FitTimeSpan(messageItem.getLastMessage().getCreate_time(), 3));
        // 响应事件
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
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItem);
                    }
                });

        RxView.clicks(holder.getView(R.id.iv_detail_image))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItem);
                    }
                });
        RxView.clicks(holder.getView(R.id.tv_deatil))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItem);
                    }
                });
    }

    /**
     * 进入聊天页
     *
     * @param messageItem
     */
    private void toChat(MessageItem messageItem) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChatFragment.BUNDLE_USERID, messageItem.getUserInfo().getUserId());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
