package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMConnectionEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.widget.recycleview.BlankClickRecycleView;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageAdapterV2;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
public class MessageConversationFragment extends TSListFragment<MessageConversationContract.Presenter, MessageItemBeanV2>
        implements MessageConversationContract.View, MessageAdapterV2.OnSwipeItemClickListener,
        OnUserInfoClickListener, BlankClickRecycleView.BlankClickListener {

    @Inject
    MessageConversationPresenter mConversationPresenter;
    @BindView(R.id.searchView)
    TSSearchView mSearchView;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSearchView.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home_message_list;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected void initData() {
        DaggerMessageConversationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageConversationPresenterModule(new MessageConversationPresenterModule(this))
                .build()
                .inject(this);
        super.initData();
        initListener();
    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 刷新信息内容
        if (mPresenter != null) {
            mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            mPresenter.refreshConversationReadMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mAdapter != null && ((MessageAdapterV2) mAdapter).hasItemOpend()) {
            ((MessageAdapterV2) mAdapter).closeAllItems();
        }
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MessageItemBeanV2> data, boolean isLoadMore) {
        if (data.size() > 1) {
            // 数据大于一个才排序
            Collections.sort(data, new EmTimeSortClass());
        }
        super.onNetResponseSuccess(data, isLoadMore);
        hideStickyMessage();
    }

    @Override
    protected boolean showNoMoreData() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MessageAdapterV2 commonAdapter = new MessageAdapterV2(getActivity(), mListDatas);
        commonAdapter.setOnSwipItemClickListener(this);
        commonAdapter.setOnUserInfoClickListener(this);
        return commonAdapter;
    }

    @Override
    public BaseFragment getCurrentFragment() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        onBlickClick();
    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean 当前 item 内容
     * @param position        当前点击位置
     */
    private void toChatV2(MessageItemBeanV2 messageItemBean, int position) {
        if (messageItemBean == null) {
            return;
        }
        ChatActivity.startChatActivity(mActivity, messageItemBean.getConversation().conversationId()
                , messageItemBean.getConversation().getType() == EMConversation.EMConversationType.Chat ? EaseConstant.CHATTYPE_SINGLE : EaseConstant.CHATTYPE_GROUP);
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onBlickClick() {
        if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
            ((MessageAdapterV2) mAdapter).closeAllItems();
        }
    }

    @Override
    public void onLeftClick(int position) {
        toChatV2(mListDatas.get(position), position);
    }

    @Override
    public void onRightClick(int position) {
        mPresenter.deleteConversation(position);
        refreshData();
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMConnectionEventBus(TSEMConnectionEvent event) {
        LogUtils.d("onTSEMConnectionEventBus");
        switch (event.getType()) {
            case TSEMConstants.TS_CONNECTION_USER_LOGIN_OTHER_DIVERS:
                break;
            case TSEMConstants.TS_CONNECTION_USER_REMOVED:
                break;
            case TSEMConstants.TS_CONNECTION_CONNECTED:
                hideStickyMessage();
                requestNetData(0L, false);
                break;
            case TSEMConstants.TS_CONNECTION_DISCONNECTED:
                showStickyMessage(getString(R.string.chat_unconnected));
                break;
            default:
        }
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMessageEventEventBus(TSEMessageEvent event) {
        LogUtils.d("TSEMessageEvent");
        EMCmdMessageBody body = (EMCmdMessageBody) event.getMessage().getBody();
        switch (body.action()) {
            case TSEMConstants.TS_ATTR_GROUP_DISBAND:
                String groupId = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_ID, null);
                String groupName = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_NAME, null);
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                ToastUtils.showToast(groupName + "解散了");
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                mPresenter.deleteGroup(groupId);
                break;
            default:
        }
    }

    private void initListener() {
        mSearchView.setOnSearchClickListener(new TSSearchView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    // 展示原数据
                    mPresenter.requestNetData(0L, false);
                } else {
                    // 显示搜索结果
                    mPresenter.searchList(s.toString());
                }
            }
        });
    }
}
