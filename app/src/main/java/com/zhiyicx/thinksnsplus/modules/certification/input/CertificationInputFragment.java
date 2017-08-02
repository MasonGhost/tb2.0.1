package com.zhiyicx.thinksnsplus.modules.certification.input;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/2
 * @contact email:648129313@qq.com
 */

public class CertificationInputFragment extends TSFragment<CertificationInputContract.Presenter>
        implements CertificationInputContract.View{

    public CertificationInputFragment instance(Bundle bundle){
        CertificationInputFragment fragment = new CertificationInputFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_certification_input;
    }
}
