package com.zhiyicx.thinksnsplus.modules.q_a.mine.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @author Catherine
 * @describe 我的问答
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyQuestionActivity extends TSActivity {

    public static final String BUNDLE_MY_QUESTION_TYPE = "bundle_my_question_type";

    @Override
    protected Fragment getFragment() {
        return MyQuestionFragment.instance();
    }

    @Override
    protected void componentInject() {

    }
}
