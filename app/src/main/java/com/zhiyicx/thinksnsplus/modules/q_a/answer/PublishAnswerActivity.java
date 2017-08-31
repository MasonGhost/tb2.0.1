package com.zhiyicx.thinksnsplus.modules.q_a.answer;

import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentActivity;

/**
 * @Author Jliuer
 * @Date 2017/08/15/16:21
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishAnswerActivity extends PublishContentActivity {

    @Override
    protected PublishAnswerFragment getFragment() {
        return PublishAnswerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
