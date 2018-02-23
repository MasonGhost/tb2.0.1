package com.zhiyicx.thinksnsplus.modules.chat.item.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.presenter.EaseChatTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowTipText;

/**
 * @author Jliuer
 * @Date 18/02/06 10:08
 * @Email Jliuer@aliyun.com
 * @Description  文本消息，提示信息
 */
public class TSChatTipTextPresenter extends EaseChatTextPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        return new ChatRowTipText(cxt, message, position, adapter, userInfoBean);
    }
}
