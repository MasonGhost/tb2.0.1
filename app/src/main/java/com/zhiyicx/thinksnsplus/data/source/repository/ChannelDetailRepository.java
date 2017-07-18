package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailRepository extends BaseChannelRepository implements ChannelDetailContract.Repository {
    @Inject
    public ChannelDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
