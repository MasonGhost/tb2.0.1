package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ComponentConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentStatusBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.ArrayList;
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

    /**
     * 从服务器获取组件状态信息
     */
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

    /**
     * 通过组件查看配置信息
     *
     * @param component 组件
     */
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
