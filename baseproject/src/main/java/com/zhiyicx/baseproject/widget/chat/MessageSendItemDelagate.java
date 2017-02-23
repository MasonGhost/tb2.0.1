<<<<<<< HEAD
package com.zhiyicx.baseproject.widget.chat;

import android.graphics.drawable.Drawable;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageType;

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
    public boolean isForViewType(Message item, int position) {
        // TODO: 2017/1/6 需要添加是否是我的消息的判断
        return item.getType() == MessageType.MESSAGE_TYPE_TEXT;
    }


}
=======
package com.zhiyicx.baseproject.widget.chat;

import android.graphics.drawable.Drawable;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageType;

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
    public boolean isForViewType(Message item, int position) {
        // TODO: 2017/1/6 需要添加是否是我的消息的判断
        return item.getType() == MessageType.MESSAGE_TYPE_TEXT;
    }


}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
