package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.BaseChatFragment;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
public class SystemConversationFragment extends BaseChatFragment<SystemConversationContract.Presenter> implements SystemConversationContract.View {
    private long mMax_id = TSListFragment.DEFAULT_PAGE_MAX_ID;
    private boolean mIsRequestNeted = true; // 页面 是否需要进入时刷新,代表是否已经从网络获取到了数据  true 代表加载本地，false 代表需要从网络获取
    private boolean mIsFristLoadCache = true;

    public static SystemConversationFragment newInstance() {
        Bundle args = new Bundle();
        SystemConversationFragment fragment = new SystemConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ts_helper);
    }


    @Override
    protected void initData() {
        super.initData();
        initMessageList();
        mPresenter.requestCacheData(mMax_id);
    }

    @Override
    protected MessageItemBean initMessageItemBean() {
        MessageItemBean messageItemBean = new MessageItemBean();
        Conversation conversation = new Conversation();
        messageItemBean.setConversation(conversation);
        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setName(getString(R.string.ts_helper));
        messageItemBean.setUserInfo(userInfoBean);
        return messageItemBean;
    }

    /**
     * 发送按钮被点击
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mPresenter.sendTextMessage(text);
    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     */
    @Override
    protected void onResendClick(ChatItemBean chatItemBean) {
        chatItemBean.getLastMessage().setSend_status(MessageStatus.SEND_SUCCESS);
        updateSendText(chatItemBean);
        mPresenter.reSendTextMessage(chatItemBean);
    }


    @Override

    public void onRefresh() {
        if (!mIsRequestNeted) {
            mMax_id = TSListFragment.DEFAULT_PAGE_MAX_ID;
            mPresenter.requestNetData(mMax_id);
        } else {
            mPresenter.requestCacheData(mMax_id);
        }

    }

    @Override
    public void updateNetData(List<ChatItemBean> datas) {
        mIsRequestNeted = true;
        mDatas.clear();
        mDatas.addAll(datas);
        mMessageList.refreshSoomthBottom();
        setMaxId();
    }

    @Override
    public void updateCacheData(List<ChatItemBean> datas) {
        hideLoading();
        if (!mIsRequestNeted) {
            mMessageList.getRefreshLayout().setRefreshing(true);
        }
        datas.addAll(mDatas);
        mDatas.clear();
        mDatas.addAll(datas);
        mMessageList.refresh();
        if (mIsFristLoadCache) { // 第一次加载
            mMessageList.scrollToBottom();
            mIsFristLoadCache = false;
        }
        setMaxId();
    }

    @Override
    public void updateSendText(ChatItemBean chatItemBean) {
        int size = mDatas.size();
        int position = -1;
        for (int i = 0; i < size; i++) {
            if (mDatas.get(i).getLastMessage().getId() == chatItemBean.getLastMessage().getId()) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            mDatas.set(position, chatItemBean);
        } else {
            mDatas.add(chatItemBean);
        }
        mMessageList.refresh();
        mMessageList.scrollToBottom();
        setMaxId();
    }

    private void setMaxId() {
        if (mDatas.isEmpty()) {
            mMax_id = TSListFragment.DEFAULT_PAGE_MAX_ID;
        } else {
            mMax_id = mDatas.get(0).getLastMessage().getCreate_time();
        }
    }
}
