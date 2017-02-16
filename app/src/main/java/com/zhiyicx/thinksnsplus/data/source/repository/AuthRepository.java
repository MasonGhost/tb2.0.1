package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

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
        AppApplication.setmCurrentLoginAuth(authBean);
        return SharePreferenceUtils.saveObject(mContext, AuthBean.SHAREPREFERENCE_TAG, authBean);
    }

    @Override
    public AuthBean getAuthBean() {
        AppApplication.setmCurrentLoginAuth((AuthBean) SharePreferenceUtils.getObject(mContext, AuthBean.SHAREPREFERENCE_TAG));
        return AppApplication.getmCurrentLoginAuth();
    }

    @Override
    public Observable<BaseJson<IMBean>> getImInfo() {
        return mUserInfoClient.getIMInfo()
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 刷新token
     */
    @Override
    public void refreshToken() {
        AuthBean authBean = getAuthBean();
        if (!isNeededRefreshToken(authBean)) {
            return;
        }
        CommonClient commonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();
        String imei = DeviceUtils.getIMEI(mContext);
        commonClient.refreshToken(authBean.getRefresh_token(), imei)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        // 获取了最新的token，将这些信息保存起来
                        saveAuthBean(data);
                        // 刷新im信息
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.GET_IM_INFO));
                    }

                    @Override
                    protected void onFailure(String message) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    /**
     * 删除认证信息
     *
     * @return
     */
    @Override
    public boolean clearAuthBean() {
        return SharePreferenceUtils.remove(mContext, AuthBean.SHAREPREFERENCE_TAG);
    }

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        return getAuthBean() != null;
    }

    /**
     * 是否需要刷新token
     *
     * @return
     */
    private boolean isNeededRefreshToken(AuthBean authBean) {
        if (authBean == null) {// 没有token，不需要刷新
            return false;
        }
        long createTime = authBean.getCreated_at();
        int expiers = authBean.getExpires();
        int days = TimeUtils.getifferenceDays((createTime + expiers) * 1000);//表示token过期时间距离现在的时间
        if (expiers == 0) {// 永不过期,不需要刷新token
            return false;
        } else if (days >= -1) {// 表示当前时间是过期时间的前一天,或者已经过期,需要尝试刷新token
            return true;
        }
        return false;
    }

}
