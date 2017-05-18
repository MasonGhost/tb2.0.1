package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class SystemRepository implements ISystemRepository {

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
        if (mSystemConversationBeanGreenDao == null) {
            mSystemConversationBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().systemConversationBeanGreenDaoImpl();
        }
        if (mUserInfoBeanGreenDao == null) {
            mUserInfoBeanGreenDao = AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao();
        }

    }

    /**
     * 去获取服务器启动信息
     */
    @Override
    public void getBootstrappersInfoFromServer() {
        mCommonClient.getBootstrappersInfo()
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribeForV2<SystemConfigBean>() {
                    @Override
                    protected void onSuccess(SystemConfigBean data) {
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

    /**
     * 去获取本地启动信息
     *
     * @return
     */
    @Override
    public SystemConfigBean getBootstrappersInfoFromLocal() {
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        return systemConfigBean;
    }

    /**
     * 保存启动信息
     *
     * @param systemConfigBean
     * @return
     */
    private boolean saveComponentStatus(SystemConfigBean systemConfigBean) {
        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }


    /**
     * 系统反馈
     *
     * @param content 反馈内容
     * @return
     */
    @Override
    public Observable<BaseJson<Object>> systemFeedback(String content, long system_mark) {
        return mCommonClient.systemFeedback(content, system_mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取系统对话信息
     *
     * @param max_id 分页标识
     * @param limit  每页数量
     * @return
     */
    @Override
    public Observable<BaseJson<List<SystemConversationBean>>> getSystemConversations(long max_id, int limit) {
        return mCommonClient.getSystemConversations(max_id, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseJson<List<SystemConversationBean>>, BaseJson<List<SystemConversationBean>>>() {
                    @Override
                    public BaseJson<List<SystemConversationBean>> call(BaseJson<List<SystemConversationBean>> listBaseJson) {
                        if (listBaseJson.isStatus()) {
                            descNetSystemConversation(listBaseJson.getData());
                            mSystemConversationBeanGreenDao.saveMultiData(listBaseJson.getData());
                            handleTsHelperUserInfo(listBaseJson.getData());
                        }
                        return listBaseJson;
                    }
                });
    }

    private void descNetSystemConversation(List<SystemConversationBean> datas) {
        Collections.sort(datas, new Comparator<SystemConversationBean>() { // 排序，最大的放在最后面
            @Override
            public int compare(SystemConversationBean o1, SystemConversationBean o2) {
                return o1.getId().intValue() - o2.getId().intValue();
            }
        });
    }

    private void descCacheSystemConversation(List<SystemConversationBean> datas) {
        Collections.sort(datas, new Comparator<SystemConversationBean>() { // 排序，最大的放在最后面
            @Override
            public int compare(SystemConversationBean o1, SystemConversationBean o2) {
                return (int) (TimeUtils.utc2LocalLong(o1.getCreated_at()) - TimeUtils.utc2LocalLong(o2.getCreated_at()));
            }
        });
    }

    @Override
    public List<SystemConversationBean> requestCacheData(long max_Id) {
        List<SystemConversationBean> list = mSystemConversationBeanGreenDao.getMultiDataFromCacheByMaxId(max_Id);
//        descCacheSystemConversation(list);
        handleTsHelperUserInfo(list);
        return list;
    }

    /**
     * 处理 TS 助手和用户信息
     *
     * @param list
     */
    private void handleTsHelperUserInfo(List<SystemConversationBean> list) {
        UserInfoBean myUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(Long.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()));
        UserInfoBean tsHleper = new UserInfoBean();
        tsHleper.setName(mContext.getString(R.string.ts_helper));
        for (SystemConversationBean systemConversationBean : list) {
            systemConversationBean.setUserInfo(systemConversationBean.getUser_id() == null || systemConversationBean.getUser_id() == 0 ? tsHleper : myUserInfo);
            systemConversationBean.setToUserInfo(systemConversationBean.getTo_user_id() == null || systemConversationBean.getTo_user_id() == 0 ? tsHleper : myUserInfo);
        }
    }
}
