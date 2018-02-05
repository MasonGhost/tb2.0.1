package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.text.Spannable;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.zhiyicx.baseproject.em.manager.control.TSEMConstants;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;

import static com.zhiyicx.thinksnsplus.widget.chat.MessageTextItemDelagate.MAX_SPACING_TIME;


/**
 * @author Catherine
 * @describe 文字的item
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatRowText extends ChatBaseRow {
    private TextView mTvChatContent;
    private boolean adminMsg;

    public ChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        int resId;
        boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));

        adminMsg = "admin".equals(message.getUserName()) || isGroupChange;
        if (adminMsg) {
            resId = R.layout.include_chat_extra;
        } else {
            resId = message.direct() == EMMessage.Direct.SEND ?
                    R.layout.item_chat_list_send_text : R.layout.item_chat_list_receive_text;
        }
        inflater.inflate(resId, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mTvChatContent = (TextView) findViewById(R.id.tv_chat_content);
    }

    @Override
    protected void onSetUpView() {
        if (adminMsg) {
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
        } else {
            super.onSetUpView();
        }

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        // 正文
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        mTvChatContent.setText(span, TextView.BufferType.SPANNABLE);
    }
}
