package com.zhiyicx.thinksnsplus.modules.circle.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;

import javax.inject.Inject;

public class CreateCircleActivity extends TSActivity<CreateCirclePresenter, CreateCircleFragment> {

    @Override
    protected CreateCircleFragment getFragment() {
        return CreateCircleFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerCreateCircleComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .createCirclePresenterModule(new CreateCirclePresenterModule(mContanierFragment))
                .build().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    public static void startCreateActivity(Context context) {
        context.startActivity(new Intent(context, CreateCircleActivity.class));
    }

    public static void startUpdateActivity(Context context, CircleInfoDetail circleInfoDetail) {
        Intent intent = new Intent(context, CreateCircleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CreateCircleFragment.CIRCLEINFO, circleInfoDetail);
        boolean canUpdate = circleInfoDetail.getUser_id() == AppApplication.getMyUserIdWithdefault();
        bundle.putBoolean(CreateCircleFragment.CANUPDATE, canUpdate);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
