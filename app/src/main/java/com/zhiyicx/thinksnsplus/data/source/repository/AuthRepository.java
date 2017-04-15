package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentStatusBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/19
 * @Contact master.jungle68@gmail.com
 */

public class AuthRepository implements IAuthRepository {
    public static final int MAX_RETRY_COUNTS = 2;//重试次数
    public static final int RETRY_DELAY_TIME = 1;// 重试间隔时间,单位 s
    private UserInfoClient mUserInfoClient;
    private CommonClient mCommonClient;
    private Context mContext;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DigedBeanGreenDaoImpl mDigedBeanGreenDao;
    @Inject
    CommentedBeanGreenDaoImpl mCommentedBeanGreenDao;
    @Inject
    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    @Inject
    public AuthRepository(ServiceManager serviceManager, Application context) {
        mUserInfoClient = serviceManager.getUserInfoClient();
        mCommonClient = serviceManager.getCommonClient();
        mContext = context;
        if (mDynamicBeanGreenDao == null) {
            mDynamicBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicBeanGreenDao();
        }
        if (mDynamicDetailBeanGreenDao == null) {
            mDynamicDetailBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicDetailBeanGreenDao();
        }
        if (mDynamicToolBeanGreenDao == null) {
            mDynamicToolBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicToolBeanGreenDao();
        }
        if (mDynamicCommentBeanGreenDao == null) {
            mDynamicCommentBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().dynamicCommentBeanGreenDao();
        }
        if (mDigedBeanGreenDao == null) {
            mDigedBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().digedBeanGreenDao();
        }
        if (mCommentedBeanGreenDao == null) {
            mCommentedBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().commentedBeanGreenDao();
        }
        if (mFlushMessageBeanGreenDao == null) {
            mFlushMessageBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().flushMessageBeanGreenDao();
        }
    }


    @Override
    public boolean saveAuthBean(AuthBean authBean) {
        AppApplication.setmCurrentLoginAuth(authBean);
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN, authBean);
    }

    @Override
    public AuthBean getAuthBean() {
        AppApplication.setmCurrentLoginAuth((AuthBean) SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN));
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
                    protected void onFailure(String message, int code) {

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
        if (AppApplication.getPlaybackManager() != null) { // 释放音乐播放器
            AppApplication.getPlaybackManager().handleStopRequest(null);
        }
        MessageDao.getInstance(mContext).delDataBase();// 清空聊天信息、对话
        mDynamicBeanGreenDao.clearTable();
        mDynamicCommentBeanGreenDao.clearTable();
        mDynamicDetailBeanGreenDao.clearTable();
        mDynamicToolBeanGreenDao.clearTable();
        mDigedBeanGreenDao.clearTable();
        mCommentedBeanGreenDao.clearTable();
        mFlushMessageBeanGreenDao.clearTable();
        MessageDao.getInstance(context).delDataBase();
        return SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IMCONFIG);
    }

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        return getAuthBean() != null && getIMConfig() != null;
    }

    @Override
    public boolean saveIMConfig(IMConfig imConfig) {
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IMCONFIG, imConfig);
    }

    @Override
    public IMConfig getIMConfig() {
        return SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IMCONFIG);
    }

    @Override
    public void loginIM() {
        ZBIMClient.getInstance().login(getIMConfig());
    }

    /**
     * @return
     */
    public ComponentStatusBean getComponentStatusLocal() {
        ComponentStatusBean componentStatusBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_COMPONENT_STATUS);
        if (componentStatusBean == null) { //默认开启 IM
            componentStatusBean = new ComponentStatusBean();
            componentStatusBean.setIm(true);
        }
        return componentStatusBean;
    }

    /**
     * @param componentStatusBean
     * @return
     */
    public boolean saveComponentStatus(ComponentStatusBean componentStatusBean) {
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_COMPONENT_STATUS, componentStatusBean);
    }

    /**
     * @return
     */
    public List<ComponentConfigBean> getComponentConfigLocal() {
        List<ComponentConfigBean> result = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_COMPONENT_CONFIG);
        if (result == null || result.size() == 0) { //本地默认地址
            result = new ArrayList<>();
            ComponentConfigBean componentConfigBean = new ComponentConfigBean();
            componentConfigBean.setName("serverurl");
            componentConfigBean.setValue(ApiConfig.APP_IM_DOMAIN);
            result.add(componentConfigBean);
        }
        return result;
    }

    /**
     * @param componentConfigBeens
     * @return
     */
    public boolean saveComponentConfig(List<ComponentConfigBean> componentConfigBeens) {
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_COMPONENT_CONFIG, componentConfigBeens);
    }

    @Override
    public void getComponentStatusFromServer() {
        mCommonClient.getComponentStatus()
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<ComponentStatusBean>() {
                    @Override
                    protected void onSuccess(ComponentStatusBean data) {
                        saveComponentStatus(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void getComponentConfigFromServer(String component) {
        mCommonClient.getComponentConfigs(component)
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<List<ComponentConfigBean>>() {
                    @Override
                    protected void onSuccess(List<ComponentConfigBean> data) {
                        saveComponentConfig(data);

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
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
