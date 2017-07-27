package com.zhiyicx.thinksnsplus.modules.user_tag;

import com.zhiyicx.thinksnsplus.data.source.repository.EditUserTagRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@Module
public class EditUserTagPresenterModule {
    private final EditUserTagContract.View mView;

    public EditUserTagPresenterModule(EditUserTagContract.View view) {
        mView = view;
    }

    @Provides
    EditUserTagContract.View provideEditUserTagContractContractView() {
        return mView;
    }

    @Provides
    EditUserTagContract.Repository provideEditUserTagContractRepository(EditUserTagRepository repository) {
        return repository;
    }
}
