package com.zhiyicx.thinksnsplus.modules.q$a;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA$Activity extends TSActivity {

    @Override
    protected QA$Fragment getFragment() {
        return QA$Fragment.getInstance();
    }

    @Override
    protected void componentInject() {

    }
}
