package com.zhiyicx.baseproject.widget.chat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatMessageList extends FrameLayout {
    private static final String TAG = ChatMessageList.class.getSimpleName();
    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected Conversation conversation;
    protected int chatType;
    protected String toChatUsername;
    protected MessageAdapter messageAdapter;
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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseChatMessageList);
        showAvatar = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserAvatar, true);
        myBubbleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        otherBuddleBg = ta.getDrawable(R.styleable.EaseChatMessageList_msgListMyBubbleBackground);
        showUserNick = ta.getBoolean(R.styleable.EaseChatMessageList_msgListShowUserNick, false);
        ta.recycle();
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_chat_message_list, this);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.rl_chat_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_chat_list);
    }

    /**
     * init widget
     * @param toChatUsername
     * @param chatType
     * @param customChatRowProvider
     */
    public void init(String toChatUsername, int chatType, EaseCustomChatRowProvider customChatRowProvider) {
        this.chatType = chatType;
        this.toChatUsername = toChatUsername;

        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        messageAdapter = new EaseMessageAdapter(context, toChatUsername, chatType, listView);
        messageAdapter.setShowAvatar(showAvatar);
        messageAdapter.setShowUserNick(showUserNick);
        messageAdapter.setMyBubbleBg(myBubbleBg);
        messageAdapter.setOtherBuddleBg(otherBuddleBg);
        messageAdapter.setCustomChatRowProvider(customChatRowProvider);
        // set message adapter
        mRecyclerView.setAdapter(messageAdapter);

        refreshSelectLast();
    }

    /**
     * refresh
     */
    public void refresh(){
        if (messageAdapter != null) {
            messageAdapter.refresh();
        }
    }

    /**
     * refresh and jump to the last
     */
    public void refreshSelectLast(){
        if (messageAdapter != null) {
            messageAdapter.refreshSelectLast();
        }
    }

    /**
     * refresh and jump to the position
     * @param position
     */
    public void refreshSeekTo(int position){
        if (messageAdapter != null) {
            messageAdapter.refreshSeekTo(position);
        }
    }

    public ListView getListView() {
        return listView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return swipeRefreshLayout;
    }

    public Message getItem(int position){
        return messageAdapter.getItem(position);
    }

    public void setShowUserNick(boolean showUserNick){
        this.showUserNick = showUserNick;
    }

    public boolean isShowUserNick(){
        return showUserNick;
    }

    public interface MessageListItemClickListener{
        void onResendClick(Message message);
        /**
         * there is default handling when bubble is clicked, if you want handle it, return true
         * another way is you implement in onBubbleClick() of chat row
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
     * @param listener
     */
    public void setItemClickListener(MessageListItemClickListener listener){
        if (messageAdapter != null) {
            messageAdapter.setItemClickListener(listener);
        }
    }
}
