package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
@Module
public class DynamicCommentListPresenterModule {
    private DynamicCommentListContract.View mView;

    public DynamicCommentListPresenterModule(DynamicCommentListContract.View view) {
        mView = view;
    }

    @Provides
    public DynamicCommentListContract.View provideContractView() {
        return mView;
    }

}
