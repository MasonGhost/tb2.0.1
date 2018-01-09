package com.zhiyicx.thinksnsplus.modules.chat.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.presenter.EaseChatLocationPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowLocation;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/9
 * @contact email:648129313@qq.com
 */

public class TSChatLocationPresenter extends EaseChatLocationPresenter{

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        return new ChatRowLocation(cxt, message, position, adapter, userInfoBean);
    }
}
