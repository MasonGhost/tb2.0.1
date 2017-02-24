package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;


/**
 * @Describe 动态数据处理基类
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */

public class BaseDynamicRepository implements IDynamicReppsitory {
    protected DynamicClient mDynamicClient;
    protected Context mContext;

    @Inject
    public BaseDynamicRepository(ServiceManager serviceManager, Context context) {
        mDynamicClient = serviceManager.getDynamicClient();
        mContext = context;
    }

    /**
     * 发布动态
     *
     * @param dynamicDetailBean dynamic content
     * @return
     */
    @Override
    public Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mDynamicClient.sendDynamic(body);
    }
}
