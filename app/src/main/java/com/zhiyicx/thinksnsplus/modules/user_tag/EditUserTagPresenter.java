package com.zhiyicx.thinksnsplus.modules.user_tag;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/10
 * @Contact master.jungle68@gmail.com
 */

public class EditUserTagPresenter extends BasePresenter<EditUserTagContract.Repository, EditUserTagContract.View> implements EditUserTagContract.Presenter {


    @Inject
    public EditUserTagPresenter(EditUserTagContract.Repository repository, EditUserTagContract.View rootView) {
        super(repository, rootView);
    }

}
