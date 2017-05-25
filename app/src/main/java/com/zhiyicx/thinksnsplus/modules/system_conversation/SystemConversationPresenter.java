package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/26
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationPresenter extends AppBasePresenter<SystemConversationContract.Repository, SystemConversationContract.View> implements SystemConversationContract.Presenter {
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;


    @Inject
    public SystemConversationPresenter(SystemConversationContract.Repository repository, SystemConversationContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List data, boolean isLoadMore) {
        return false;
    }
}
