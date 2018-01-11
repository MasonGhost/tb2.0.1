package com.zhiyicx.thinksnsplus.modules.chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.db.dao.ConversationDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.chat.ChatMessageList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
public class ChatFragment extends TSFragment<ChatContract.Presenter> implements ChatContract.View, InputLimitView.OnSendClickListener, OnRefreshListener, ChatMessageList.MessageListItemClickListener {
    public static final String BUNDLE_MESSAGEITEMBEAN = "MessageItemBean";

    @BindView(R.id.message_list)
    ChatMessageList mMessageList; // 聊天列表
    @BindView(R.id.ilv_container)
    InputLimitView mIlvContainer; // 输入控件
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;  // 页面容器
    private ActionPopupWindow mDeletCommentPopWindow;

    private List<ChatItemBean> mDatas = new ArrayList<>();
    private MessageItemBean mMessageItemBean;
    private boolean mKeyboradIsOpen;// 软键盘是否打开

    public static ChatFragment newInstance(MessageItemBean messageItemBean) {
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }


    @Override
    protected void setRightClick() {
        super.setRightClick();
    }

    @Override
    protected void setLeftClick() {
        DeviceUtils.hideSoftKeyboard(mActivity,mIlvContainer.getEtContent());
        super.setLeftClick();
    }

