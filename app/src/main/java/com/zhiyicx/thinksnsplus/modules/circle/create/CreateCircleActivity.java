package com.zhiyicx.thinksnsplus.modules.circle.create;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;

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
        Intent intent = new Intent(context, CreateCircleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CreateCircleFragment.CANUPDATE, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startUpdateActivity(Context context, CircleInfo CircleInfo) {
        Intent intent = new Intent(context, CreateCircleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CreateCircleFragment.CIRCLEINFO, CircleInfo);
        boolean isJoined = CircleInfo.getJoined() != null;
        boolean canNotUpdate = !isJoined || CircleMembers.MEMBER.equals(CircleInfo.getJoined().getRole());
        boolean isOwner = CircleInfo.getUser_id() == AppApplication.getMyUserIdWithdefault();
        boolean isManager = isJoined && CircleMembers.ADMINISTRATOR.equals(CircleInfo.getJoined().getRole());
        bundle.putBoolean(CreateCircleFragment.CANUPDATE, !canNotUpdate);
        bundle.putBoolean(CreateCircleFragment.PERMISSION_MANAGER, isManager);
        bundle.putBoolean(CreateCircleFragment.PERMISSION_OWNER, isOwner);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, CreateCircleFragment.REQUST_CODE_UPDATE);
            return;
        }
        context.startActivity(intent);
    }
}
