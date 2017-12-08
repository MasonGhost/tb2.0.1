package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author Catherine
 * @describe 扫码页面
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeFragment extends TSFragment<ScanCodeContract.Presenter> implements ScanCodeContract.View{

    @Override
    public void setPresenter(ScanCodeContract.Presenter presenter) {

    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_scan_code;
    }
}
