package com.zhiyicx.thinksnsplus.modules.q_a.answer;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.DaggerEditeQuestionDetailComponent;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailPresenterModule;

/**
 * @Author Jliuer
 * @Date 2018/01/22/11:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EditeAnswerDetailActivity extends EditeQuestionDetailActivity {

    @Override
    protected EditeQuestionDetailFragment getYourFragment() {
        return EditeAnswerDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEditeQuestionDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editeQuestionDetailPresenterModule(new EditeQuestionDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }


}
