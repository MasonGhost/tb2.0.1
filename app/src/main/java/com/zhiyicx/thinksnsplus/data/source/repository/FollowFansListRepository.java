package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GsonFollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class FollowFansListRepository implements FollowFansListContract.Repository {
    private FollowFansClient mFollowFansClient;

    public FollowFansListRepository(ServiceManager serviceManager) {
        mFollowFansClient = serviceManager.getFollowFansClient();
    }

/*    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFollowFansListFromNet(int userId, boolean isFollowed) {
        return Observable.create(new Observable.OnSubscribe<BaseJson<List<FollowFansBean>>>() {
            @Override
            public void call(Subscriber<? super BaseJson<List<FollowFansBean>>> subscriber) {
                List<FollowFansBean> datas = new ArrayList<FollowFansBean>();
                for (int i = 0; i < 10; i++) {
                    FollowFansBean followFansItemBean = new FollowFansBean();
                    followFansItemBean.setFollowState(1);
                    followFansItemBean.setUserId(20000l);
                    followFansItemBean.setFollowedUserId(10002l + i);
                    followFansItemBean.setUserFollowedId("");
                    datas.add(followFansItemBean);
                }
                BaseJson<List<FollowFansBean>> baseJson = new BaseJson<List<FollowFansBean>>();
                baseJson.setStatus(true);
                baseJson.setData(datas);
                baseJson.setCode(0);
                baseJson.setMessage("数据获取成功");
                subscriber.onNext(baseJson);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }*/

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFollowListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFollowsList(userId, maxId)
                .map(new Func1<BaseJson<GsonFollowFansBean>, BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public BaseJson<List<FollowFansBean>> call(BaseJson<GsonFollowFansBean> gsonFollowFansBeanBaseJson) {
                        GsonFollowFansBean gsonFollowFansBean = gsonFollowFansBeanBaseJson.getData();
                        List<FollowFansBean> followFansBeanList = new ArrayList<FollowFansBean>();
                        for (GsonFollowFansBean.GsonFollowsBean gsonFollowsBean : gsonFollowFansBean.getFollows()) {
                            FollowFansBean followFansBean = new FollowFansBean();
                            // 关注主体是当前传入的userId
                            followFansBean.setUserId(userId);
                            followFansBean.setFollowedUserId(gsonFollowsBean.getUser_id());
                            followFansBean.setFollowState(FollowFansBean.IFOLLOWED_STATE);
                            followFansBean.setUserFollowedId(null);
                            followFansBeanList.add(followFansBean);
                        }
                        BaseJson<List<FollowFansBean>> listBaseJson = new BaseJson<List<FollowFansBean>>();
                        listBaseJson.setCode(gsonFollowFansBeanBaseJson.getCode());
                        listBaseJson.setMessage(gsonFollowFansBeanBaseJson.getMessage());
                        listBaseJson.setStatus(gsonFollowFansBeanBaseJson.isStatus());
                        listBaseJson.setData(followFansBeanList);
                        return listBaseJson;
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFansListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFansList(userId, maxId)
                .map(new Func1<BaseJson<GsonFollowFansBean>, BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public BaseJson<List<FollowFansBean>> call(BaseJson<GsonFollowFansBean> gsonFollowFansBeanBaseJson) {
                        GsonFollowFansBean gsonFollowFansBean = gsonFollowFansBeanBaseJson.getData();
                        List<FollowFansBean> followFansBeanList = new ArrayList<FollowFansBean>();
                        for (GsonFollowFansBean.GsonFollowsBean gsonFollowsBean : gsonFollowFansBean.getFolloweds()) {
                            FollowFansBean followFansBean = new FollowFansBean();
                            // 关注主体是当前从服务器获取到的user_id
                            followFansBean.setUserId(gsonFollowsBean.getUser_id());
                            followFansBean.setFollowedUserId(userId);
                            followFansBean.setFollowState(FollowFansBean.IFOLLOWED_STATE);
                            followFansBean.setUserFollowedId(null);
                            followFansBeanList.add(followFansBean);
                        }
                        BaseJson<List<FollowFansBean>> listBaseJson = new BaseJson<List<FollowFansBean>>();
                        listBaseJson.setCode(gsonFollowFansBeanBaseJson.getCode());
                        listBaseJson.setMessage(gsonFollowFansBeanBaseJson.getMessage());
                        listBaseJson.setStatus(gsonFollowFansBeanBaseJson.isStatus());
                        listBaseJson.setData(followFansBeanList);
                        return listBaseJson;
                    }
                });
    }

    @Override
    public Observable<BaseJson> followUser(long userId) {
        return mFollowFansClient.followUser(userId);
    }

    @Override
    public Observable<BaseJson> cancleFollowUser(long userId) {
        return mFollowFansClient.cancelFollowUser(userId);
    }
}
