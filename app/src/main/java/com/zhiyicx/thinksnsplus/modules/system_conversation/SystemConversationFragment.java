package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.BaseChatFragment;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
public class SystemConversationFragment extends BaseChatFragment<SystemConversationContract.Presenter> implements SystemConversationContract.View {

    private boolean mIsLoadMore = false;
    private long mMax_id = TSListFragment.DEFAULT_PAGE_MAX_ID;

    public static SystemConversationFragment newInstance() {
        Bundle args = new Bundle();
        SystemConversationFragment fragment = new SystemConversationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return "";
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected MessageItemBean initMessageItemBean() {
        return null;
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
    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     *
     * @param chatItemBean
     */
    @Override
    public void onStatusClick(ChatItemBean chatItemBean) {

    }

    @Override
    public void onRefresh() {
        mPresenter.requestNetData(mMax_id, mIsLoadMore);
    }
}
