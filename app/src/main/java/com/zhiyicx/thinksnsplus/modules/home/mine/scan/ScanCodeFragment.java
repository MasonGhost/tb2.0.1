package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * @author Catherine
 * @describe 扫码页面
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeFragment extends TSFragment<ScanCodeContract.Presenter> implements ScanCodeContract.View, QRCodeView.Delegate {

    @BindView(R.id.zx_scan)
    ZXingView mZxScan;

    @Override
    public void setPresenter(ScanCodeContract.Presenter presenter) {

    }

    @Override
    protected void initView(View rootView) {
        mZxScan.setDelegate(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mZxScan.startCamera();
        mZxScan.showScanRect();
        mZxScan.startSpotAndShowRect();
    }

    @Override
    public void onStop() {
        mZxScan.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mZxScan.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_scan_code;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mZxScan.stopCamera();
        mZxScan.stopSpot();
        vibrate();
        ToastUtils.showToast(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showToast("失败");
    }
}
