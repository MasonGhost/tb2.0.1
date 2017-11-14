package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.list.ChannelListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelListRepository extends BaseChannelRepository implements ChannelListContract.Repository {

    @Inject
    public ChannelListRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }


    @Override
    public Observable<List<GroupInfoBean>> getAllGroupList(long max_id) {
        return getGroupList(0, max_id);
    }

    @Override
    public Observable<List<GroupInfoBean>> getUserJoinedGroupList(long max_id) {
        return getGroupList(1, max_id);
    }
}
