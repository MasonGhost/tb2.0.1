package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_InfoContainerActivity extends TSActivity {

    @Override
    protected QA_InfoContainerFragment getFragment() {
        return QA_InfoContainerFragment.getInstance();
    }

    @Override
    protected void componentInject() {

    }
}
