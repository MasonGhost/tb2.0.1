package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

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
        return SharePreferenceUtils.saveObject(mContext, AuthBean.SHAREPREFERENCE_TAG, authBean);
    }

    public Observable<BaseJson<IMBean>> getImInfo() {
        return mCommonClient.getIMInfo()
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
