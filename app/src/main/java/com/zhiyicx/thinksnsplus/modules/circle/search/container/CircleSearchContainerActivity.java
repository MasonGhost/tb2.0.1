package com.zhiyicx.thinksnsplus.modules.circle.search.container;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.DaggerQASearchContainerComponent;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerPresenter;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerPresenterModule;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.IHistoryCententClickListener;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;

import static com.zhiyicx.thinksnsplus.modules.report.ReportFragment.BUNDLE_REPORT_RESOURCE_DATA;

/**
 * @Describe 圈子搜索容器
 * @Author Jungle68
 * @Date 2017/12/7
 * @Contact master.jungle68@gmail.com
 */
public class CircleSearchContainerActivity extends TSActivity<CircleSearchContainerPresenter, CircleSearchContainerFragment> implements
        IHistoryCententClickListener {

    @Override
    protected void componentInject() {
        DaggerCircleSearchContainerComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleSearchContainerPresenterModule(new CircleSearchContainerPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected CircleSearchContainerFragment getFragment() {
        return CircleSearchContainerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onContentClick(String str) {
        mContanierFragment.onHistoryContentUpdate(str);
    }


    /**
     * @param context  not application context clink
     * @param pageType type {@link CircleSearchContainerViewPagerFragment.PAGE_CIRCLE,CircleSearchContainerViewPagerFragment.PAGE_CIRCLE_POST}
     */
    public static void startCircelSearchActivity(Context context, int pageType) {

        Intent intent = new Intent(context, CircleSearchContainerActivity.class);
        Bundle bundle = new Bundle();
        if (pageType != CircleSearchContainerViewPagerFragment.PAGE_CIRCLE && pageType != CircleSearchContainerViewPagerFragment.PAGE_CIRCLE_POST) {
            pageType = CircleSearchContainerViewPagerFragment.PAGE_CIRCLE;
        }
        bundle.putInt(CircleSearchContainerViewPagerFragment.BUNDLE_PAGE, pageType);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }

}
