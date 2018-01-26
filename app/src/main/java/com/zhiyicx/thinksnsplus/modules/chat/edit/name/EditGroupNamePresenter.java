package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupNamePresenter extends AppBasePresenter<EditGroupNameContract.View>
        implements EditGroupNameContract.Presenter{

    @Inject
    public EditGroupNamePresenter(EditGroupNameContract.View rootView) {
        super(rootView);
    }
}
