package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class EditeQuestionDetailPresenterModule {
    EditeQuestionDetailContract.View mView;

    public EditeQuestionDetailPresenterModule(EditeQuestionDetailContract.View view) {
        mView = view;
    }

    @Provides
    EditeQuestionDetailContract.View provideEditeQuestionDetailView(){
        return mView;
    }
}
