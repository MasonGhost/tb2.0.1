package com.zhiyicx.thinksnsplus.modules.circle.manager.permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningPresenter;

public class PermissionActivity extends TSActivity<CircleEarningPresenter, PermissionFragment> {

    @Override
    protected PermissionFragment getFragment() {
        return PermissionFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }

    public static void startActivity(Context context, long id, String permissions) {
        Intent intent = new Intent(context, PermissionActivity.class);
        Bundle bundle = new Bundle();
        int permisstion;
        if (permissions.contains(CircleMembers.MEMBER)) {
            permisstion = PermissionFragment.PERMISSION_ALL;
        } else {
            if (permissions.contains(CircleMembers.ADMINISTRATOR)) {
                permisstion = PermissionFragment.PERMISSION_MANAGER;
            } else {
                permisstion = PermissionFragment.PERMISSION_OWNER;
            }
        }
        bundle.putLong(PermissionFragment.CIRCLEID, id);
        bundle.putInt(PermissionFragment.PERMISSION, permisstion);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, PermissionFragment.PERMISSION_REQUEST);
            return;
        }
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
