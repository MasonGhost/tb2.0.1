package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

import static com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type.SelectDynamicTypeFragment.SEND_OPTION;

public class SelectDynamicTypeActivity extends TSActivity {

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getFragment() {
        return SelectDynamicTypeFragment.getInstance(getIntent().getBundleExtra(SEND_OPTION));
    }

    @Override
    protected void componentInject() {

    }

}
