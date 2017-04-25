package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.system_conversation.SystemConversationContract;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */

public class SystemConversationRepository extends SystemRepository implements SystemConversationContract.Repository {

    @Inject
    public SystemConversationRepository(ServiceManager serviceManager, Application application) {
        super(serviceManager, application);
        if (mSystemConversationBeanGreenDao == null) {
            mSystemConversationBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().systemConversationBeanGreenDaoImpl();
        }
    }

    @Override
    public List<SystemConversationBean> requestCacheData(long max_Id) {
        return mSystemConversationBeanGreenDao.getMultiDataFromCacheByMaxId(max_Id);
    }
}
