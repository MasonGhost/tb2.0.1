package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo.QA_ListInfoConstact;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:54
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoRepostory extends BaseQARepository implements QA_ListInfoConstact.Repository {

    @Inject
    public QA_ListInfoRepostory(ServiceManager serviceManager) {
        super(serviceManager);
    }
}
