package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


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
     * publish dynamic
     *
     * @param dynamicDetailBean dynamic content
     * @return
     */
    @Override
    public Observable<BaseJson<Object>> sendDynamic(DynamicDetailBean dynamicDetailBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(dynamicDetailBean));
        return mDynamicClient.sendDynamic(body);
    }

    /**
     * get dynamic list
     *
     * @param type   "" 代表最新；follows 代表关注 ； hots 代表热门
     * @param max_id 用来翻页的记录id(对应数据体里的feed_id ,最新和关注选填)
     * @param limit  请求数据条数 默认10条
     * @param page   页码 热门选填
     * @return
     */
    @Override
    public Observable<BaseJson<List<DynamicBean>>> getDynamicList(final String type, Long max_id, Long limit, Long page) {
        return mDynamicClient.getDynamicList(type, max_id, limit, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<BaseJson<List<DynamicBean>>, BaseJson<List<DynamicBean>>>() {// 热门需要单独唯一标识，方便本地查找
                    @Override
                    public BaseJson<List<DynamicBean>> call(BaseJson<List<DynamicBean>> listBaseJson) {
                        if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS))// 如果是热门，需要初始化时间
                            if (listBaseJson.isStatus()) {
                                for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                    dynamicBean.setHot_creat_time(System.currentTimeMillis());
                                }
                            }
                        return listBaseJson;
                    }
                });
    }
}
