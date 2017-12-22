package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ReportReviewPresenter extends AppBasePresenter<ReporReviewContract.Repository, ReporReviewContract.View>
        implements ReporReviewContract.Presenter {

    @Inject
    public ReportReviewPresenter(ReporReviewContract.Repository repository, ReporReviewContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getCircleReportList(mRootView.getSourceId(), mRootView.getStatus(), maxId.intValue()
                , TSListFragment.DEFAULT_ONE_PAGE_SIZE,mRootView.getStartTime(),mRootView.getEndTime())
                .subscribe(new BaseSubscribeForV2<List<CircleReportListBean>>() {
                    @Override
                    protected void onSuccess(List<CircleReportListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public void approvedCircleReport(Long reportId) {
        mRepository.approvedCircleReport(reportId).subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
            @Override
            protected void onSuccess(BaseJsonV2 data) {
                mRootView.refreshData();
            }
        });
    }

    @Override
    public void refuseCircleReport(Long reportId) {
        mRepository.refuseCircleReport(reportId).subscribe(new BaseSubscribeForV2<BaseJsonV2>() {
            @Override
            protected void onSuccess(BaseJsonV2 data) {
                mRootView.refreshData();
            }
        });
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleReportListBean> data, boolean isLoadMore) {
        return false;
    }
}
