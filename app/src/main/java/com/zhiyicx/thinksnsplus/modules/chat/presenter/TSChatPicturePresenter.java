package com.zhiyicx.thinksnsplus.modules.chat.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.presenter.EaseChatImagePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowPicture;

/**
 * @author Catherine
 * @describe 图片消息
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class TSChatPicturePresenter extends EaseChatImagePresenter {

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        return new ChatRowPicture(cxt, message, position, adapter, userInfoBean);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);
    }
}
