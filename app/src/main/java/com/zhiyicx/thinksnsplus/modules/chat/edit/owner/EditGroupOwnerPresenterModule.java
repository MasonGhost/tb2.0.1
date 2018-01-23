package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import com.zhiyicx.thinksnsplus.data.source.repository.EditGroupOwnerRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Catherine on 2018/1/22.
 */
@Module
public class EditGroupOwnerPresenterModule {

    private EditGroupOwnerContract.View mView;

    public EditGroupOwnerPresenterModule(EditGroupOwnerContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public EditGroupOwnerContract.View provideEditGroupOwnerContractView(){
        return mView;
    }

    @Provides
    public EditGroupOwnerContract.Repository provideEditGroupOwnerContractRepository(EditGroupOwnerRepository repository){
        return repository;
    }
}
