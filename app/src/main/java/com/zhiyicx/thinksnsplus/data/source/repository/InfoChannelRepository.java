package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.InfoChannelConstract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoChannelRepository implements InfoChannelConstract.Reppsitory {

    private InfoMainClient mInfoMainClient;
    private Application mContext;

    @Inject
    public InfoChannelRepository(ServiceManager serviceManager, Application context) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        mContext = context;
    }

    @Override
    public Observable<BaseJson<Integer>> doSubscribe(String follows) {
        return mInfoMainClient.doSubscribe(follows);
    }

    @Override
    public void handleSubscribe(final String follows) {
        Observable.just(follows)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("follows", follows);
                        // 后台处理
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.POST, params);
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_INFO_FOLLOW_LIST, follows));
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                                (backgroundRequestTaskBean);
                    }
                });
    }
}
