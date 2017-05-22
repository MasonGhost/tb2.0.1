package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.wallet.recharge.RechargeContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class RechargeRepository implements RechargeContract.Repository {

    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    protected SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    @Inject
    protected ChatRepository mChatRepository;

    private CommonClient mCommonClient;
    private Context mContext;

    @Inject
    public RechargeRepository(ServiceManager serviceManager, Application context) {
        mCommonClient = serviceManager.getCommonClient();
        mContext = context;
    }
}
