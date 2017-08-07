package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoPublishRepository implements PublishInfoContract.Repository {
    private InfoMainClient mInfoMainClient;

    @Inject
    public InfoPublishRepository(ServiceManager serviceManager) {
        mInfoMainClient=serviceManager.getInfoMainClient();
    }
}
