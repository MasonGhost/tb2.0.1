package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment.DynamicCommentTollContract;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollRepository implements DynamicCommentTollContract.Repository {

    @Inject
    public DynamicCommentTollRepository(ServiceManager serviceManager) {
    }
}
