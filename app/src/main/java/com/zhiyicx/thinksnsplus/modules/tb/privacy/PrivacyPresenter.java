package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/03/01/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PrivacyPresenter extends AppBasePresenter<PrivacyContract.View> implements PrivacyContract.Presenter {

    @Inject
    public PrivacyPresenter(PrivacyContract.View rootView) {
        super(rootView);
    }
}