    @Override
    protected String setCenterTitle() {
        return "";
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mIlvContainer.setOnSendClickListener(this);
        mIlvContainer.setSendButtonVisiable(true); // 保持显示
        mIlvContainer.getFocus();
        // 软键盘控制区
        RxView.globalLayouts(mRlContainer)
                .flatMap(new Func1<Void, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Void aVoid) {
                        if (mRlContainer==null) {
                            return Observable.just(false);
                        }
                        Rect rect = new Rect();
                        //获取root在窗体的可视区域
                        mRlContainer.getWindowVisibleDisplayFrame(rect);
                        //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = mRlContainer.getRootView().getHeight() - rect.bottom;
                        int dispayHeight = UIUtils.getWindowHeight(getContext());
                        return Observable.just(rootInvisibleHeight > (dispayHeight * (1f / 3)));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (mMessageList==null) {
                        return;
                    }
                    //若不可视区域高度大于1/3屏幕高度，则键盘显示
                    LogUtils.i(TAG + "---RxView   " + aBoolean);
                    if (aBoolean) {
                        if (!mKeyboradIsOpen && mMessageItemBean.getConversation() != null) {// 如果对话没有创建，不做处理
                            mMessageList.scrollToBottom();
                        }
                        mKeyboradIsOpen = true;
                    } else {
                        //键盘隐藏
                        mKeyboradIsOpen = false;
//                            mIlvContainer.clearFocus();// 主动失去焦点
                    }
//                        mIlvContainer.setSendButtonVisiable(mKeyboradIsOpen);//      不需要隐藏

                });
        mIlvContainer.setEtContentHint(getString(R.string.default_input_chat_hint));
        // 软键盘异常解决方案： 1： 使用      android:fitsSystemWindows="true"  2:        AndroidBug5497Workaround.assistActivity(getActivity());
    }

    @Override
    protected void initData() {
        getIntentData();
        mMessageList.setMessageListItemClickListener(this);
        mMessageList.setRefreshListener(this);
        if (mMessageItemBean.getConversation() == null) { // 先获取本地信息，如果本地信息存在，直接使用，如果没有直接创建
            Conversation conversation = ConversationDao.getInstance(getContext()).getPrivateChatConversationByUids((int) AppApplication.getmCurrentLoginAuth().getUser_id(), mMessageItemBean.getUserInfo().getUser_id().intValue());
            if (conversation == null) {
                mPresenter.createChat(mMessageItemBean.getUserInfo(), null);
            } else {
                mMessageItemBean.setConversation(conversation);
                initMessageList();
            }
        } else {
            initMessageList();
        }

    }

    @Override
    public void hideLoading() {
        mMessageList.getRefreshLayout().finishRefresh();
    }

    /**
     * 发送按钮被点击
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mMessageItemBean.getConversation() != null) {
            mPresenter.sendTextMessage(text, mMessageItemBean.getConversation().getCid());
        } else {
            mPresenter.createChat(mMessageItemBean.getUserInfo(), text);
        }
    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     *
     * @param chatItemBean
     */
    @Override
    public void onStatusClick(ChatItemBean chatItemBean) {
        initDeletCommentPopupWindow(chatItemBean);
        mDeletCommentPopWindow.show();
    }

    /**
     * 聊天气泡点击
     *
     * @param message
     */
    @Override
    public void onBubbleClick(ChatItemBean message) {
        LogUtils.d("----------------onBubbleClick----------");
        DeviceUtils.hideSoftKeyboard(getContext(), mMessageList.getListView());

    }

    /**
     * 聊天气泡长按
     *
     * @param message
     * @return
     */
    @Override
    public boolean onBubbleLongClick(ChatItemBean message) {
        LogUtils.d("----------------onBubbleLongClick----------");
        DeviceUtils.hideSoftKeyboard(getContext(), mMessageList.getListView());
        return true;
    }

    /**
     * 用户信息点击
     *
     * @param chatItemBean
     */
    @Override
    public void onUserInfoClick(ChatItemBean chatItemBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), chatItemBean.getUserInfo());
    }

    /**
     * 用户信息长按
     *
     * @param chatItemBean
     * @return
     */
    @Override
    public boolean onUserInfoLongClick(ChatItemBean chatItemBean) {
        showSnackSuccessMessage(chatItemBean.getUserInfo().getName());
        return true;
    }

    @Override
    public void setChatTitle(@NotNull String titleStr) {
        setCenterText(titleStr);
    }

    @Override
    public void reFreshMessage(ChatItemBean chatItemBean) {
        mDatas.add(chatItemBean);
        mMessageList.refreshSoomthBottom();
    }

    @Override
    public void refreshData() {
        mMessageList.refresh();
    }

    @Override
    public void smoothScrollToBottom() {
        mMessageList.smoothScrollToBottom();
    }

    @Override
    public int getCurrentChatCid() {
        return mMessageItemBean.getConversation().getCid();
    }

    @Override
    public void updateMessageStatus(Message message) {
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            if (mDatas.get(i).getLastMessage().getId() == message.getId()) {
                mDatas.get(i).setLastMessage(message);
                mMessageList.refresh(); // 没有 UI 更新 ，可以不刷新
                break;
            }
        }
    }

    @Override
    public void updateConversation(Conversation conversation) {
        mMessageItemBean.setConversation(conversation);
        initMessageList();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mMessageItemBean.getConversation() == null) {
            hideLoading();
            return;
        }
        List<ChatItemBean> chatItemBeen = mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), mDatas.size() > 0 ? mDatas.get(0).getLastMessage().getCreate_time() : (System.currentTimeMillis() + ConstantConfig.DAY));
        chatItemBeen.addAll(mDatas);
        mDatas.clear();
        mDatas.addAll(chatItemBeen);
        mMessageList.refresh();
    }

    private void getIntentData() {
        mMessageItemBean = getArguments().getParcelable(BUNDLE_MESSAGEITEMBEAN);
        setChatTitle(mMessageItemBean.getUserInfo().getName());
    }

    public void initMessageList() {
        mDatas.addAll(mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), (System.currentTimeMillis() + ConstantConfig.DAY)));
        mMessageList.init(mMessageItemBean.getConversation().getType() == ChatType.CHAT_TYPE_PRIVATE ? mMessageItemBean.getUserInfo().getName() : getString(R.string.default_message_group)
                , mMessageItemBean.getConversation().getType(), mDatas);
        mMessageList.scrollToBottom();
    }

    /**
     * 初始化评论删除选择弹框
     */
    private void initDeletCommentPopupWindow(final ChatItemBean chatItemBean) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.resend))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    onResendClick(chatItemBean);
                    mDeletCommentPopWindow.hide();

                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    private void onResendClick(ChatItemBean chatItemBean) {
        mPresenter.reSendText(chatItemBean);
    }

}
