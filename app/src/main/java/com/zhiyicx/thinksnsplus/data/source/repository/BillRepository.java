package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.wallet.bill.BillContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BillRepository implements BillContract.Repository {

    @Inject
    public BillRepository(ServiceManager serviceManager) {
    }
}
