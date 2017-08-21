package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.q_a.search.list.IHistoryCententClickListener;

public class QASearchContainerActivity extends TSActivity<QASearchContainerPresenter, QASearchContainerFragment> implements IHistoryCententClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {
        DaggerQASearchContainerComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qASearchContainerPresenterModule(new QASearchContainerPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected QASearchContainerFragment getFragment() {
        return QASearchContainerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onContentClick(String str) {
        mContanierFragment.onHistoryContentUpdate(str);
    }
}
