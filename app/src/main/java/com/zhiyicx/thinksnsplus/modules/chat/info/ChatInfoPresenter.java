package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import javax.inject.Inject;

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
}
