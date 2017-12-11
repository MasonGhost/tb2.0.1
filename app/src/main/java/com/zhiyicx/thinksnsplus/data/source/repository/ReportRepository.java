package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IReportRepository;
import com.zhiyicx.thinksnsplus.modules.report.ReportContract;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportRepository implements IReportRepository ,ReportContract.Repository{

    @Inject
    public ReportRepository(ServiceManager serviceManager){

    }
}
