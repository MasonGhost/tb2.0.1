package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupNamePresenter extends AppBasePresenter<EditGroupNameContract.Repository, EditGroupNameContract.View>
        implements EditGroupNameContract.Presenter{

    @Inject
    public EditGroupNamePresenter(EditGroupNameContract.Repository repository, EditGroupNameContract.View rootView) {
        super(repository, rootView);
    }
}
