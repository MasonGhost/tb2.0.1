package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

public class SelectDynamicTypeActivity extends TSActivity {

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        return new SelectDynamicTypeFragment();
    }

    @Override
    protected void componentInject() {

    }

}
