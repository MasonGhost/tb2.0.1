package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 语音消息
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class ChatRowVoice extends ChatBaseRow{

    private TextView mTvVoiceLength;
    private ImageView mIvVoicePlay;
    private AnimationDrawable voiceAnimation;

    public ChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.SEND ?
                R.layout.item_chat_list_send_voice : R.layout.item_chat_list_receive_voice, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mTvVoiceLength = (TextView) findViewById(R.id.tv_voice_length);
        mIvVoicePlay = (ImageView) findViewById(R.id.iv_voice_play);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if (len > 0) {
            mTvVoiceLength.setText(voiceBody.getLength() + "\"");
            mTvVoiceLength.setVisibility(View.VISIBLE);
        } else {
            mTvVoiceLength.setVisibility(View.INVISIBLE);
        }
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            mIvVoicePlay.setImageResource(com.hyphenate.easeui.R.drawable.ease_chatfrom_voice_playing);
        } else {
            mIvVoicePlay.setImageResource(com.hyphenate.easeui.R.drawable.ease_chatto_voice_playing);
        }
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            LogUtils.d(TAG, "it is receive msg");
            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        // To avoid the item is recycled by listview and slide to this item again but the animation is stopped.
        EaseChatRowVoicePlayer voicePlayer = EaseChatRowVoicePlayer.getInstance(getContext());
        if (voicePlayer != null && voicePlayer.isPlaying() && message.getMsgId().equals(voicePlayer.getCurrentPlayingId())) {
            startVoicePlayAnimation();
        }
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        super.onViewUpdate(msg);
        // Only the received message has the attachment download status.
        if (message.direct() == EMMessage.Direct.SEND) {
            return;
        }

        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) msg.getBody();
        if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void startVoicePlayAnimation() {
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            mIvVoicePlay.setImageResource(R.anim.voice_from_icon);
        } else {
            mIvVoicePlay.setImageResource(R.anim.voice_to_icon);
        }
        voiceAnimation = (AnimationDrawable) mIvVoicePlay.getDrawable();
        voiceAnimation.start();
    }

    public void stopVoicePlayAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) {
            mIvVoicePlay.setImageResource(R.drawable.ease_chatfrom_voice_playing);
        } else {
            mIvVoicePlay.setImageResource(R.drawable.ease_chatto_voice_playing);
        }
    }
}
