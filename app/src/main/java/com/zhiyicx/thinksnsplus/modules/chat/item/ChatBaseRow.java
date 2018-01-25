package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.concurrent.TimeUnit;
import skin.support.widget.SkinCompatProgressBar;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.widget.chat.MessageTextItemDelagate.MAX_SPACING_TIME;

/**
 * @author Catherine
 * @describe 消息item的基类
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class ChatBaseRow extends EaseChatRow {

    protected UserAvatarView mIvChatHeadpic;
    protected TextView mTvChatTime;
    protected TextView mTvChatName;
    /**状态view*/
    protected ImageView mMsgStatus;
    protected SkinCompatProgressBar mProgressBar;
    protected TextView mTvMessageStatus;

    protected ChatUserInfoBean mUserInfoBean;

    public ChatBaseRow(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter);
        this.mUserInfoBean = userInfoBean;
    }

    @Override
    protected void onInflateView() {

    }

    @Override
    protected void onFindViewById() {
        mIvChatHeadpic = (UserAvatarView) findViewById(R.id.iv_chat_headpic);
        mTvChatTime = (TextView) findViewById(R.id.tv_chat_time);
        mTvChatName = (TextView) findViewById(R.id.tv_chat_name);
        mMsgStatus = (ImageView) findViewById(R.id.msg_status);
        mProgressBar = (SkinCompatProgressBar) findViewById(R.id.progress_bar);
        mTvMessageStatus = (TextView) findViewById(R.id.tv_message_status);
        bubbleLayout = findViewById(R.id.rl_chat_bubble);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        mTvMessageStatus.setText(context.getString(msg.isAcked() ? R.string.chat_send_message_read:R.string.chat_send_message_unread));
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess(msg);
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
            default:
        }
    }

    @Override
    protected void onSetUpView() {
        // 头像
        mTvMessageStatus.setText(context.getString(R.string.chat_send_message_unread));
        ImageUtils.loadUserHead(mUserInfoBean, mIvChatHeadpic, false);
        // 时间
        if (position == 0){
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
        // 用户名
        mTvChatName.setText(mUserInfoBean.getName());
        RxView.clicks(mIvChatHeadpic)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(mUserInfoBean.getUser_id());
                    PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
                });
        if (mMsgStatus != null){
            mMsgStatus.setOnClickListener(v -> {
                if (itemActionCallback != null) {
                    itemActionCallback.onResendClick(message);
                }
            });
        }
    }

    private void onMessageCreate() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessageStatus.setVisibility(GONE);
        if (mMsgStatus != null){
            mMsgStatus.setVisibility(View.GONE);
        }
    }

    private void onMessageSuccess(EMMessage message) {
        mProgressBar.setVisibility(View.GONE);
        if (mMsgStatus != null){
            mMsgStatus.setVisibility(View.GONE);
        }
        onMessageRead(message.isAcked());
    }

    private void onMessageError() {
        mProgressBar.setVisibility(View.GONE);
        mTvMessageStatus.setVisibility(GONE);
        if (mMsgStatus != null){
            mMsgStatus.setVisibility(View.VISIBLE);
        }
    }

    private void onMessageInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTvMessageStatus.setVisibility(GONE);
        if (mMsgStatus != null){
            mMsgStatus.setVisibility(View.GONE);
        }
    }

    private void onMessageRead(boolean hasRead){
        mProgressBar.setVisibility(View.GONE);
        if (mMsgStatus != null){
            mMsgStatus.setVisibility(View.GONE);
        }
        mTvMessageStatus.setVisibility(hasRead ? VISIBLE : GONE);
    }
}
