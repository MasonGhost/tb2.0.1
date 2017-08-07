package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */
public class InfoPublishAddInfoRepository implements AddInfoContract.Repository {
    private InfoMainClient mInfoMainClient;

    @Inject
    public InfoPublishAddInfoRepository(ServiceManager serviceManager) {
        mInfoMainClient=serviceManager.getInfoMainClient();
    }
}
