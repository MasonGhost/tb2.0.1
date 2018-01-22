package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void updateGroup(String im_group_id, String groupName, String groupIntro, int isPublic, int maxUser,
                            boolean isMemberOnly, int isAllowInvites, String groupFace) {
        Subscription subscription = mRepository.updateGroup(im_group_id, groupName, groupIntro, isPublic, maxUser, isMemberOnly,
                isAllowInvites, groupFace)
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
}
