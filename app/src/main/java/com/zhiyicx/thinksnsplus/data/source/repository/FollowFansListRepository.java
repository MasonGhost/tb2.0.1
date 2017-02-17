package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
    public Observable<BaseJson<List<FollowFansBean>>> getFollowListFromNet(long userId, int maxId) {
        return mFollowFansClient.getUserFollowsList(userId, maxId);
    }

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFansListFromNet(long userId, int maxId) {
        return mFollowFansClient.getUserFansList(userId, maxId);
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
