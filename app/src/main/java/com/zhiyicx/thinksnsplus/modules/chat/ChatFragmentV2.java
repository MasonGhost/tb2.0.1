package com.zhiyicx.thinksnsplus.modules.chat;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.presenter.TSChatTextPresenter;
import com.zhiyicx.thinksnsplus.widget.chat.TSChatPrimaryMenu;

import java.util.concurrent.Executors;

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

    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;

    protected View mDriver;
    protected View mStatusPlaceholderView;

    public ChatFragmentV2 instance(Bundle bundle){
        ChatFragmentV2 fragmentV2 = new ChatFragmentV2();
        fragmentV2.setArguments(bundle);
        return fragmentV2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getContentView(inflater);
    }

    private View getContentView(LayoutInflater inflater){
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 是否添加和状态栏等高的占位 View
        if (setUseSatusbar() && setUseStatusView()) {
            mStatusPlaceholderView = new View(getContext());
            mStatusPlaceholderView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DeviceUtils.getStatuBarHeight(getContext())));
            if (StatusBarUtils.intgetType(getActivity().getWindow()) == 0 && ContextCompat.getColor(getContext(), R.color.white) == Color
                    .WHITE) {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.themeColor));
            } else {
                mStatusPlaceholderView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            linearLayout.addView(mStatusPlaceholderView);
        }
        // 分割线
        mDriver = new View(getContext());
        mDriver.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(com.zhiyicx.baseproject.R.dimen
                .divider_line)));
        mDriver.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.general_for_line));
        linearLayout.addView(mDriver);
        if (setUseSatusbar()) {
            // 状态栏顶上去
            StatusBarUtils.transparencyBar(getActivity());
            linearLayout.setFitsSystemWindows(false);
        } else {
            // 状态栏不顶上去
            StatusBarUtils.setStatusBarColor(getActivity(), R.color.white);
            linearLayout.setFitsSystemWindows(true);
        }
        StatusBarUtils.statusBarLightMode(getActivity());
        FrameLayout frameLayout = new FrameLayout(getActivity());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // 内容区域
        final View bodyContainer = inflater.inflate(getBodyLayoutId(), null);
        bodyContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(bodyContainer);
        linearLayout.addView(frameLayout);
        return linearLayout;
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        mUserInfoBeans = getArguments().getParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST);
        // 标题栏
        // 背景白色
        titleBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        titleBar.setTitleColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
        // 左边返回键
        titleBar.setLeftImageResource(R.mipmap.topbar_back);
        // 右边更多按钮
        titleBar.setRightImageResource(R.mipmap.topbar_more_black);
        if (chatType == EaseConstant.CHATTYPE_SINGLE){
            titleBar.setTitle(mUserInfoBeans.get(1).getName());
        } else if (chatType == EaseConstant.CHATTYPE_GROUP){
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            titleBar.setTitle(group.getGroupName());
            EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
        }
        titleBar.setLeftLayoutClickListener(v -> onBackPressed());
        titleBar.setRightLayoutClickListener(v -> toGroupDetails());
        setRefreshLayoutListener();
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }
        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
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

    private int getBodyLayoutId(){
        return R.layout.fragment_chat_v2;
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

    /**
     * 状态栏是否可用
     *
     * @return 默认不可用
     */
    protected boolean setUseSatusbar() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 设置是否需要添加和状态栏等高的占位 view
     *
     * @return
     */
    protected boolean setUseStatusView() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
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

    /**
     * handle the click event for extend menu
     *
     */
    class ExtendMenuItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener{

        @Override
        public void onClick(int itemId, View view) {
            if(chatFragmentHelper != null){
                if(chatFragmentHelper.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_LOCATION:
                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }
}
