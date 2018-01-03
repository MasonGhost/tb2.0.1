package com.zhiyicx.thinksnsplus.modules.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.presenter.TSChatTextPresenter;

/**
 * @author Catherine
 * @describe 使用环信UI的聊天页面
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatFragmentV2 extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper {

    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    public ChatFragmentV2 instance(Bundle bundle){
        ChatFragmentV2 fragmentV2 = new ChatFragmentV2();
        fragmentV2.setArguments(bundle);
        return fragmentV2;
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        mUserInfoBeans = getArguments().getParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST);
        super.setUpView();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    public void onEnterToChatDetails() {
        if (chatType == ChatConfig.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                ToastUtils.showToast(R.string.gorup_not_found);
                return;
            }
            // 跳转群组详情
        }
    }

    @Override
    public void onAvatarClick(String username) {

    }

    @Override
    public void onAvatarLongClick(String username) {

    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.mipmap.ico_chat_video, ITEM_VIDEO, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_file, R.mipmap.ico_chat_accessory, ITEM_FILE, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.mipmap.ico_chat_voicecall, ITEM_VOICE_CALL, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.mipmap.ico_chat_videocall, ITEM_VIDEO_CALL, extendMenuItemClickListener);

        //end of red packet code
    }

    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if(message.getBooleanAttribute(ChatConfig.MESSAGE_TYPE_RECALL, false)){
                    return MESSAGE_TYPE_RECALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                EaseChatRowPresenter presenter = new TSChatTextPresenter();
                return presenter;
            }
            return null;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                EaseChatRowPresenter presenter = new TSChatTextPresenter();
                return presenter;
            }
            return null;
        }

    }
}
