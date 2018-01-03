package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class MarkdownPresenterModule {

    MarkdownContract.View mView;

    public MarkdownPresenterModule(MarkdownContract.View view) {
        mView = view;
    }

    @Provides
    MarkdownContract.View providesMarkdownContractView() {
        return mView;
    }

}
