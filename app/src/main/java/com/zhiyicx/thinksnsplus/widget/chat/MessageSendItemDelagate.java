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
        return item.getUserInfo() != null && item.getUserInfo().getUser_id() != null && item.getUserInfo().getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
    }

    @Override
    public void convert(ViewHolder holder, ChatItemBean chatItemBean, ChatItemBean lastChatItemBean, int position, int itemCounts) {
        super.convert(holder, chatItemBean, lastChatItemBean, position, itemCounts);
        // 消息状态 isAcked 是标明发出的消息他人是否已读（需要对方发出已读回执）
        // isUnread 是表面他人的消息自己是否已读的，用来判断是否需要发出已读回执
        // 此为二期功能，暂时隐藏
//        if (!chatItemBean.getMessage().isAcked()){
//            holder.setText(R.id.tv_message_status, "未读");
//        } else {
//            holder.setText(R.id.tv_message_status, "已读");
//        }
    }

}
