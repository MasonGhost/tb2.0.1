package com.zhiyicx.thinksnsplus.modules.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.util.PathUtil;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.TSEaseChatFragment;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.chat.call.BaseCallActivity;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoActivity;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatCallPresneter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatFilePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatLocationPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatPicturePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatTipTextPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatVideoPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.item.presenter.TSChatVoicePresenter;
import com.zhiyicx.thinksnsplus.modules.chat.location.SendLocationActivity;
import com.zhiyicx.thinksnsplus.modules.chat.video.ImageGridActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import static com.hyphenate.easeui.EaseConstant.EXTRA_CHAT_TYPE;
import static com.hyphenate.easeui.EaseConstant.EXTRA_TO_USER_ID;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;

/**
 * @author Catherine
 * @describe 使用环信UI的聊天页面
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatFragment extends TSEaseChatFragment<ChatContract.Presenter>
        implements TSEaseChatFragment.EaseChatFragmentHelper, ChatContract.View {

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    public static final int REQUEST_CODE_SELECT_AMAP = 16;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;
    private static final int MESSAGE_TYPE_RECALL = 9;

    /**
     * 群聊天室的提示消息
     */
    private static final int MESSAGE_TYPE_TIP = 10;

    static final int ITEM_TAKE_PICTURE_TS = 31;
    static final int ITEM_PICTURE_TS = 32;
    static final int ITEM_LOCATION_TS = 33;
    private static final int ITEM_VIDEO_TS = 34;
    private static final int ITEM_VOICE_CALL_TS = 35;
    private static final int ITEM_VIDEO_CALL_TS = 36;

    protected View mStatusPlaceholderView;

    private ActionPopupWindow mActionPopupWindow;

    public static ChatFragment instance(Bundle bundle) {
        ChatFragment fragmentV2 = new ChatFragment();
        fragmentV2.setArguments(bundle);
        return fragmentV2;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int getToolBarLayoutId() {
        return R.layout.ease_ts_title_bar;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.topbar_more_black;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back;
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            setCenterText(mPresenter.getUserName(toChatUsername));
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            setCenterText(mPresenter.getGroupName(toChatUsername));
        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }
        setRefreshLayoutListener();
        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected void setRightClick() {
        toGroupDetails();
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

    }

    @Override
    protected void toGroupDetails() {
        onEnterToChatDetails();
    }

    @Override
    public void onEnterToChatDetails() {
        // 跳转群组详情
        Intent intent = new Intent(getActivity(), ChatInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TO_USER_ID, toChatUsername);
        bundle.putInt(EXTRA_CHAT_TYPE, chatType);
        intent.putExtras(bundle);
        startActivity(intent);
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
        LogUtils.d("Cathy", "onExtendMenuItemClick" + itemId);
        switch (itemId) {
            case ITEM_TAKE_PICTURE_TS:
                // 拍照
                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                selectPicFromCamera();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            case ITEM_PICTURE_TS:
                // 相册
                selectPicFromLocal();
                break;
            case ITEM_LOCATION_TS:
                // 位置
                Intent intentMap = new Intent(new Intent(getActivity(), SendLocationActivity.class));
                intentMap.putExtras(new Bundle());
                startActivityForResult(intentMap, REQUEST_CODE_MAP);
                break;
            case ITEM_VIDEO_TS:
                // 发送视频文件
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_VIDEO_CALL_TS:
                // 视频通话
                mRxPermissions
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                startVideoCall();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            case ITEM_VOICE_CALL_TS:
                // 语音通话
                mRxPermissions
                        .requestEach(Manifest.permission.RECORD_AUDIO)
                        .subscribe(permission -> {
                            if (permission.granted) {
                                // 权限被允许
                                startVoiceCall();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                initPermissionPopUpWindow(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint));
                            }
                        });
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        mPresenter.dealMessages(messages);
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMessageEventEventBus(TSEMessageEvent event) {
        LogUtils.d("TSEMessageEvent");
        if (event.getMessage() == null) {
            return;
        }
        EMCmdMessageBody body = (EMCmdMessageBody) event.getMessage().getBody();
        switch (body.action()) {
            case TSEMConstants.TS_ATTR_GROUP_DISBAND:
                String groupId = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_ID, null);
                String groupName = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_NAME, null);
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                ToastUtils.showToast(groupName + "解散了");
                EventBus.getDefault().post(groupId, EVENT_IM_DELETE_QUIT);
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                break;
            default:
        }
    }

    @Override
    public void onMessageReceivedWithUserInfo(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername) || message.getTo().equals(toChatUsername)
                    || message.conversationId().equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                conversation.markMessageAsRead(message.getMsgId());
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
//        super.registerExtendMenuItem();
        //extend menu items
        // 图片
        inputMenu.registerExtendMenuItem(R.string.attach_picture, R.mipmap.ico_chat_picture, ITEM_PICTURE_TS, extendMenuItemClickListener);
        // 拍照
        inputMenu.registerExtendMenuItem(R.string.attach_take_pic, R.mipmap.ico_chat_takephoto, ITEM_TAKE_PICTURE_TS, extendMenuItemClickListener);
        // 视频 -- 需求取消  2018-1-31 15:26:14
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.mipmap.ico_chat_video, ITEM_VIDEO_TS, extendMenuItemClickListener);
        // 位置
        inputMenu.registerExtendMenuItem(R.string.attach_location, R.mipmap.ico_chat_location, ITEM_LOCATION_TS, extendMenuItemClickListener);
        // 目前仅有单聊才有音视频通话
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // 语音电话
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.mipmap.ico_chat_voicecall, ITEM_VOICE_CALL_TS, extendMenuItemClickListener);
            // 视频通话
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.mipmap.ico_chat_videocall, ITEM_VIDEO_CALL_TS, extendMenuItemClickListener);
        }
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
            boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                    || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                    || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));

            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(ChatConfig.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
                //messagee recall
                else if (message.getBooleanAttribute(ChatConfig.MESSAGE_TYPE_RECALL, false)) {
                    return MESSAGE_TYPE_RECALL;
                } else if ("admin".equals(message.getUserName()) || isGroupChange) {
                    return MESSAGE_TYPE_TIP;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                // voice call or video call
                EaseChatRowPresenter presenter = new TSChatTextPresenter();
                return presenter;
            }
            return null;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
            if (message.getType() == EMMessage.Type.TXT) {
                EaseChatRowPresenter presenter;
                // voice call or video call
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    presenter = new TSChatCallPresneter();
                    return presenter;
                } else {
                    boolean admin;
                    boolean isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"))
                            || TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));

                    admin = "admin".equals(message.getUserName());
                    if (admin || isGroupChange) {
                        presenter = new TSChatTipTextPresenter();
                    } else {
                        presenter = new TSChatTextPresenter();
                    }
                }
                return presenter;
            } else if (message.getType() == EMMessage.Type.IMAGE) {
                EaseChatRowPresenter presenter = new TSChatPicturePresenter();
                return presenter;
            } else if (message.getType() == EMMessage.Type.VOICE) {
                EaseChatRowPresenter presenter = new TSChatVoicePresenter();
                return presenter;
            } else if (message.getType() == EMMessage.Type.LOCATION) {
                EaseChatRowPresenter presenter = new TSChatLocationPresenter();
                return presenter;
            } else if (message.getType() == EMMessage.Type.VIDEO) {
                EaseChatRowPresenter presenter = new TSChatVideoPresenter();
                return presenter;
            } else if (message.getType() == EMMessage.Type.FILE) {
                EaseChatRowPresenter presenter = new TSChatFilePresenter();
                return presenter;
            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            ToastUtils.showToast(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT);
        } else {
            BaseCallActivity.startVoiceCallActivity(mActivity, toChatUsername, false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected()) {
            ToastUtils.showToast(R.string.not_connect_to_server);
        } else {
            BaseCallActivity.startVideoCallActivity(mActivity, toChatUsername, false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_DELETE_QUIT)
    public void deleteGroup(String id) {
        mActivity.finish();
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_GROUP_CREATE_FROM_SINGLE)
    public void closeCurrent(ChatGroupBean chatGroupBean) {
        if (!chatGroupBean.getId().equals(toChatUsername)) {
            getActivity().finish();
        }
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO)
    public void updateCurrent(ChatGroupBean chatGroupBean) {
        if (chatGroupBean.getId().equals(toChatUsername)) {
            setCenterText(chatGroupBean.getName());
        }
    }

    private void initPermissionPopUpWindow(String item1) {
        mActionPopupWindow = PermissionPopupWindow.builder()
                .permissionName(getString(com.zhiyicx.baseproject.R.string.camera_permission))
                .with(getActivity())
                .bottomStr(getString(com.zhiyicx.baseproject.R.string.cancel))
                .item1Str(item1)
                .item2Str(getString(com.zhiyicx.baseproject.R.string.setting_permission))
                .item2ClickListener(() -> {
                    DeviceUtils.openAppDetail(getContext());
                    mActionPopupWindow.hide();
                })
                .bottomClickListener(() -> mActionPopupWindow.hide())
                .isFocus(true)
                .isOutsideTouch(true)
                .backgroundAlpha(0.8f)
                .build();
        mActionPopupWindow.show();
    }
}
