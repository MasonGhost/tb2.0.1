package com.zhiyicx.thinksnsplus.modules.circle.manager.earning;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;

public class CircleEarningActivity extends TSActivity<CircleEarningPresenter, CircleEarningFragment> {

    @Override
    protected CircleEarningFragment getFragment() {
        return CircleEarningFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerCircleEarningComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleEarningPresenterModule(new CircleEarningPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startActivity(Context context, CircleInfo CircleInfo) {
        Intent intent = new Intent(context, CircleEarningActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CircleEarningFragment.DATA, CircleInfo);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
