package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeContract;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/9/19
 * @contact email:648129313@qq.com
 */

public class SelectDynamicTypeRepository implements SelectDynamicTypeContract.Repository{

    @Inject
    public SelectDynamicTypeRepository(ServiceManager manager) {
    }
}
