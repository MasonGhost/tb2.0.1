package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 图片消息
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class ChatRowPicture extends ChatBaseRow {

    private AppCompatImageView mIvChatContent;

    public ChatRowPicture(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean chatUserInfoBean) {
        super(context, message, position, adapter, chatUserInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.SEND ?
                R.layout.item_chat_list_send_picture : R.layout.item_chat_list_receive_picture, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mIvChatContent = (AppCompatImageView) findViewById(R.id.iv_chat_content);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {

    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMImageMessageBody imageMessageBody = (EMImageMessageBody) message.getBody();
        // 图片内容
        Glide.with(getContext())
                .load(imageMessageBody.getRemoteUrl())
                .placeholder(R.drawable.ease_default_image)
                .error(R.drawable.ease_default_image)
                .into(mIvChatContent);
    }
}
