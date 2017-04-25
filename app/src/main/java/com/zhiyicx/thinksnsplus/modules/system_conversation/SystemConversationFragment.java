package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.BaseChatFragment;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
public class SystemConversationFragment extends BaseChatFragment<SystemConversationContract.Presenter> implements SystemConversationContract.View{
    public static final String BUNDLE_MESSAGEITEMBEAN = "MessageItemBean";

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
        if (mMessageItemBean.getConversation() == null) { // 先获取本地信息，如果本地信息存在，直接使用，如果没有直接创建
            Conversation conversation = ConversationDao.getInstance(getContext()).getPrivateChatConversationByUids(AppApplication.getmCurrentLoginAuth().getUser_id(), mMessageItemBean.getUserInfo().getUser_id().intValue());
            if (conversation == null) {
            } else {
                mMessageItemBean.setConversation(conversation);
                initMessageList();
            }
        } else {
            initMessageList();
        }

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
        if (mMessageItemBean.getConversation() == null) {
            hideLoading();
            return;
        }
        mDatas.clear();
        mMessageList.refresh();
    }
}
