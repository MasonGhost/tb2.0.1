package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackContract;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportPresenter extends AppBasePresenter<ReportContract.Repository, ReportContract.View>
        implements FeedBackContract.Presenter {

    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public ReportPresenter(ReportContract.Repository repository, ReportContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void submitFeedBack(String content, String contract) {


    }
}
