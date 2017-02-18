package com.zhiyicx.thinksnsplus.modules.chat;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.widget.chat.ChatMessageList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
public class ChatFragment extends TSFragment<ChatContract.Presenter> implements ChatContract.View, InputLimitView.OnSendClickListener, BGARefreshLayout.BGARefreshLayoutDelegate, ChatMessageList.MessageListItemClickListener {
    public static final String BUNDLE_MESSAGEITEMBEAN = "MessageItemBean";

    @BindView(R.id.message_list)
    ChatMessageList mMessageList; // 聊天列表
    @BindView(R.id.ilv_container)
    InputLimitView mIlvContainer; // 输入控件
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;  // 页面容器


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
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mIlvContainer.setOnSendClickListener(this);
        // 软键盘控制区
        mRlContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect rect = new Rect();
                //获取root在窗体的可视区域
                mRlContainer.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = mRlContainer.getRootView().getHeight() - rect.bottom;
                int dispayHeight = UIUtils.getWindowHeight(getContext());
                //若不可视区域高度大于1/3屏幕高度，则键盘显示
                if (rootInvisibleHeight > (1 / 3 * dispayHeight)) {
                    mKeyboradIsOpen = true;
                    smoothScrollToBottom();
                } else {
                    //键盘隐藏
                    mKeyboradIsOpen = false;
                    mIlvContainer.clearFocus();// 主动失去焦点
                }
                mIlvContainer.setSendButtonVisiable(mKeyboradIsOpen);
            }
        });

    }

    @Override
    protected void initData() {
        getIntentData();
        mDatas.addAll(mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), System.currentTimeMillis()));
        mMessageList.setMessageListItemClickListener(this);
        mMessageList.init(mMessageItemBean.getConversation().getType() == ChatType.CHAT_TYPE_PRIVATE ? mMessageItemBean.getUserInfo().getName() : getString(R.string.default_message_group)
                , mMessageItemBean.getConversation().getType(), mDatas);
        mMessageList.setBGARefreshLayoutDelegate(this);
        smoothScrollToBottom();
    }

    private void getIntentData() {
        mMessageItemBean = getArguments().getParcelable(BUNDLE_MESSAGEITEMBEAN);
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mMessageList.getRefreshLayout().endRefreshing();
    }
    @Override
    public void showMessage(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        List<ChatItemBean> chatItemBeen = mPresenter.getHistoryMessages(mMessageItemBean.getConversation().getCid(), mDatas.size() > 0 ? mDatas.get(0).getLastMessage().getCreate_time() : System.currentTimeMillis());
        chatItemBeen.addAll(mDatas);
        mDatas.clear();
        mDatas.addAll(chatItemBeen);
        mMessageList.refresh();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return true;
    }

    /**
     * 发送按钮被点击
     *
     * @param text
     */
    @Override
    public void onSendClick(String text) {
        mPresenter.sendTextMessage(text, mMessageItemBean.getConversation().getCid());
    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     *
     * @param message
     */
    @Override
    public void onStatusClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
    }

    /**
     * 聊天气泡点击
     *
     * @param message
     */
    @Override
    public void onBubbleClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());

    }

    /**
     * 聊天气泡长按
     *
     * @param message
     * @return
     */
    @Override
    public boolean onBubbleLongClick(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }

    /**
     * 用户信息点击
     *
     * @param chatItemBean
     */
    @Override
    public void onUserInfoClick(ChatItemBean chatItemBean) {
        showMessage(chatItemBean.getUserInfo().getName());
    }

    /**
     * 用户信息长按
     *
     * @param chatItemBean
     * @return
     */
    @Override
    public boolean onUserInfoLongClick(ChatItemBean chatItemBean) {
        showMessage(chatItemBean.getUserInfo().getName());
        return true;
    }

    /**
     * item 点击
     *
     * @param message
     */
    @Override
    public void onItemClickListener(ChatItemBean message) {
        DeviceUtils.hideSoftKeyboard(getContext(), mRootView);
    }

    /**
     * item 长按
     *
     * @param message
     * @return
     */
    @Override
    public boolean onItemLongClickListener(ChatItemBean message) {
        showMessage(message.getLastMessage().getTxt());
        return true;
    }


    @Override
    public void reFreshMessage(ChatItemBean chatItemBean) {
        mDatas.add(chatItemBean);
        mMessageList.refresh();
    }

    @Override
    public void smoothScrollToBottom() {
        mMessageList.smoothScrollToBottom();
    }
}
