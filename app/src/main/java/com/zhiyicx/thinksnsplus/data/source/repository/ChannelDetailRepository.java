package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailContract;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailRepository extends BaseChannelRepository implements ChannelDetailContract.Repository {
    @Inject
    public ChannelDetailRepository(ServiceManager serviceManager, Application context) {
        super(serviceManager, context);
    }
}
