package com.zhiyicx.thinksnsplus.widget.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatMessageList extends FrameLayout {
    private static final String TAG = ChatMessageList.class.getSimpleName();
    private static final float RECYCLEVIEW_ITEMDECORATION_SPACING = 15F;

    protected BGARefreshLayout mRefreshLayout;
    protected BGARefreshLayout.BGARefreshLayoutDelegate mBGARefreshLayoutDelegate;
    protected MessageListItemClickListener mMessageListItemClickListener;

    protected RecyclerView mRecyclerView;
    protected Conversation conversation;
    protected int chatType;
    protected String toChatUsername;
    protected MultiItemTypeAdapter messageAdapter;
    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;
    protected Context mContext;

    protected MessageSendItemDelagate mMessageSendItemDelagate;
    protected MessageReceiveItemDelagate mMessageReceiveItemDelagate;

    public ChatMessageList(Context context) {
        super(context);
        init(context);
    }

    /**
     * 使用布局文件的构造函数
     *
     * @param context
     * @param attrs
     */
    public ChatMessageList(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
        init(context);
    }

    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseStyle(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseStyle(context, attrs);
        init(context);
    }

    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.ChatMessageList_showUserAvatar, true);
        myBubbleBg = ta.getDrawable(R.styleable.ChatMessageList_myBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.ChatMessageList_otherBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.ChatMessageList_showName, false);
        ta.recycle();
    }

    private void init(Context context) {
        this.mContext = context;
        if (myBubbleBg == null) {
            myBubbleBg = ContextCompat.getDrawable(mContext,R.drawable.shape_message_bubble_my);
        }
        if (otherBuddleBg == null) {
            otherBuddleBg =  ContextCompat.getDrawable(mContext,R.drawable.shape_message_bubble_other);
        }
        LayoutInflater.from(context).inflate(R.layout.view_chat_message_list, this);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_chat_refresh_layout);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), RECYCLEVIEW_ITEMDECORATION_SPACING), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, false));
    }

    /**
     * init widget
     *
     * @param toChatUsername
     * @param chatType
     */
    public void init(String toChatUsername, int chatType, List<ChatItemBean> datas) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;
        messageAdapter = new MultiItemTypeAdapter(mContext, datas);
        mMessageSendItemDelagate = new MessageSendItemDelagate(showUserNick, showAvatar, myBubbleBg);
        mMessageSendItemDelagate.setMessageListItemClickListener(mMessageListItemClickListener);
        mMessageReceiveItemDelagate = new MessageReceiveItemDelagate(showUserNick, showAvatar, otherBuddleBg);
        mMessageReceiveItemDelagate.setMessageListItemClickListener(mMessageListItemClickListener);
        messageAdapter.addItemViewDelegate(mMessageSendItemDelagate);
        messageAdapter.addItemViewDelegate(mMessageReceiveItemDelagate);
        // TODO: 2017/1/7 添加图片、视频、音频等Delegate
        // set message adapter
        mRecyclerView.setAdapter(messageAdapter);
        refreshSelectLast();
    }

    /**
     * 设置刷新监听
     *
     * @param BGARefreshLayoutDelegate
     */
    public void setBGARefreshLayoutDelegate(BGARefreshLayout.BGARefreshLayoutDelegate BGARefreshLayoutDelegate) {
        mBGARefreshLayoutDelegate = BGARefreshLayoutDelegate;
        mRefreshLayout.setDelegate(mBGARefreshLayoutDelegate);
    }

    /**
     * refresh
     */
    public void refresh() {
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast() {
//        if (messageAdapter != null) {
//            messageAdapter.refreshSelectLast();
//        }
    }

    /**
     * refresh and jump to the position
     *
     * @param position
     */
    public void refreshSeekTo(int position) {
        if (messageAdapter != null) {
            mRecyclerView.scrollToPosition(position);
        }
    }

    /**
     * 平滑的滑动到底部
     */
    public void smoothScrollToBottom() {
        mRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }

    /**
     * 直接滑动到底部
     */
    public void scrollToBottom() {
        mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }
    public RecyclerView getListView() {
        return mRecyclerView;
    }

    public BGARefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public Message getItem(int position) {
        return (Message) messageAdapter.getItem(position);
    }

    public void setShowUserNick(boolean showUserNick) {
        this.showUserNick = showUserNick;
    }

    public boolean isShowUserNick() {
        return showUserNick;
    }

    public interface MessageListItemClickListener {
        void onStatusClick(ChatItemBean chatItemBean);

        /**
         * there is default handling when bubble is clicked, if you want handle it, return true
         * another way is you implement in onBubbleClick() of chat row
         *
         * @param chatItemBean
         * @return
         */
        void onBubbleClick(ChatItemBean chatItemBean);

        boolean onBubbleLongClick(ChatItemBean chatItemBean);

        void onUserInfoClick(ChatItemBean chatItemBean);

        boolean onUserInfoLongClick(ChatItemBean chatItemBean);

        void onItemClickListener(ChatItemBean chatItemBean);

        boolean onItemLongClickListener(ChatItemBean chatItemBean);
    }

    public void setMessageListItemClickListener(MessageListItemClickListener messageListItemClickListener) {
        this.mMessageListItemClickListener = messageListItemClickListener;
    }

}
