package com.zhiyicx.thinksnsplus.modules.circle.mine.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyQuestionFragment;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/11/14
 * @Contact master.jungle68@gmail.com
 */
public class MyCircleContainerActivity extends TSActivity{

    public static final String BUNDLE_MY_QUESTION_TYPE = "bundle_my_question_type";

    @Override
    protected Fragment getFragment() {
        return MyCircleContainerFragment.instance();
    }

    @Override
    protected void componentInject() {

    }
}
