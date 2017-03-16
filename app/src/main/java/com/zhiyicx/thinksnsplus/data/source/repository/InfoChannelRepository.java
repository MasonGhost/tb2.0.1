package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.InfoChannelConstract;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoChannelRepository implements InfoChannelConstract.Reppsitory {

    private InfoMainClient mInfoMainClient;

    @Inject
    public InfoChannelRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJson<Integer>> doSubscribe(String follows) {
            return mInfoMainClient.doSubscribe(follows);
    }
}
