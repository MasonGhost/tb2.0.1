package com.zhiyicx.thinksnsplus.data.source.repository;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;

import java.util.ArrayList;
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
    protected UserInfoRepository mUserInfoRepository;
    protected Context mContext;

    @Inject
    public BaseDynamicRepository(ServiceManager serviceManager, Context context) {
        mContext = context;
        mDynamicClient = serviceManager.getDynamicClient();
        mUserInfoRepository = new UserInfoRepository(serviceManager);
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
     * @param page   页码 热门选填
     * @return
     */
    @Override
    public Observable<BaseJson<List<DynamicBean>>> getDynamicList(final String type, Long max_id, int page) {
        return mDynamicClient.getDynamicList(type, max_id, null, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<BaseJson<List<DynamicBean>>, Observable<BaseJson<List<DynamicBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<DynamicBean>>> call(final BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus() && listBaseJson.getData() != null && !listBaseJson.getData().isEmpty()) {
                            List<Long> user_ids = new ArrayList<>();
                            if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {// 如果是热门，需要初始化时间
                                for (int i = listBaseJson.getData().size() - 1; i >= 0; i--) {
                                    listBaseJson.getData().get(i).setHot_creat_time(System.currentTimeMillis());
                                }
                            }
                            for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                user_ids.add(dynamicBean.getUser_id());
                                if (type.equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) { //如果是关注，需要初始化标记
                                    dynamicBean.setIsFollowed(true);
                                }
                                if (type.equals(ApiConfig.DYNAMIC_TYPE_HOTS)) {
                                    dynamicBean.setMaxId(dynamicBean.getHot_creat_time());
                                } else {
                                    dynamicBean.setMaxId(dynamicBean.getFeed().getFeed_id());
                                }
                            }

                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(new Func1<BaseJson<List<UserInfoBean>>, BaseJson<List<DynamicBean>>>() {
                                        @Override
                                        public BaseJson<List<DynamicBean>> call(BaseJson<List<UserInfoBean>> userinfobeans) {
                                            if (userinfobeans.isStatus()) {
                                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                                for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                                }
                                                for (DynamicBean dynamicBean : listBaseJson.getData()) {
                                                    dynamicBean.setUserInfoBean(userInfoBeanSparseArray.get((int) dynamicBean.getUser_id()));
                                                }
                                                AppApplication.AppComponentHolder.getAppComponent().userInfoBeanGreenDao().insertOrReplace(userinfobeans.getData());
                                            } else {
                                                listBaseJson.setStatus(userinfobeans.isStatus());
                                                listBaseJson.setCode(userinfobeans.getCode());
                                                listBaseJson.setMessage(userinfobeans.getMessage());
                                            }
                                            return listBaseJson;
                                        }
                                    });
                        } else {
                            return Observable.just(listBaseJson);
                        }

                    }
                });
    }

    /**
     * @param feed_id
     * @return
     */
    @Override
    public Observable<BaseJson<String>> likeDynamic(Long feed_id) {
        return mDynamicClient.likeDynamic(feed_id);
    }

    /**
     * @param feed_id
     * @return
     */
    @Override
    public Observable<BaseJson<String>> cancleLikeDynamic(Long feed_id) {
        return mDynamicClient.cancleLikeDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<Object>> collectDynamic(Long feed_id) {
        return mDynamicClient.collectDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<Object>> cancleCollectDynamic(Long feed_id) {
        return mDynamicClient.cancleCollectDynamic(feed_id);
    }

    @Override
    public Observable<BaseJson<List<UserInfoBean>>> getDynamicDigList(Long feed_id, Integer max_id) {
        return mDynamicClient.getDynamicDigList(feed_id, max_id)
                .flatMap(new Func1<BaseJson<List<DynamicDigListBean>>, Observable<BaseJson<List<UserInfoBean>>>>() {
                    @Override
                    public Observable<BaseJson<List<UserInfoBean>>> call(BaseJson<List<DynamicDigListBean>> listBaseJson) {
                        List<Long> user_ids = null;
                        // 获取点赞列表成功
                        if (listBaseJson.isStatus()) {
                            user_ids = new ArrayList<Long>();
                            List<DynamicDigListBean> dynamicDigListBeanList = listBaseJson.getData();
                            if (dynamicDigListBeanList != null && !dynamicDigListBeanList.isEmpty()) {
                                for (DynamicDigListBean digListBean : dynamicDigListBeanList) {
                                    user_ids.add(digListBean.getUser_id());
                                }
                                return mUserInfoRepository.getUserInfo(user_ids);
                            } else {
                                // 不需要获取用户信息，发送一个空的BaseJson
                                BaseJson<List<UserInfoBean>> baseJsonUserInfoList = new BaseJson<List<UserInfoBean>>();
                                baseJsonUserInfoList.setData(new ArrayList<UserInfoBean>());
                                baseJsonUserInfoList.setStatus(true);
                                baseJsonUserInfoList.setCode(0);
                                baseJsonUserInfoList.setMessage("");
                                return Observable.just(baseJsonUserInfoList);
                            }
                        } else {
                            // 获取点赞列表失败
                            throw new JsonParseException(AppApplication.getContext().getString(R.string.get_dynamic_dig_list_failure));
                        }

                    }
                });
    }
}
