package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptListContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/28/14:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ManuscriptListRepository extends BaseInfoRepository implements ManuscriptListContract.Repository {

    @Inject
    public ManuscriptListRepository(ServiceManager manager) {
        super(manager);
    }
}
