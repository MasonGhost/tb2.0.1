package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCirclePresenter extends AppBasePresenter<CreateCircleContract.Repository,CreateCircleContract.View>
        implements CreateCircleContract.Presenter {

    @Inject
    public CreateCirclePresenter(CreateCircleContract.Repository repository, CreateCircleContract.View rootView) {
        super(repository, rootView);
    }
}
