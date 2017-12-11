package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.tbruyelle.rxpermissions.Permission;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @author Catherine
 * @describe 扫码页面
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */

public class ScanCodeFragment extends TSFragment<ScanCodeContract.Presenter> implements ScanCodeContract.View, QRCodeView.Delegate {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHOTO = 1;

    @BindView(R.id.zx_scan)
    ZXingView mZxScan;
    @BindView(R.id.iv_light)
    AppCompatImageView mIvLight;
    /**
     * 是否打开了灯
     */
    private boolean mIsOpenLight;
    private boolean getCameraPermissonSuccess;
    private ActionPopupWindow mActionPopupWindow;

    @Override
    protected void initView(View rootView) {
        mZxScan.setDelegate(this);
        setCenterTextColor(R.color.white);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        // 先查看相机权限
        mRxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 权限被允许
                        getCameraPermissonSuccess = true;
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 权限被拒绝
                        getCameraPermissonSuccess = false;
                    } else {
                        // 权限被彻底禁止
                        getCameraPermissonSuccess = false;
                        initPermissionPopUpWindow();
                        mActionPopupWindow.show();
                    }
                });
        if (!getCameraPermissonSuccess) {
            return;
        }
        startScan();
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.text_color_black;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.my_qr_code_title);
    }

    @OnClick({R.id.iv_light})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_light:
                // 打开或者关闭灯
                if (mIsOpenLight) {
                    mIvLight.setImageResource(R.mipmap.ico_torch);
                    mZxScan.closeFlashlight();
                } else {
                    mIvLight.setImageResource(R.mipmap.ico_torch_on);
                    mZxScan.openFlashlight();
                }
                mIsOpenLight = !mIsOpenLight;
                break;
            default:
        }
    }

    @Override
    public void onStop() {
        if (mZxScan != null) {
            mZxScan.stopCamera();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (mZxScan != null) {
            mZxScan.onDestroy();
        }
        super.onDestroy();
    }

    /**
     * 开始扫描
     */
    private void startScan() {
        mZxScan.startSpotAndShowRect();
    }

    /**
     * 开灯
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 关灯
     */
    private void cancleVibrate() {
        Vibrator vibrator = (Vibrator) getContext().getSystemService(VIBRATOR_SERVICE);
        vibrator.cancel();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_scan_code;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        mZxScan.stopSpotAndHiddenRect();
        vibrate();
        // 扫描到结果后直接跳转到个人中心  成功的结果应该是uid=xxx 所以直接取等号后面的内容
        // 至于为什么是uid=xxx ios定的 我也不知道
        try {
            Long uid = Long.parseLong(result.split("=")[1]);
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(uid);
            PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
            cancleVibrate();
            getActivity().finish();
        } catch (Exception e) {
            startScan();
            showSnackErrorMessage(getString(R.string.qr_scan_failed_alert));
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showSnackErrorMessage(getString(R.string.qr_scan_failed_alert));
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    /**
     * 添加用户完全禁止权限后的提示弹框
     */
    private void initPermissionPopUpWindow() {
        if (mActionPopupWindow != null) {
            return;
        }
        mActionPopupWindow = PermissionPopupWindow.builder()
                .permissionName(getString(com.zhiyicx.baseproject.R.string.camera_permission))
                .with(getActivity())
                .bottomStr(getString(com.zhiyicx.baseproject.R.string.cancel))
                .item1Str(getString(com.zhiyicx.baseproject.R.string.setting_permission_hint))
                .item2Str(getString(com.zhiyicx.baseproject.R.string.setting_permission))
                .item2ClickListener(() -> {
                    DeviceUtils.openAppDetail(getContext());
                    mActionPopupWindow.hide();
                })
                .bottomClickListener(() -> mActionPopupWindow.hide())
                .isFocus(true)
                .isOutsideTouch(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .build();
    }
}
