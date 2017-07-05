package com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment;

import com.zhiyicx.thinksnsplus.data.source.repository.DynamicCommentTollRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.DynamicCommentTopRepository;

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

    @Provides
    DynamicCommentTopContract.Repository provideDynamicCommentTollContractRepository(DynamicCommentTopRepository commentTopRepository) {
        return commentTopRepository;
    }
}
