package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class DynamicCommentTopPresenterModule {

    DynamicCommentTopContract.View mView;

    public DynamicCommentTopPresenterModule(DynamicCommentTopContract.View view) {
        mView = view;
    }

    @Provides
    DynamicCommentTopContract.View provideDynamicCommentTollContractView() {
        return mView;
    }

}
