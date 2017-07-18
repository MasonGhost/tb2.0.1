package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.thinksnsplus.data.source.repository.DynamicCommentTollRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class DynamicCommentTollPresenterModule {

    DynamicCommentTollContract.View mView;

    public DynamicCommentTollPresenterModule(DynamicCommentTollContract.View view) {
        mView = view;
    }

    @Provides
    DynamicCommentTollContract.View provideDynamicCommentTollContractView() {
        return mView;
    }

    @Provides
    DynamicCommentTollContract.Repository provideDynamicCommentTollContractRepository(DynamicCommentTollRepository commentTollRepository) {
        return commentTollRepository;
    }
}
