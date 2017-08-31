package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

import javax.inject.Inject;


/**
 * @Describe 动态列表相关数据处理
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public class DynamicRepository extends BaseDynamicRepository implements DynamicContract.Repository {

    @Inject
    public DynamicRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

}
