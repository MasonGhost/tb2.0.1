package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DigBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.rank.RankContract;

import java.util.List;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/11
 * @Contact master.jungle68@gmail.com
 */

public class RankRepository implements RankContract.Repository {

    public RankRepository(ServiceManager serviceManager) {
    }


    @Override
    public Observable<BaseJson<List<DigBean>>> getRankListFromNet(long userId, int page) {
        return null;
    }

    @Override
    public Observable<BaseJson> followUser(long followedId) {
        return null;
    }

    @Override
    public Observable<BaseJson> cancleFollowUser(long followedId) {
        return null;
    }
}
