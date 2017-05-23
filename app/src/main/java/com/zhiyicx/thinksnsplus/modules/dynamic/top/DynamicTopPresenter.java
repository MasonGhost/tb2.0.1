package com.zhiyicx.thinksnsplus.modules.dynamic.top;


import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicTopPresenter extends AppBasePresenter<DynamicTopContract.Repository,DynamicTopContract.View>
        implements DynamicTopContract.Presenter {

    @Inject
    public DynamicTopPresenter(DynamicTopContract.Repository repository, DynamicTopContract.View rootView) {
        super(repository, rootView);
    }
}
