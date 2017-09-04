package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoMainRepository extends BaseInfoRepository implements InfoMainContract.Repository {

    @Inject
    public InfoMainRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<InfoTypeBean> getInfoType() {
        return mInfoMainClient.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<InfoListDataBean>> getInfoListV2(String cate_id, String key, long max_id, long page, int isRecommend) {
        switch (cate_id) {
            case ApiConfig.INFO_TYPE_COLLECTIONS:
                return getCollectionListV2(max_id);
            default:
                return super.getInfoListV2(cate_id, key, max_id, page, isRecommend);
        }
    }
}
