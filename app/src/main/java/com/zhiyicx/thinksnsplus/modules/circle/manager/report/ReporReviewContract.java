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

    interface Repository {
        /**
         * 圈子举报列表
         *
         * @param groupId 圈子id
         * @param after
         * @param limit
         * @param start    秒级时间戳，起始筛选时间
         * @param end      秒级时间戳，结束筛选时间
         * @param status  状态 默认全部，0-未处理 1-已处理 2-已驳回
         * @return
         */
        Observable<List<CircleReportListBean>> getCircleReportList(Long groupId, Integer status, Integer after,
                                                                   Integer limit,Long start, Long end);

        /**
         * 同意举报
         *
         * @param reportId 舉報的id
         * @return
         */
        Observable<BaseJsonV2> approvedCircleReport(Long reportId);

        /**
         * 拒绝举报
         *
         * @param reportId 舉報的id
         * @return
         */
        Observable<BaseJsonV2> refuseCircleReport(Long reportId);
    }
}
