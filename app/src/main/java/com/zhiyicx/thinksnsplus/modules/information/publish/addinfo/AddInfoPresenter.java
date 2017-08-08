package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class AddInfoPresenter extends AppBasePresenter<AddInfoContract.Repository,AddInfoContract.View>
        implements AddInfoContract.Presenter{

    @Inject
    public AddInfoPresenter(AddInfoContract.Repository repository, AddInfoContract.View rootView) {
        super(repository, rootView);
    }
}
