package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;

public class EarningListActivity extends TSActivity<EarningListPresenter, EarningListFragment> {

    @Override
    protected EarningListFragment getFragment() {
        return EarningListFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerEarningListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .earningListPresenterModule(new EarningListPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startActivity(Context context, CircleInfo CircleInfo, int type) {
        Intent intent = new Intent(context, EarningListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EarningListFragment.CIRCLE, CircleInfo);
        bundle.putInt(EarningListFragment.TYPE, type);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
