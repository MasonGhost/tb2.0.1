package com.zhiyicx.thinksnsplus.widget.chat;

import android.graphics.drawable.Drawable;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class MessageSendItemDelagate extends MessageTextItemDelagate {

    public MessageSendItemDelagate(boolean showName, boolean showAvatar, Drawable myBubbleBg) {
        super(showName, showAvatar, myBubbleBg, null);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_list_send_text;
    }

    /**
     * 消息类型为文本，是我发送的消息
     *
     * @param item
     * @param position
     * @return
     */
    @Override
    public boolean isForViewType(ChatItemBean item, int position) {
        // TODO: 2017/1/6 需要添加是否是我的消息的判断
        return item.getUserInfo().getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
    }


}
