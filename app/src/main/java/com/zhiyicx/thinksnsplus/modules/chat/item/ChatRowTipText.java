package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.text.Spannable;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;

import static com.zhiyicx.thinksnsplus.widget.chat.MessageTextItemDelagate.MAX_SPACING_TIME;


/**
 * @author Jliuer
 * @Date 18/02/06 14:01
 * @Email Jliuer@aliyun.com
 * @Description 文本消息，提示信息
 */
public class ChatRowTipText extends ChatBaseRow {
    private TextView mTvChatContent;

    public ChatRowTipText(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.include_chat_extra, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mTvChatContent = (TextView) findViewById(R.id.tv_chat_content);
    }

    @Override
    public void updateView(EMMessage msg) {
    }

    @Override
    protected void onSetUpView() {
        if (position == 0) {
            mTvChatTime.setText(TimeUtils.getTimeFriendlyForChat(message.getMsgTime()));
            mTvChatTime.setVisibility(VISIBLE);
        } else {
            EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
            if ((message.getMsgTime() - prevMessage.getMsgTime()) >= (MAX_SPACING_TIME * ConstantConfig.MIN)) {
                mTvChatTime.setText(TimeUtils.getTimeFriendlyForChat(message.getMsgTime()));
                mTvChatTime.setVisibility(VISIBLE);
            } else {
                mTvChatTime.setVisibility(GONE);
            }
        }

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        // 正文
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        mTvChatContent.setText(span, TextView.BufferType.SPANNABLE);
    }
}
