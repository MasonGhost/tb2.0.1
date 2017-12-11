package com.zhiyicx.thinksnsplus.modules.report;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ReportRepository;

import javax.inject.Inject;

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


    }
}
