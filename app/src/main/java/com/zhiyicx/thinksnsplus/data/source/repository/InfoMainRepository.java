package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
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
        // 如果传入的cate_id是collections，表示需要获取收藏列表，那么path需要添加该字段；
        // 如果传入的cate_id表示的是咨询列表类型，那么path为空即可
        String type = "";
        switch (cate_id) {
            case ApiConfig.INFO_TYPE_COLLECTIONS:
                type = ApiConfig.INFO_TYPE_COLLECTIONS;
                break;
            default:
                type = "";
        }
        return mInfoMainClient.getInfoList(type, cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page);
    }


}
