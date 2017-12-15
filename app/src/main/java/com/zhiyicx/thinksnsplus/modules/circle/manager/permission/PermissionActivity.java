package com.zhiyicx.thinksnsplus.modules.circle.manager.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

import java.util.List;

public class PermissionActivity extends TSActivity {

    @Override
    protected PermissionFragment getFragment() {
        return PermissionFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }

    public static void startActivity(Context context,long id,List<String> permission){
        Intent intent=new Intent(context,PermissionActivity.class);
        Bundle bundle=new Bundle();
        bundle.putLong(PermissionFragment.CIRCLEID,id);

    }
}
