package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoSearchRepository implements SearchContract.Repository {

    private InfoMainClient mInfoMainClient;

    @Inject
    public InfoSearchRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJson<List<InfoListDataBean>>> searchInfoList(String key, long max_id) {
        return mInfoMainClient.searchInfoList(key,max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
    }
}
