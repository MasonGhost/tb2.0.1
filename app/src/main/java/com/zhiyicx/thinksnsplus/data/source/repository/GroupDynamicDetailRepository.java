package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.GroupDynamicDetailContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/18
 * @contact email:648129313@qq.com
 */

public class GroupDynamicDetailRepository extends BaseChannelRepository implements GroupDynamicDetailContract.Repository{

    @Inject
    public GroupDynamicDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids) {
        return mUserInfoRepository.getUserFollowState(user_ids);
    }
}
