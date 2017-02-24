package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;


/**
 * @Describe 动态列表相关数据处理
 * @Author Jungle68
 * @Date 2017/2/24
 * @Contact master.jungle68@gmail.com
 */

public class DynamicRepository extends BaseDynamicRepository implements DynamicContract.Repository {

    @Inject
    public DynamicRepository(ServiceManager serviceManager, Application context) {
        super(serviceManager,context);
    }

    @Override
    public Observable<BaseJson<List<DynamicBean>>> getHistoryDynamicList(String type, long max_id, long limit, long page) {
        return Observable.just(new BaseJson<List<DynamicBean>>());
    }
}
