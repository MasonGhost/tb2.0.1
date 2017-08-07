package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishContentPresenter extends AppBasePresenter<PublishContentConstact.Repository,PublishContentConstact.View>
        implements PublishContentConstact.Presenter {

    @Inject
    public PublishContentPresenter(PublishContentConstact.Repository repository, PublishContentConstact.View rootView) {
        super(repository, rootView);
    }
}
