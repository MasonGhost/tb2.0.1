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
import com.zhiyicx.thinksnsplus.R;


/**
 * @author Catherine
 * @describe 文字的item
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatRowText extends ChatBaseRow {
    private TextView mTvChatContent;

    public ChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        int resId;
        boolean isJoinOrEixt = false;

//        boolean isJoinOrEixt = message.getBooleanAttribute(TSEMConstants.TS_ATTR_JOIN, false)
//                || message.getBooleanAttribute(TSEMConstants.TS_ATTR_EIXT, false);
        if (isJoinOrEixt) {
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
        super.onSetUpView();
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        // 正文
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        mTvChatContent.setText(span, TextView.BufferType.SPANNABLE);
    }
}
