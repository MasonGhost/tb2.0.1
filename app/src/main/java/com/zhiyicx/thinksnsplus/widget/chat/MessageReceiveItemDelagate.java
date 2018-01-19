package com.zhiyicx.thinksnsplus.widget.chat;

import android.graphics.drawable.Drawable;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class MessageReceiveItemDelagate extends MessageTextItemDelagate {

    public MessageReceiveItemDelagate(boolean showName, boolean showAvatar, Drawable otherBuddleBg) {
        super(showName, showAvatar, null, otherBuddleBg);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_chat_list_receive_text;
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
        return item.getUserInfo() == null || item.getUserInfo().getUser_id() == null || item.getUserInfo().getUser_id() != AppApplication.getMyUserIdWithdefault();
    }
}
