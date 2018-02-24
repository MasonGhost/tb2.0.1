package com.zhiyicx.thinksnsplus.modules.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
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
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.util.PathUtil;
import com.zhiyi.richtexteditorlib.view.dialogs.LinkDialog;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
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
        implements TSEaseChatFragment.EaseChatFragmentHelper, ChatContract.View, EaseChatRow.OnTipMsgClickListener {

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

    private ActionPopupWindow mActionPopupWindow;

    public static ChatFragment instance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
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
    public void setGoupName(String name) {
        setCenterText(name);
    }

    @Override
    protected void initEMView(View rootView) {
        super.initEMView(rootView);
        // 适配手机无法显示输入焦点
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(mActivity);
        }
    }

    @Override
    public void onTipMsgClick(EaseChatRow.TipMsgType tipMsgType) {
        LinkDialog dialog = createLinkDialog();
        dialog.setListener(new LinkDialog.OnDialogClickListener() {
            @Override
            public void onConfirmButtonClick(String name, String url) {
                ChatGroupBean groupBean = mPresenter.getChatGroupInfo(toChatUsername);
                groupBean.setName(url);
                mPresenter.updateGroupName(groupBean);
                dialog.dismiss();
            }

            @Override
            public void onCancelButtonClick() {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(), LinkDialog.Tag);
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
            onMessageListInit(this);
        }
        setRefreshLayoutListener();
        // show forward message if the message is not null
        String forwardMsgId = getArguments().getString("forward_msg_id");
        if (forwardMsgId != null) {
            forwardMessage(forwardMsgId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            setCenterText(mPresenter.getUserName(toChatUsername));
        } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
            setCenterText(mPresenter.getGroupName(toChatUsername));
        }
    }

    public void onNewIntent(Bundle bundle) {
        setArguments(bundle);
        fragmentArgs = getArguments();
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
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
        inputMenu.hideExtendMenuContainer();
        return true;
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
    public void setTitle(String s) {
        setCenterText(s);
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
                showSnackWarningMessage(getString(R.string.group_quit_format, groupName));
                EventBus.getDefault().post(groupId, EVENT_IM_DELETE_QUIT);
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                break;
            default:
        }
    }

    @Override
    public void onMessageReceivedWithUserInfo(List<EMMessage> messages) {
        boolean isGroupChange = false;
        String chatGroupId = "";
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }
            // 从左到右依次：用户加入，用户退出，创建群聊，屏蔽/接收群消息，该群消息作用对象是否是自己,群信息修改
            boolean isUserJoin, isUserExit, isCreate, isBlock, userTag;

            isUserJoin = TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
            isUserExit = TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"));
            if (!isGroupChange) {
                isGroupChange = TSEMConstants.TS_ATTR_GROUP_CHANGE.equals(message.ext().get("type"));
                chatGroupId = message.ext().get(TSEMConstants.TS_ATTR_GROUP_ID).toString();
            }
            isCreate = message.getBooleanAttribute(TSEMConstants.TS_ATTR_GROUP_CRATE, false);
            isBlock = message.getBooleanAttribute(TSEMConstants.TS_ATTR_BLOCK, false);
            userTag = AppApplication.getMyUserIdWithdefault() == message.getLongAttribute(TSEMConstants.TS_ATTR_TAG, -1L);

            if (isCreate || isBlock) {
                if (!userTag) {
                    return;
                }
            }

            if (isUserExit || isUserJoin) {
                // 只有群聊中才会有 成员 加入or退出的消息
                mPresenter.updateChatGroupMemberCount(message.conversationId(), 1, isUserJoin);
                setCenterText(mPresenter.getGroupName(message.conversationId()));
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
        if (isGroupChange) {
            mPresenter.getGroupChatInfo(chatGroupId);
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
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.mipmap.ico_chat_voicecall, ITEM_VOICE_CALL_TS,
                    extendMenuItemClickListener);
            // 视频通话
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.mipmap.ico_chat_videocall, ITEM_VIDEO_CALL_TS,
                    extendMenuItemClickListener);
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
                return new TSChatTextPresenter();
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
                return new TSChatPicturePresenter();
            } else if (message.getType() == EMMessage.Type.VOICE) {
                return new TSChatVoicePresenter();
            } else if (message.getType() == EMMessage.Type.LOCATION) {
                return new TSChatLocationPresenter();
            } else if (message.getType() == EMMessage.Type.VIDEO) {
                return new TSChatVideoPresenter();
            } else if (message.getType() == EMMessage.Type.FILE) {
                return new TSChatFilePresenter();
            }
            return null;
        }

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
                .build();
        mActionPopupWindow.show();
    }

    private LinkDialog createLinkDialog() {
        return LinkDialog.createLinkDialog()
                .setUrlHinit(getString(R.string.chat_edit_group_name_alert))
                .setTitleStr(getString(R.string.chat_edit_group_name))
                .setNameVisible(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissPop(mActionPopupWindow);
    }
}
