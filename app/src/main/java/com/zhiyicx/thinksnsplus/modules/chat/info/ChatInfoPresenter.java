package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.os.Bundle;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.EmptySubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_DATA_CHANGED;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_EDIT_NAME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_REMOVE_MEMBER;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoPresenter extends AppBasePresenter<ChatInfoContract.View>
        implements ChatInfoContract.Presenter {

    @Inject
    ChatInfoRepository mRepository;

    @Inject
    public ChatInfoPresenter(ChatInfoContract.View rootView) {
        super(rootView);
    }

    @Override
    public boolean isGroupOwner() {
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        if (chatGroupBean == null) {
            return false;
        }
        String owner = String.valueOf(chatGroupBean.getOwner());
        return owner.equals(String.valueOf(AppApplication.getMyUserIdWithdefault()));
    }

    @Override
    public void destoryOrLeaveGroup(String chatId) {
        Observable.empty()
                .observeOn(Schedulers.io())
                .subscribe(new EmptySubscribe<Object>() {
                    @Override
                    public void onCompleted() {
                        try {
                            if (isGroupOwner()) {
                                // 解散群组
                                EMClient.getInstance().groupManager().destroyGroup(chatId);
                            } else {
                                // 退群
                                EMClient.getInstance().groupManager().leaveGroup(chatId);
                            }
                            EventBus.getDefault().post(chatId, EVENT_IM_DELETE_QUIT);
                            mRootView.closeCurrentActivity();
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                        }
                    }
                });
    }

    @Override
    public void openOrCloseGroupMessage(boolean isChecked, String chatId) {
        Observable.empty()
                .observeOn(Schedulers.io())
                .subscribe(new EmptySubscribe<Object>() {
                    @Override
                    public void onCompleted() {
                        try {
                            if (isChecked) {
                                EMClient.getInstance().groupManager().blockGroupMessage(chatId);
                            } else {
                                EMClient.getInstance().groupManager().unblockGroupMessage(chatId);
                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                        }
                    }
                });
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace) {
        // 这里不是修改群主，所以newOwner直接传空
        Subscription subscription = mRepository.updateGroup(chatGroupBean.getIm_group_id(), chatGroupBean.getName(), chatGroupBean.getDescription(), 0, 200, chatGroupBean.isMembersonly(),
                0, chatGroupBean.getGroup_face(), isEditGroupFace, "")
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("修改中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        // 成功后重置页面
                        LogUtils.d("updateGroup", data);
                        mRootView.updateGroup(data);
                        mRootView.dismissSnackBar();
                        EventBus.getDefault().post(mRootView.getGroupBean(), EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getGroupChatInfo(String groupId) {
        Subscription subscription = mRepository.getGroupChatInfo(groupId)
                .doOnSubscribe(() -> mRootView.isShowEmptyView(true, true))
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mRootView.getGroupInfoSuccess(data.get(0));
                        mRootView.isShowEmptyView(false, true);
                        mRootView.dismissSnackBar();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                        mRootView.isShowEmptyView(false, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                        mRootView.isShowEmptyView(false, false);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void createGroupFromSingleChat() {
        String name = mContext.getString(R.string.chat_single_name_default, 2);
        String member = AppApplication.getMyUserIdWithdefault() + "," + mRootView.getToUserId();
        Subscription subscription = mRepository.createGroup(name, "暂无", false,
                200, false, true, AppApplication.getMyUserIdWithdefault(), member)
                .doOnSubscribe(() -> {
                    // 这里的占位文字都没提供emm
                    mRootView.showSnackLoadingMessage("创建中..");
                })
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        mRootView.dismissSnackBar();
                        EventBus.getDefault().post(data, EventBusTagConfig.EVENT_IM_GROUP_CREATE_FROM_SINGLE);
                        mRootView.createGroupSuccess(data);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_IM_GROUP_EDIT_NAME)
    public void onGroupNameChanged(String newName) {
        mRootView.getGroupBean().setName(newName);
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        updateGroup(chatGroupBean, false);
    }

    @Subscriber(tag = EVENT_IM_GROUP_DATA_CHANGED)
    public void onGroupOwnerChanged(ChatGroupBean chatGroupBean) {
        mRootView.updateGroup(chatGroupBean);
    }

    @Subscriber(tag = EVENT_IM_GROUP_REMOVE_MEMBER)
    public void onGroupMemberRemoved(Bundle bundle) {
        List<UserInfoBean> removedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_REMOVE_MEMBER);
        if (removedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        List<UserInfoBean> originalList = new ArrayList<>();
        originalList.addAll(chatGroupBean.getAffiliations());
        for (int i = 0; i < removedList.size(); i++) {
            for (UserInfoBean userInfoBean : chatGroupBean.getAffiliations()) {
                if (removedList.get(i).getUser_id().equals(userInfoBean.getUser_id())) {
                    originalList.remove(userInfoBean);
                    break;
                }
            }
        }
        chatGroupBean.setAffiliations(originalList);
        mRootView.updateGroup(chatGroupBean);
    }

    @Subscriber(tag = EVENT_IM_GROUP_ADD_MEMBER)
    public void onGroupMemberAdded(Bundle bundle) {
        List<UserInfoBean> addedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_ADD_MEMBER);
        if (addedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        chatGroupBean.getAffiliations().addAll(addedList);
        mRootView.updateGroup(chatGroupBean);
    }
}
