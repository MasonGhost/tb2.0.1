package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;

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
