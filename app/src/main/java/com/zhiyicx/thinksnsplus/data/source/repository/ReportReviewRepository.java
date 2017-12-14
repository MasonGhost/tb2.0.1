package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.circle.manager.report.ReporReviewContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:51
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ReportReviewRepository implements ReporReviewContract.Repository {

    CircleClient mCircleClient;

    @Inject
    public ReportReviewRepository(ServiceManager serviceManager) {
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<List<CircleReportListBean>> getCircleReportList(Long groupId, Integer status, Integer after, Integer limit) {
        return mCircleClient.getCircleReportList(groupId,status,after,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedCircleReport(Long reportId) {
        return mCircleClient.approvedCircleReport(reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseCircleReport(Long reportId) {
        return mCircleClient.refuseCircleReport(reportId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
