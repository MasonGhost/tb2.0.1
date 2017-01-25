package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;

public class GuideFragment extends TSFragment {

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
