package com.zhiyicx.baseproject.widget.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

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
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatMessageList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    protected void parseStyle(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.ChatMessageList_showUserAvatar, true);
        myBubbleBg = ta.getDrawable(R.styleable.ChatMessageList_myBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.ChatMessageList_otherBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.ChatMessageList_showUserNick, false);
        ta.recycle();
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_chat_message_list, this);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_chat_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), RECYCLEVIEW_ITEMDECORATION_SPACING), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
    }

    /**
     * init widget
     *
     * @param toChatUsername
     * @param chatType
     */
    public void init(String toChatUsername, int chatType, List<String> datas) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;
        messageAdapter = new MultiItemTypeAdapter(mContext, datas);
        messageAdapter.addItemViewDelegate(new MessageSendItemDelagate(showUserNick, showAvatar, myBubbleBg, otherBuddleBg));
        messageAdapter.addItemViewDelegate(new MessageReceiveItemDelagate(showUserNick, showAvatar, myBubbleBg, otherBuddleBg));
        // TODO: 2017/1/7 添加图片、视频、音频等Delegate
        // set message adapter
        mRecyclerView.setAdapter(messageAdapter);

        refreshSelectLast();
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
        void onResendClick(Message message);

        /**
         * there is default handling when bubble is clicked, if you want handle it, return true
         * another way is you implement in onBubbleClick() of chat row
         *
         * @param message
         * @return
         */
        boolean onBubbleClick(Message message);

        void onBubbleLongClick(Message message);

        void onUserAvatarClick(String username);

        void onUserAvatarLongClick(String username);
    }

    /**
     * set click listener
     *
     * @param listener
     */
    public void setItemClickListener(MultiItemTypeAdapter.OnItemClickListener listener) {
        if (messageAdapter != null) {
            messageAdapter.setOnItemClickListener(listener);
        }
    }
}
