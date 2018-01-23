package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_CHANGE_OWNER;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_DATA_CHANGED;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_EDIT_NAME;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoPresenter extends AppBasePresenter<ChatInfoContract.Repository, ChatInfoContract.View>
        implements ChatInfoContract.Presenter{

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public ChatInfoPresenter(ChatInfoContract.Repository repository, ChatInfoContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public boolean isGroupOwner() {
        String owner = EMClient.getInstance().groupManager().getGroup(mRootView.getChatId()).getOwner();
        return owner.equals(String.valueOf(AppApplication.getMyUserIdWithdefault()));
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace) {
        Subscription subscription = mRepository.updateGroup(chatGroupBean.getIm_group_id(), chatGroupBean.getName(), chatGroupBean.getDescription(), 0, 200, chatGroupBean.isMembersonly(),
                0, chatGroupBean.getGroup_face(), isEditGroupFace)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("修改中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        // 成功后重置页面
                        LogUtils.d("updateGroup", data);
                        mRootView.updateGroup(data);
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
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_IM_GROUP_EDIT_NAME)
    public void onGroupNameChanged(String newName){
        mRootView.getGroupBean().setName(newName);
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        updateGroup(chatGroupBean, false);
    }

    @Subscriber(tag = EVENT_IM_GROUP_DATA_CHANGED)
    public void onGroupOwnerChanged(ChatGroupBean chatGroupBean){
        mRootView.updateGroup(chatGroupBean);
    }
}
