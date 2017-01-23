package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class AuthRepository implements IAuthRepository {
    public static final int MAX_RETRY_COUNTS = 1;//重试次数
    public static final int RETRY_DELAY_TIME = 1;// 重试间隔时间,单位 s
    private UserInfoClient mUserInfoClient;
    private Context mContext;

    @Inject
    public AuthRepository(ServiceManager serviceManager, Application context) {
        mUserInfoClient = serviceManager.getUserInfoClient();
        mContext = context;
    }


    @Override
    public boolean saveAuthBean(AuthBean authBean) {
        return SharePreferenceUtils.saveObject(mContext, AuthBean.SHAREPREFERENCE_TAG, authBean);
    }

    @Override
    public AuthBean getAuthBean() {
        return SharePreferenceUtils.getObject(mContext, AuthBean.SHAREPREFERENCE_TAG);
    }

    public Observable<BaseJson<IMBean>> getImInfo() {
        return mUserInfoClient.getIMInfo()
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
