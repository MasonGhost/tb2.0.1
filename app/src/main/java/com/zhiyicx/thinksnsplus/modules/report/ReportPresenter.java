package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ReportRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportPresenter extends AppBasePresenter<ReportContract.Repository, ReportContract.View>
        implements ReportContract.Presenter {

    @Inject
    ReportRepository mReportRepository;

    @Inject
    public ReportPresenter(ReportContract.Repository repository, ReportContract.View rootView) {
        super(repository, rootView);
    }


    /**
     * 举报
     *
     * @param inputContent       举报的内容
     * @param reportResourceBean 举报相关的资源
     */
    @Override
    public void report(String inputContent, ReportResourceBean reportResourceBean) {
        mRootView.showLoading();
        Observable<ReportResultBean> observable;
        switch (reportResourceBean.getType()) {
            case INFO:
                observable = mReportRepository.reportInfo(reportResourceBean.getId(), inputContent);
                break;
            case DYNAMIC:
                observable = mReportRepository.reportDynamic(reportResourceBean.getId(), inputContent);

                break;
            case QA:
                observable = mReportRepository.reportQA(reportResourceBean.getId(), inputContent);

                break;

            case QA_ANSWER:
                observable = mReportRepository.reportQAAnswer(reportResourceBean.getId(), inputContent);

                break;

            case CIRCLE:
                observable = mReportRepository.reportCircle(reportResourceBean.getId(), inputContent);

                break;
            case CIRCLE_POST:
                observable = mReportRepository.reportCirclePost(reportResourceBean.getId(), inputContent);

                break;
            case COMMENT:
                observable = mReportRepository.reportComment(reportResourceBean.getId(), inputContent);

                break;
            default:
                observable = mReportRepository.reportComment(reportResourceBean.getId(), inputContent);

        }

        Subscription subscribe = observable
                .doAfterTerminate(() -> mRootView.hideLoading())
                .subscribe(new BaseSubscribeForV2<ReportResultBean>() {
                    @Override
                    protected void onSuccess(ReportResultBean data) {
                        mRootView.reportSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                    }
                });
        addSubscrebe(subscribe);

    }
}
