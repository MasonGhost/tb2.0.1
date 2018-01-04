package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class AnswerDetailsPresenterMudule {

    AnswerDetailsConstract.View mView;

    public AnswerDetailsPresenterMudule(AnswerDetailsConstract.View view) {
        mView = view;
    }

    @Provides
    AnswerDetailsConstract.View provideInfoDetailsView(){
        return mView;
    }

}
