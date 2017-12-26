package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface ReporReviewContract {
    interface View extends ITSListView<CircleReportListBean, Presenter> {
        Long getSourceId();

        Integer getStatus();

        Long getStartTime();

        Long getEndTime();
    }

    interface Presenter extends ITSListPresenter<CircleReportListBean> {
        void approvedCircleReport(Long reportId);

        void refuseCircleReport(Long reportId);
    }

}
