package com.zhiyicx.thinksnsplus.modules.tb.privacy;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2018/02/28/18:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PrivacyFragment extends TSFragment<PrivacyContract.Presenter> implements PrivacyContract.View {

    @Override
    protected String setCenterTitle() {
        return getString(R.string.setting_rank);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_privacy;
    }
}
