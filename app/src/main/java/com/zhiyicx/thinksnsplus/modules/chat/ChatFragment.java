package com.zhiyicx.thinksnsplus.modules.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.widget.chat.ChatMessageList;

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
public class ChatFragment extends TSFragment<ChatContract.Presenter> implements ChatContract.View, InputLimitView.OnSendClickListener, BGARefreshLayout.BGARefreshLayoutDelegate, ChatMessageList.MessageListItemClickListener {
    public static final String BUNDLE_MESSAGEITEMBEAN = "MessageItemBean";

    @BindView(R.id.message_list)
    ChatMessageList mMessageList;
    @BindView(R.id.ilv_container)
    InputLimitView mIlvContainer;

    protected List<ChatItemBean> mDatas = new ArrayList<>();
    private MessageItemBean mMessageItemBean;

    public static ChatFragment newInstance(MessageItemBean messageItemBean) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MESSAGEITEMBEAN, messageItemBean);
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
        mIlvContainer.setOnSendClickListener(this);
    }

    @Override
    protected void initData() {
        getIntentData();
        mDatas.addAll(mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), 0));
//        for (int i = 0; i < 10; i++) {
//            ChatItemBean chatItemBean = new ChatItemBean();
//            Message message = new Message();
//            message.setMid(System.currentTimeMillis());
//            message.setId(i);
//            message.setCreate_time(System.currentTimeMillis());
//            message.setTxt("测试消息，我的看了个 " + i);
//            if (i % 2 == 0) {
//                message.setType(-1);
//            }
//            chatItemBean.setLastMessage(message);
//            mDatas.add(chatItemBean);
//        }
        mMessageList.setMessageListItemClickListener(this);
        mMessageList.init(mMessageItemBean.getConversation().getType() == ChatType.CHAT_TYPE_PRIVATE ? mMessageItemBean.getUserInfo().getName() : getString(R.string.default_message_group)
                , mMessageItemBean.getConversation().getType(), mDatas);
        mMessageList.setBGARefreshLayoutDelegate(this);
    }

    private void getIntentData() {
        mMessageItemBean = getArguments().getParcelable(BUNDLE_MESSAGEITEMBEAN);
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
        mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), 0);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return true;
    }

    /**
     * 发送按钮被点击
     *
     * @param text
     */
    @Override
    public void onSendClick(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     *
     * @param message
     */
    @Override
    public void onStatusClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
    }

    /**
     * 聊天气泡点击
     *
     * @param message
     */
    @Override
    public void onBubbleClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());

    }

    /**
     * 聊天气泡长按
     *
     * @param message
     * @return
     */
    @Override
    public boolean onBubbleLongClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }

    /**
     * 用户信息点击
     *
     * @param username
     */
    @Override
    public void onUserInfoClick(String username) {
        showMessage(username);
    }

    /**
     * 用户信息长按
     *
     * @param username
     * @return
     */
    @Override
    public boolean onUserInfoLongClick(String username) {
        showMessage(username);
        return true;
    }

    /**
     * item 点击
     *
     * @param message
     */
    @Override
    public void onItemClickListener(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
    }

    /**
     * item 长按
     *
     * @param message
     * @return
     */
    @Override
    public boolean onItemLongClickListener(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }


}
