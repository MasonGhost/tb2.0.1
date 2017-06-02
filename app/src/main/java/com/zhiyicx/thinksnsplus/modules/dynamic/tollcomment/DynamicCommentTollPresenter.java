package com.zhiyicx.thinksnsplus.modules.dynamic.tollcomment;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicCommentTollPresenter extends AppBasePresenter<DynamicCommentTollContract.Repository,DynamicCommentTollContract.View>
        implements DynamicCommentTollContract.Presenter {

    @Inject
    public DynamicCommentTollPresenter(DynamicCommentTollContract.Repository repository, DynamicCommentTollContract.View rootView) {
        super(repository, rootView);
    }
}
