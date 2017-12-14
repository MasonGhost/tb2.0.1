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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.hyphenate.chat.EMConversation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wcy.overscroll.OverScrollCheckListener;
import com.wcy.overscroll.OverScrollLayout;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatMessageList extends FrameLayout implements OnRefreshListener {
    private static final String TAG = ChatMessageList.class.getSimpleName();
    private static final float RECYCLEVIEW_ITEMDECORATION_SPACING = 15F;

    protected SmartRefreshLayout mRefreshLayout;
    protected OnRefreshListener mOnRefreshListener;
    protected MessageListItemClickListener mMessageListItemClickListener;

    protected RecyclerView mRecyclerView;
    protected Conversation conversation;
    protected EMConversation.EMConversationType chatType;
    protected String toChatUsername;
    protected MultiItemTypeAdapter messageAdapter;
    protected boolean showUserNick;
    protected boolean showAvatar;
    protected Drawable myBubbleBg;
    protected Drawable otherBuddleBg;
    protected Context mContext;

    protected MessageSendItemDelagate mMessageSendItemDelagate;
    protected MessageReceiveItemDelagate mMessageReceiveItemDelagate;
    private LinearLayoutManager mLinearLayoutManager;

    private int mLastVisibleItemPosition;//　记录上次加载的最有一个 item
    private boolean mIsHandledDrag;// 标记是否已经处理过拖动事件了
    private OverScrollLayout overscroll;

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
            myBubbleBg = ContextCompat.getDrawable(mContext, R.drawable.shape_message_bubble_my);
        }
        if (otherBuddleBg == null) {
            otherBuddleBg = ContextCompat.getDrawable(mContext, R.drawable.shape_message_bubble_other);
        }
        LayoutInflater.from(context).inflate(R.layout.view_chat_message_list, this);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshlayout);

        mRecyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mRecyclerView.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), RECYCLEVIEW_ITEMDECORATION_SPACING), 0, 0),-1);//设置最后一个 Item 的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(this);
        // 因为增加了过度拉动动画，故取消此监听
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.d("event = " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (!mIsHandledDrag) {
                            mIsHandledDrag = true;
                            if (handleSoftInput(v)) {
                                return false;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIsHandledDrag = false;
                        if (handleSoftInput(v)) {
                            return false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (handleSoftInput(v)) {
                            return false;
                        }
                        break;
                    default:
                }
                return false;
            }
        });
        overscroll = (OverScrollLayout)findViewById(R.id.overscroll);
        overscroll.setOverScrollCheckListener(new OverScrollCheckListener() {
            @Override
            public int getContentViewScrollDirection() {
                return OverScrollLayout.SCROLL_VERTICAL;
            }

            @Override
            public boolean canScrollUp() {
                if (mRefreshLayout.isEnableRefresh()) {
                    return true;
                } else {
                    // 如果不能够下拉刷新，并且到了顶部 就可以scrollUp
                    if (!mRecyclerView.canScrollVertically(-1)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean canScrollDown() {
                // 如果能够上拉加载，就不能够overScroll Down
                if (mRefreshLayout.isEnableLoadmore()) {
                    return true;
                } else {
                    // 如果不能够上拉加载，并且到了底部 就可以scrollUp
                    if (!mRecyclerView.canScrollVertically(1)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public boolean canScrollLeft() {
                return false;
            }

            @Override
            public boolean canScrollRight() {
                return false;
            }
        });
    }

    private boolean handleSoftInput(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) mRecyclerView.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    v.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    /**
     * init widget
     *
     * @param toChatUsername
     * @param chatType
     */
    public void init(String toChatUsername, EMConversation.EMConversationType chatType, List<ChatItemBean> datas) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;
        messageAdapter = new MultiItemTypeAdapter(mContext, datas);
        mMessageSendItemDelagate = new MessageSendItemDelagate(showUserNick, showAvatar, myBubbleBg);
        mMessageSendItemDelagate.setMessageListItemClickListener(mMessageListItemClickListener);
        mMessageReceiveItemDelagate = new MessageReceiveItemDelagate(showUserNick, showAvatar, otherBuddleBg);
        mMessageReceiveItemDelagate.setMessageListItemClickListener(mMessageListItemClickListener);
        messageAdapter.addItemViewDelegate(mMessageSendItemDelagate);
        messageAdapter.addItemViewDelegate(mMessageReceiveItemDelagate);
        messageAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DeviceUtils.hideSoftKeyboard(mContext, mRecyclerView);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                DeviceUtils.hideSoftKeyboard(mContext, mRecyclerView);
                return false;
            }
        });

        // TODO: 2017/1/7 添加图片、视频、音频等Delegate
        mRecyclerView.setAdapter(messageAdapter);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(300);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if ((mLastVisibleItemPosition == (messageAdapter.getItemCount() - 1))) {
                        DeviceUtils.showSoftKeyboard(mContext, mRecyclerView);
                    }
                    mLastVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 设置刷新监听
     *
     * @param l
     */
    public void setRefreshListener(OnRefreshListener l) {
        mOnRefreshListener = l;
    }

    /**
     * refresh
     */
    public void refreshSoomthBottom() {
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
//            messageAdapter.notifyItemInserted(messageAdapter.getItemCount()-1);
            smoothScrollToBottom();

        }
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
     * refresh and jump to the position
     *
     * @param position
     */
    public void refresh(int position) {
        messageAdapter.notifyItemChanged(position);
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
        if (messageAdapter != null){
            mRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

    public RecyclerView getListView() {
        return mRecyclerView;
    }

    public SmartRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    public MultiItemTypeAdapter getMessageAdapter() {
        return messageAdapter;
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

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh(refreshlayout);
        }
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
    }

    public void setMessageListItemClickListener(MessageListItemClickListener messageListItemClickListener) {
        this.mMessageListItemClickListener = messageListItemClickListener;
    }

}
