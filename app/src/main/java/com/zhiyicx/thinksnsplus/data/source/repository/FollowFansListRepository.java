package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GsonFollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.remote.FollowFansClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getFollowListFromNet(final long userId, int maxId) {
        // 将网络请求获取的数据，通过map转换
        return mFollowFansClient.getUserFollowsList(userId, maxId)
                .map(new Func1<BaseJson<GsonFollowFansBean>, BaseJson<List<FollowFansBean>>>() {
                    @Override
                    public BaseJson<List<FollowFansBean>> call(BaseJson<GsonFollowFansBean> gsonFollowFansBeanBaseJson) {
                        GsonFollowFansBean gsonFollowFansBean = gsonFollowFansBeanBaseJson.getData();
                        List<FollowFansBean> followFansBeanList = gsonFollowFansBean.getFollows();
                        return packageData(followFansBeanList, userId, gsonFollowFansBeanBaseJson);
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
                        List<FollowFansBean> followFansBeanList = gsonFollowFansBean.getFolloweds();
                        return packageData(followFansBeanList, userId, gsonFollowFansBeanBaseJson);
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

    /**
     * 重新封装服务器数据
     */
    private BaseJson<List<FollowFansBean>> packageData(List<FollowFansBean> followFansBeanList, long userId, BaseJson<GsonFollowFansBean> gsonFollowFansBeanBaseJson) {
        if (followFansBeanList != null) {
            for (FollowFansBean followFansBean : followFansBeanList) {
                // 关注主体是当前传入的userId
                followFansBean.setOriginUserId(userId);
                followFansBean.setOrigintargetUser(null);
            }
        }
        BaseJson<List<FollowFansBean>> listBaseJson = new BaseJson<List<FollowFansBean>>();
        listBaseJson.setCode(gsonFollowFansBeanBaseJson.getCode());
        listBaseJson.setMessage(gsonFollowFansBeanBaseJson.getMessage());
        listBaseJson.setStatus(gsonFollowFansBeanBaseJson.isStatus());
        listBaseJson.setData(followFansBeanList);
        return listBaseJson;
    }
}
