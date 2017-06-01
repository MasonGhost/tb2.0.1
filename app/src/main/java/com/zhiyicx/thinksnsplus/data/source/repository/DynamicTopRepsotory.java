package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.top.DynamicTopContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicTopRepsotory implements DynamicTopContract.Repository {

    @Inject
    public DynamicTopRepsotory(ServiceManager serviceManager) {
    }

    @Override
    public void stickTop() {

    }
}
