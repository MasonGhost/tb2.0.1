package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.SendDynamicContract;

import java.util.HashMap;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/21
 * @contact email:450127106@qq.com
 */

public class SendDynamicPresenterRepository implements SendDynamicContract.Repository {
    private DynamicClient mDynamicClient;

    public SendDynamicPresenterRepository(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
    }

    @Override
    public Observable<BaseJson<Object>> sendDynamic(HashMap<String, Object> params) {
        return mDynamicClient.sendDynamic(params);
    }
}
