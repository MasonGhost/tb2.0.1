package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import static com.zhiyicx.thinksnsplus.widget.chat.MessageTextItemDelagate.MAX_SPACING_TIME;

/**
 * @author Catherine
 * @describe 文字的item
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatRowText extends EaseChatRow {

    private UserAvatarView mIvChatHeadpic;
    private TextView mTvChatTime;
    private TextView mTvChatName;
    private RelativeLayout mRlChatBubble;
    private TextView mTvChatContent;

    private ChatUserInfoBean mUserInfoBean;

    public ChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter);
        this.mUserInfoBean = userInfoBean;
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.SEND ?
                R.layout.item_chat_list_send_text : R.layout.item_chat_list_receive_text, this);
    }

    @Override
    protected void onFindViewById() {
        mIvChatHeadpic = (UserAvatarView) findViewById(R.id.iv_chat_headpic);
        mTvChatTime = (TextView) findViewById(R.id.tv_chat_time);
        mTvChatName = (TextView) findViewById(R.id.tv_chat_name);
        mTvChatContent = (TextView) findViewById(R.id.tv_chat_content);
        mRlChatBubble = (RelativeLayout) findViewById(R.id.rl_chat_bubble);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {

    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        // 正文
        Spannable span = EaseSmileUtils.getSmiledText(context, txtBody.getMessage());
        mTvChatContent.setText(span, TextView.BufferType.SPANNABLE);
        // 头像
        ImageUtils.loadUserHead(mUserInfoBean, mIvChatHeadpic, false);
        // 时间
        if ((message.getMsgTime() - message.getMsgTime()) >= (MAX_SPACING_TIME * ConstantConfig.MIN)) {
            mTvChatTime.setText(TimeUtils.getTimeFriendlyForChat(message.getMsgTime()));
            mTvChatTime.setVisibility(VISIBLE);
        } else {
            mTvChatTime.setVisibility(GONE);
        }
        // 用户名
        mTvChatName.setText(mUserInfoBean.getName());
    }
}
