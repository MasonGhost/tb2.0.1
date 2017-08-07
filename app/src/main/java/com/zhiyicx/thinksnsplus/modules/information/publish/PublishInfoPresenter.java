package com.zhiyicx.thinksnsplus.modules.information.publish;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoPresenter extends AppBasePresenter<PublishInfoContract.Repository,PublishInfoContract.View>
        implements PublishInfoContract.Presenter{

    @Inject
    public PublishInfoPresenter(PublishInfoContract.Repository repository, PublishInfoContract.View rootView) {
        super(repository, rootView);
    }
}
