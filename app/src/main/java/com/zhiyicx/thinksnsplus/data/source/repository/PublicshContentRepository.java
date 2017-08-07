package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentConstact;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublicshContentRepository implements PublishContentConstact.Repository {

    @Inject
    public PublicshContentRepository(ServiceManager serviceManager) {
    }
}
