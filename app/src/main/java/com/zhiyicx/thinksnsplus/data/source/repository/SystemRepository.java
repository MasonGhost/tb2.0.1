package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ComponentConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.ComponentStatusBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class SystemRepository implements ISystemRepository {
    private CommonClient mCommonClient;
    private Context mContext;

    @Inject
    public SystemRepository(ServiceManager serviceManager, Application context) {
        mCommonClient = serviceManager.getCommonClient();
        mContext = context;
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

    @Override
    public Observable<BaseJson<CacheBean>> systemFeedback(String content) {
        return null;
    }

    @Override
    public Observable<BaseJson<List<SystemConversationBean>>> getSystemConversations(long max_id, int limit) {
        return null;
    }
}
