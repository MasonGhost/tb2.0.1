package com.zhiyicx.thinksnsplus.modules.chat;

import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.chat.ChatMessageList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/04/26
 * @Contact master.jungle68@gmail.com
 */
public abstract class BaseChatFragment<P extends IBasePresenter> extends TSFragment<P> implements InputLimitView.OnSendClickListener, OnRefreshListener, ChatMessageList.MessageListItemClickListener {

    @BindView(R.id.message_list)
    protected ChatMessageList mMessageList; // 聊天列表
    @BindView(R.id.ilv_container)
    protected InputLimitView mIlvContainer; // 输入控件
    @BindView(R.id.rl_container)
    protected RelativeLayout mRlContainer;  // 页面容器


    protected List<ChatItemBean> mDatas = new ArrayList<>();
    protected MessageItemBean mMessageItemBean;
    protected boolean mKeyboradIsOpen;// 软键盘是否打开

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
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        //若不可视区域高度大于1/3屏幕高度，则键盘显示
//                        LogUtils.i(TAG + "---RxView   " + aBoolean);
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

                    }
                });
        mIlvContainer.setEtContentHint(getString(R.string.default_input_chat_hint));
        mMessageList.setMessageListItemClickListener(this);
        mMessageList.setRefreshListener(this);
    }

    @Override
    protected void initData() {
        mMessageItemBean = initMessageItemBean();
        if (mMessageItemBean == null) {
            throw new NullPointerException("mMessageItemBean must not be null");
        }
    }

    /**
     * 初始化对话信息
     *
     * @return
     */
    protected abstract MessageItemBean initMessageItemBean();

    @Override
    public void hideLoading() {
        mMessageList.getRefreshLayout().setRefreshing(false);
    }

    /**
     * 发送按钮被点击
     *
     * @param text
     */
    @Override
    public void onSendClick(View v, String text) {

    }

    /*******************************************  聊天 item 点击事件 *********************************************/

    /**
     * 发送状态点击
     *
     * @param chatItemBean
     */
    @Override
    public void onStatusClick(ChatItemBean chatItemBean) {

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
        return true;
    }

    @Override
    public void onRefresh() {
        if (mMessageItemBean.getConversation() == null) {
            hideLoading();
            return;
        }
    }

    /**
     * 初始化消息列表
     */
    protected void initMessageList() {
        mMessageList.init(mMessageItemBean.getConversation().getType() == ChatType.CHAT_TYPE_PRIVATE ? mMessageItemBean.getUserInfo().getName() : getString(R.string.default_message_group)
                , mMessageItemBean.getConversation().getType(), mDatas);
        mMessageList.scrollToBottom();
    }
}
