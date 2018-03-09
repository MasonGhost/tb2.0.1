package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.zhiyicx.appupdate.AppVersionBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ISystemRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_INTERVAL_TIME;
import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_MAX_COUNT;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class SystemRepository implements ISystemRepository {

    public static final int DEFAULT_TS_HELPER_TIP_MSG_ID = -1000;
    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    protected SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    private CommonClient mCommonClient;
    private Context mContext;

    @Inject
    public SystemRepository(ServiceManager serviceManager, Application context) {
        mCommonClient = serviceManager.getCommonClient();
        mContext = context;
    }

    /**
     * 去获取服务器启动信息
     */
    @Override
    public void getBootstrappersInfoFromServer() {
        mCommonClient.getBootstrappersInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribeForV2<SystemConfigBean>() {
                    @Override
                    protected void onSuccess(SystemConfigBean data) {
                        saveComponentStatus(data, mContext);
                    }
                });
    }

    public Observable<SystemConfigBean> getBootstrappersInfo() {
        return mCommonClient.getBootstrappersInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 去获取本地启动信息
     *
     * @return
     */
    @Override
    public SystemConfigBean getBootstrappersInfoFromLocal() {
        SystemConfigBean systemConfigBean = null;
        try {
            systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig
                    .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 读取本地默认配置
        if (systemConfigBean == null) {
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        return systemConfigBean;
    }

    public SystemConfigBean getAppConfigInfoFromLocal() {
        SystemConfigBean systemConfigBean = null;
        try {
            systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig
                    .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception ignored) {
        }
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        return systemConfigBean;
    }

    @Override
    public String checkTShelper(long user_id) {
        if (getBootstrappersInfoFromLocal() == null || getBootstrappersInfoFromLocal().getIm_helper() == null) {
            return null;
        }
        List<SystemConfigBean.ImHelperBean> tshleprs = getBootstrappersInfoFromLocal().getIm_helper();
        for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
            if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                return tshlepr.getUrl();
            }
        }
        return null;
    }

    /**
     * 静态检测用户是否是 ts 助手
     *
     * @param context context
     * @param user_id user  id
     * @return
     */
    public static String checkHelperUrl(Context context, long user_id) {
        String tsHelperUrl = null;
        SystemConfigBean systemConfigBean = null;
        try {

            systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig
                    .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception ignored) {
        }
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
            List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
            for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                    tsHelperUrl = tshlepr.getUrl();
                    break;
                }
            }
        }
        return tsHelperUrl;
    }

    /**
     * 重置 ts 助手
     *
     * @param context context
     * @return
     */
    public static void resetTSHelper(Context context) {
        String tsHelperUrl = null;
        SystemConfigBean systemConfigBean = null;
        try {
            systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig
                    .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception ignored) {
        }
        // 读取本地默认配置
        if (systemConfigBean == null) {
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        } else {
            if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
                List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
                for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                    tshlepr.setDelete(false);
                }
            }
        }
        SharePreferenceUtils.saveObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    /**
     * update tsHelper  delete status
     *
     * @param context context
     * @param user_id user_id
     */
    public static void updateTsHelperDeletStatus(Context context, long user_id, boolean isDelete) {
        SystemConfigBean systemConfigBean = null;
        try {
            systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig
                    .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception ignored) {
        }
        // 读取本地默认配置
        if (systemConfigBean == null) {
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
            List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
            for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                    tshlepr.setDelete(isDelete);
                    break;
                }
            }
        }
        SharePreferenceUtils.saveObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    /**
     * 保存启动信息
     *
     * @param systemConfigBean
     * @return
     */
    public boolean saveComponentStatus(SystemConfigBean systemConfigBean, Context context) {
        if (systemConfigBean == null) {
            SystemConfigBean localSystemConfigBean = null;
            try {
                localSystemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig
                        .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
            } catch (Exception igonre) {
            }

            if (localSystemConfigBean == null) { // 读取本地默认配置
                localSystemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
            }
            if (localSystemConfigBean == null || localSystemConfigBean.getIm_helper() == null || localSystemConfigBean.getIm_helper().isEmpty()) {
                return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
            }
            for (SystemConfigBean.ImHelperBean imHelperBean : localSystemConfigBean.getIm_helper()) {

                for (SystemConfigBean.ImHelperBean tshlepr : systemConfigBean.getIm_helper()) {
                    if (imHelperBean.getUid().equals(tshlepr.getUid())) {
                        tshlepr.setDelete(imHelperBean.isDelete());
                    }
                }
            }
            return false;
        }

        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    /**
     * 系统反馈
     *
     * @param content 反馈内容
     * @return
     */
    @Override
    public Observable<Object> systemFeedback(String content, long system_mark) {
        return mCommonClient.systemFeedback(content, system_mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取全部标签
     *
     * @return
     */
    @Override
    public Observable<List<TagCategoryBean>> getAllTags() {
        return mCommonClient.getAllTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * @param name search content
     * @return
     */
    @Override
    public Observable<List<LocationContainerBean>> searchLocation(String name) {
        return mCommonClient.searchLocation(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 热门城市
     *
     * @return
     */
    @Override
    public Observable<List<String>> getHoCity() {
        return mCommonClient.getHoCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<AppVersionBean>> getAppNewVersion() {

        return mCommonClient.getAppNewVersion(DeviceUtils.getVersionCode(mContext), "android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
