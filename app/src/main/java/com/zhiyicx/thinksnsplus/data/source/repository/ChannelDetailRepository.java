package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ChannelClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailRepository extends BaseChannelRepository implements ChannelDetailContract.Repository {

    ChannelClient mChannelClient;

    @Inject
    public ChannelDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mChannelClient=serviceManager.getChannelClient();
    }

    @Override
    public Observable<GroupInfoBean> getGroupDetail(long group_id) {
        return mChannelClient.getGroupDetail(group_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
