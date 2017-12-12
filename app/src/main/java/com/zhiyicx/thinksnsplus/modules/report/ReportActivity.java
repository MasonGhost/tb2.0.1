package com.zhiyicx.thinksnsplus.modules.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.feedback.DaggerFeedBackComponent;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackFragment;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackPresenter;
import com.zhiyicx.thinksnsplus.modules.feedback.FeedBackPresenterModule;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;

import static com.zhiyicx.thinksnsplus.modules.report.ReportFragment.BUNDLE_REPORT_RESOURCE_DATA;

/**
 * @Describe 聚合举报
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportActivity extends TSActivity<ReportPresenter, ReportFragment> {

    @Override
    protected ReportFragment getFragment() {
        return ReportFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerReportComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .reportPresenterModule(new ReportPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


    /**
     * @param context            not application context clink
     * @param reportResourceBean report data {@link ReportResourceBean}
     */
    public static void startReportActivity(Context context, ReportResourceBean reportResourceBean) {

        Intent intent = new Intent(context, ReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REPORT_RESOURCE_DATA, reportResourceBean);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }

}
