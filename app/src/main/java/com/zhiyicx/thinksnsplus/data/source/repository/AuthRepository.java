package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class AuthRepository implements IAuthRepository{
    private CommonClient mCommonClient;
    private RegisterClient mRegisterClient;
    private Context mContext;

    public AuthRepository(ServiceManager serviceManager, Context context) {
        mCommonClient = serviceManager.getCommonClient();
        mRegisterClient = serviceManager.getRegisterClient();
        mContext = context;
    }


    @Override
    public boolean saveAuthBean(AuthBean authBean) {
        System.out.println("authBean =-------------------------------------------- " + authBean.getUser_id());
        return SharePreferenceUtils.saveObject(mContext, AuthBean.SHAREPREFERENCE_TAG, authBean);
    }
}
