package com.zhiyicx.thinksnsplus.modules.chat.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.presenter.EaseChatTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowText;

/**
 * @author Catherine
 * @describe 纯文字聊天
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class TSChatTextPresenter extends EaseChatTextPresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        return new ChatRowText(cxt, message, position, adapter, userInfoBean);
    }
}
