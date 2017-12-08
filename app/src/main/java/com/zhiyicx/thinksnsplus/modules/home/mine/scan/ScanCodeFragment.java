package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.mine.previewuser.UserPreViewActivity;

import butterknife.BindView;
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
        if (mZxScan != null){
            mZxScan.stopCamera();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mZxScan != null){
            mZxScan.onDestroy();
        }
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
        // 扫描到结果后直接跳转到预览页面
        Bundle bundle = new Bundle();
        bundle.putString(UserPreViewActivity.BUNDLE_USER_ID, result);
        Intent intent = new Intent(getContext(), UserPreViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showSnackErrorMessage(getString(R.string.qr_scan_failed_alert));
    }
}
