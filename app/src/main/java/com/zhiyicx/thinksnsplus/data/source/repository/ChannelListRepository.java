package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.channel.list.ChannelListContract;

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
    public Observable<BaseJson<List<ChannelSubscripBean>>> getMySubscribChannelList() {
        return getChannelList(ApiConfig.CHANNEL_TYPE_MY_SUBSCRIB_CHANNEL, AppApplication.getMyUserIdWithdefault());
    }


    @Override
    public Observable<BaseJson<List<ChannelSubscripBean>>> getAllChannelList() {
        return getChannelList(ApiConfig.CHANNEL_TYPE_ALL_CHANNEL, AppApplication.getMyUserIdWithdefault());
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
