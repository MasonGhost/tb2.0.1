package com.zhiyicx.thinksnsplus.modules.circle.earning;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/12/12/11:33
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleEarningPresenter extends AppBasePresenter<CircleEarningContract.Repository, CircleEarningContract.View>
        implements CircleEarningContract.Presenter {

    @Inject
    public CircleEarningPresenter(CircleEarningContract.Repository repository, CircleEarningContract.View rootView) {
        super(repository, rootView);
    }
}
