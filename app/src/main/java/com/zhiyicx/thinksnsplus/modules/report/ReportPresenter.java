package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ReportResultBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ReportRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    UserInfoRepository mUserInfoRepository;

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
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));

                    }
                });
        addSubscrebe(subscribe);

    }

    /**
     *
     * @param userId 需要获取的用户 id
     */
    @Override
    public void getUserInfoById(Long userId) {
        Subscription subscribe = mUserInfoRepository.getLocalUserInfoBeforeNet(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mRootView.getUserInfoResult(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.getUserInfoResult(null);

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.getUserInfoResult(null);

                    }
                });
        addSubscrebe(subscribe);
    }
}
