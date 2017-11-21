package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/21/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AllCircleContainerPresenter extends AppBasePresenter<AllCircleContainerContract.Repository,AllCircleContainerContract.View>
        implements AllCircleContainerContract.Presenter {

    @Inject
    public AllCircleContainerPresenter(AllCircleContainerContract.Repository repository, AllCircleContainerContract.View rootView) {
        super(repository, rootView);
    }
}
