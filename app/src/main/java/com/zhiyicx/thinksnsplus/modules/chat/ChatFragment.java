package com.zhiyicx.thinksnsplus.modules.chat;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.thinksnsplus.widget.chat.ChatMessageList;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
public class ChatFragment extends TSFragment<ChatContract.Presenter> implements ChatContract.View, BGARefreshLayout.BGARefreshLayoutDelegate, ChatMessageList.MessageListItemClickListener {
    public static final String BUNDLE_USERID = "userId";
    public static final String BUNDLE_CONVERSATION = "conversation";

    protected List<ChatItemBean> mDatas = new ArrayList<>();
    @BindView(R.id.message_list)
    ChatMessageList mMessageList;
    @BindView(R.id.ilv_container)
    InputLimitView mIlvContainer;

    private Conversation mConversation;
    private long mTochatUsreId;// 聊天对方用户的id

    public static ChatFragment newInstance(long userId, Conversation conversation) {

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_CONVERSATION, conversation);
        args.putLong(BUNDLE_USERID, userId);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        getIntentData();
        mDatas.addAll(mPresenter.getHistoryMessages(mConversation.getCid(), 0));
        for (int i = 0; i < 10; i++) {
            ChatItemBean chatItemBean=new ChatItemBean();
            Message message = new Message();
            message.setMid(System.currentTimeMillis());
            message.setId(i);
            message.setCreate_time(System.currentTimeMillis());
            message.setTxt("测试消息，我的看了个的空间广阔疯狂的疯狂付款的流沙看到了 " + i);
            if (i % 2 == 0) {
                message.setType(-1);
            }
            chatItemBean.setLastMessage(message);
            mDatas.add(chatItemBean);
        }
        mMessageList.setMessageListItemClickListener(this);
        mMessageList.init("张三", ChatType.CHAT_TYPE_PRIVATE, mDatas);
        mMessageList.setBGARefreshLayoutDelegate(this);
    }

    private void getIntentData() {
        mTochatUsreId = getArguments().getLong(BUNDLE_USERID);
        mConversation = (Conversation) getArguments().getSerializable(BUNDLE_CONVERSATION);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
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
        ToastUtils.showToast(message);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
    // 聊天 item 点击事件

    @Override
    public void onStatusClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
    }

    @Override
    public void onBubbleClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());

    }

    @Override
    public boolean onBubbleLongClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }

    @Override
    public void onUserInfoClick(String username) {
        showMessage(username);
    }

    @Override
    public boolean onUserInfoLongClick(String username) {
        showMessage(username);
        return true;
    }

    @Override
    public void onItemClickListener(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
    }

    @Override
    public boolean onItemLongClickListener(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }
}
