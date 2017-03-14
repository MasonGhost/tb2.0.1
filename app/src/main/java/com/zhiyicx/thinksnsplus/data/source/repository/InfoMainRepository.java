package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoMainRepository implements InfoMainContract.Reppsitory {

    private InfoMainClient mInfoMainClient;

    @Inject
    public InfoMainRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJson<InfoTypeBean>> getInfoType() {
        return mInfoMainClient.getInfoType();
    }

    @Override
    public Observable<BaseJson<InfoListBean>> getInfoList(String cate_id,
                                                                long max_id,
                                                                long limit,
                                                                long page) {
        return mInfoMainClient.getInfoList(cate_id, max_id, limit, page);
    }
}
