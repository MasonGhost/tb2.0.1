package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 音视频聊天item
 * @date 2018/1/19
 * @contact email:648129313@qq.com
 */

public class ChatRowCall extends ChatBaseRow {

    private ImageView mIvCallType;
    private TextView mTvChatContent;

    public ChatRowCall(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.item_chat_list_receive_call : R.layout.item_chat_list_send_call, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mIvCallType = (ImageView) findViewById(R.id.iv_call_type);
        mTvChatContent = (TextView) findViewById(R.id.tv_chat_content);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        mTvChatContent.setText(txtBody.getMessage());
        if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                mIvCallType.setImageResource(R.mipmap.btn_chat_greyphone);
            } else {
                mIvCallType.setImageResource(R.mipmap.btn_chat_bluephone);
            }
        } else if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                mIvCallType.setImageResource(R.mipmap.btn_chat_greyvideo);
            } else {
                mIvCallType.setImageResource(R.mipmap.btn_chat_bluevideo);
            }
        }
    }
}
