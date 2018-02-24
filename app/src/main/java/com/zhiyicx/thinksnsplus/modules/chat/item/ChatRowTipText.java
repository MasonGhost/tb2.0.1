package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.klinker.android.link_builder.Link;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.thinksnsplus.widget.chat.MessageTextItemDelagate.MAX_SPACING_TIME;


/**
 * @author Jliuer
 * @Date 18/02/06 14:01
 * @Email Jliuer@aliyun.com
 * @Description 文本消息，提示信息
 */
public class ChatRowTipText extends ChatBaseRow {
    private TextView mTvChatContent;
    private OnTipMsgClickListener mOnTipMsgClickListener;

    public ChatRowTipText(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    public ChatRowTipText(Context context, EMMessage message, int position, BaseAdapter adapter, OnTipMsgClickListener onTipMsgClickListener) {
        super(context, message, position, adapter, null);
        this.mOnTipMsgClickListener = onTipMsgClickListener;
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

        ConvertUtils.stringLinkConvert(mTvChatContent, setLiknks(mTvChatContent.getText().toString()), false);
    }

    private List<Link> setLiknks(String content) {
        List<Link> links = new ArrayList<>();
        if (content.contains(context.getString(R.string.super_edit_group_name))) {
            Link nameLink = new Link(context.getString(R.string.chat_edit_group_name))
                    .setTextColor(ContextCompat.getColor(context, R.color
                            .themeColor))
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(context, R.color
                            .general_for_hint))
                    .setHighlightAlpha(.8f)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        if (mOnTipMsgClickListener != null) {
                            mOnTipMsgClickListener.onTipMsgClick(TipMsgType.CREATE_GROUP);
                        }
                    })
                    .setOnLongClickListener((clickedText, linkMetadata) -> {

                    })
                    .setUnderlined(false);
            links.add(nameLink);
        }
        return links;
    }
}
