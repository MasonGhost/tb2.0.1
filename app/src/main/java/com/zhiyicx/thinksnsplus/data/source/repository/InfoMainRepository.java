package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

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
                                                                long page) {
        return mInfoMainClient.getInfoList(cate_id, max_id,  Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page);
    }


}
