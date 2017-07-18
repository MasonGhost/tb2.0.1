package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/02/17:24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class FeedBackRepository implements FeedBackContract.Repository {

    @Inject
    public FeedBackRepository(ServiceManager serviceManager) {
    }
}
