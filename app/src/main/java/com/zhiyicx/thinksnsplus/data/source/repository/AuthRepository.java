package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.manage.ZBIMClient;
import com.zhiyicx.rxerrorhandler.functions.RetryWithDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommentedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DigedBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.TopDynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.jpush.JpushAlias;
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
    public static final int MAX_RETRY_COUNTS = 2;//重试次数
    public static final int RETRY_DELAY_TIME = 1;// 重试间隔时间,单位 s
    private UserInfoClient mUserInfoClient;

    @Inject
    Application mContext;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    TopDynamicBeanGreenDaoImpl mTopDynamicBeanGreenDao;
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
    SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;
    @Inject
    GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public AuthRepository(ServiceManager serviceManager) {
        mUserInfoClient = serviceManager.getUserInfoClient();
    }


    @Override
    public boolean saveAuthBean(AuthBean authBean) {
        AppApplication.setmCurrentLoginAuth(authBean);
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN, authBean);
    }

    @Override
    public AuthBean getAuthBean() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            AppApplication.setmCurrentLoginAuth(SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN));
        }
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
        if (!isNeededRefreshToken()) {
            return;
        }
        CommonClient commonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();
        commonClient.refreshToken(authBean.getToken())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(MAX_RETRY_COUNTS, RETRY_DELAY_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
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
        BackgroundTaskManager.getInstance(mContext).closeBackgroundTask();// 关闭后台任务
        new JpushAlias(mContext, "").setAlias(); // 注销极光
        MessageDao.getInstance(mContext).delDataBase();// 清空聊天信息、对话
        mDynamicBeanGreenDao.clearTable();
        mDynamicCommentBeanGreenDao.clearTable();
        mGroupInfoBeanGreenDao.clearTable();
        mDynamicDetailBeanV2GreenDao.clearTable();
        mDynamicDetailBeanGreenDao.clearTable();
        mDynamicToolBeanGreenDao.clearTable();
        mTopDynamicBeanGreenDao.clearTable();
        mDigedBeanGreenDao.clearTable();
        mCommentedBeanGreenDao.clearTable();
        mFlushMessageBeanGreenDao.clearTable();
        mSystemConversationBeanGreenDao.clearTable();
        MessageDao.getInstance(mContext).delDataBase();
        AppApplication.setmCurrentLoginAuth(null);
        //处理 Ts 助手
        SystemRepository.resetTSHelper(mContext);
        return SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_AUTHBEAN)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IMCONFIG)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET)
                && SharePreferenceUtils.remove(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_LAST_FLUSHMESSAGE_TIME);
    }

    /**
     * 是否登录过成功了，Token 并未过期
     *
     * @return
     */
    @Override
    public boolean isLogin() {
        return !isTourist()
                && getAuthBean() != null;
    }

    /**
     * 是否是游客
     *
     * @return true
     */
    @Override
    public boolean isTourist() {
        return getAuthBean() == null;
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
     * 是否需要刷新token
     *
     * @return
     */
    @Override
    public boolean isNeededRefreshToken() {
        AuthBean authBean = getAuthBean();
        if (authBean == null) {// 没有token，不需要刷新
            return false;
        }
        return authBean.getToken_is_expired();
    }

}
