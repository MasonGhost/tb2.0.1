package com.zhiyicx.thinksnsplus.modules.chat.edit.name;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Catherine on 2018/1/22.
 */
@Module
public class EditGroupNamePresenterModule {

    private EditGroupNameContract.View mView;

    public EditGroupNamePresenterModule(EditGroupNameContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public EditGroupNameContract.View provideEditGroupNameContractView(){
        return mView;
    }
}
