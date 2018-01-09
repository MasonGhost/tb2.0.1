package com.zhiyicx.thinksnsplus.modules.chat.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.hyphenate.easeui.widget.presenter.EaseChatVoicePresenter;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatRowVoice;

import java.io.File;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/8
 * @contact email:648129313@qq.com
 */

public class TSChatVoicePresenter extends EaseChatVoicePresenter {

    private static final String TAG = "EaseChatVoicePresenter";
    private EaseChatRowVoicePlayer voicePlayer;

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean chatUserInfoBean) {
        voicePlayer = EaseChatRowVoicePlayer.getInstance(cxt);
        return new ChatRowVoice(cxt, message, position, adapter, chatUserInfoBean);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            ((ChatRowVoice) getChatRow()).stopVoicePlayAnimation();

            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (msgId.equals(playingId)) {
                return;
            }
        }

        if (message.direct() == EMMessage.Direct.SEND) {
            // Play the voice
            String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
            File file = new File(localPath);
            if (file.exists() && file.isFile()) {
                playVoice(message);
                // Start the voice play animation.
                ((ChatRowVoice) getChatRow()).startVoicePlayAnimation();
            } else {
                asyncDownloadVoice(message);
            }
        } else {
            final String st = getContext().getResources().getString(com.hyphenate.easeui.R.string.Is_download_voice_click_later);
            if (message.status() == EMMessage.Status.SUCCESS) {
                if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                    play(message);
                } else {
                    EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
                    LogUtils.i(TAG, "Voice body download status: " + voiceBody.downloadStatus());
                    switch (voiceBody.downloadStatus()) {
                        case PENDING:// Download not begin
                        case FAILED:// Download failed
                            getChatRow().updateView(getMessage());
                            asyncDownloadVoice(message);
                            break;
                        case DOWNLOADING:// During downloading
                            ToastUtils.showToast(getContext(), st);
                            break;
                        case SUCCESSED:// Download success
                            play(message);
                            break;
                        default:
                    }
                }
            } else if (message.status() == EMMessage.Status.INPROGRESS) {
                ToastUtils.showToast(getContext(), st);
            } else if (message.status() == EMMessage.Status.FAIL) {
                ToastUtils.showToast(getContext(), st);
                asyncDownloadVoice(message);
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        if (voicePlayer.isPlaying()) {
            voicePlayer.stop();
        }
    }

    private void asyncDownloadVoice(final EMMessage message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                EMClient.getInstance().chatManager().downloadAttachment(message);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                getChatRow().updateView(getMessage());
            }
        }.execute();
    }

    private void play(EMMessage message) {
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            ackMessage(message);
            playVoice(message);
            // Start the voice play animation.
            ((ChatRowVoice) getChatRow()).startVoicePlayAnimation();
        } else {
            EMLog.e(TAG, "file not exist");
        }
    }

    private void ackMessage(EMMessage message) {
        EMMessage.ChatType chatType = message.getChatType();
        if (!message.isAcked() && chatType == EMMessage.ChatType.Chat) {
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        if (!message.isListened()) {
            EMClient.getInstance().chatManager().setVoiceMessageListened(message);
        }
    }

    private void playVoice(EMMessage msg) {
        voicePlayer.play(msg, mp -> {
            // Stop the voice play animation.
            ((ChatRowVoice) getChatRow()).stopVoicePlayAnimation();
        });
    }
}
