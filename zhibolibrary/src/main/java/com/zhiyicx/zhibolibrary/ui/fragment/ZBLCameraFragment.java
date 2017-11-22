package com.zhiyicx.zhibolibrary.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.view.CameraView;
import com.zhiyicx.zhibolibrary.ui.view.PublishView;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by jess on 16/6/18.
 */
public class ZBLCameraFragment extends ZBLBaseFragment implements CameraView, View.OnClickListener {
    protected View rootView;
    protected ImageView mCaptureIV;
    protected AutoRelativeLayout rlCameraClose;
    protected ImageButton mSwitchIb;
    protected ImageView mShootIV;
    protected ImageButton mAlbumBt;
    protected TextView mShootTV;
    protected ImageButton mOkBt;
    protected ImageButton mCancelBt;
    private PublishView mRootView;
    private File sdcardTempFile;
    private Bitmap mCaptureBitmap;


    public static ZBLCameraFragment newInstance() {
        return new ZBLCameraFragment();
    }


    @Override
    protected View initView() {
        rootView = UiUtils.inflate(R.layout.zb_fragment_camera);
        mCaptureIV = (ImageView) rootView.findViewById(R.id.iv_camera_capture);
        rlCameraClose = (AutoRelativeLayout) rootView.findViewById(R.id.rl_camera_close);
        rlCameraClose.setOnClickListener(ZBLCameraFragment.this);
        mSwitchIb = (ImageButton) rootView.findViewById(R.id.bt_camera_switch);
        mSwitchIb.setOnClickListener(ZBLCameraFragment.this);
        mShootIV = (ImageView) rootView.findViewById(R.id.iv_camera_shoot);
        mShootIV.setOnClickListener(ZBLCameraFragment.this);
        mAlbumBt = (ImageButton) rootView.findViewById(R.id.bt_camera_album);
        mAlbumBt.setOnClickListener(ZBLCameraFragment.this);
        mShootTV = (TextView) rootView.findViewById(R.id.tv_camera_shoot);
        mOkBt = (ImageButton) rootView.findViewById(R.id.bt_camera_ok);
        mOkBt.setOnClickListener(ZBLCameraFragment.this);
        mCancelBt = (ImageButton) rootView.findViewById(R.id.bt_camera_cancel);
        mCancelBt.setOnClickListener(ZBLCameraFragment.this);
        return rootView;
    }

    @Override
    protected void initData() {
        mRootView = (PublishView) getActivity();
        //用于存储照片
        sdcardTempFile = new File(DataHelper.getCacheFile(getActivity().getApplicationContext()), DataHelper.CAMERA_CAPTURE_TEMP_PICTURE);
    }


    @Override
    public void clearImage() {
        mCaptureIV.setBackgroundDrawable(new ColorDrawable(UiUtils.getColor(R.color.transparent)));
        if (mCaptureBitmap != null) {
            mCaptureBitmap.recycle();
            mCaptureBitmap = null;
        }

    }


    /**
     * 压缩bitmap到指定地址
     *
     * @param file
     * @param resizeBmp
     */
    public boolean compressBitmap(File file, Bitmap resizeBmp) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return resizeBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public ZBLBaseFragment getFragment() {
        return this;
    }

    /**
     * 传入截帧的bitmap
     *
     * @param bm
     */
    @Override
    public void setCaptureBitmap(Bitmap bm) {
        this.mCaptureBitmap = bm;
        if (mCaptureIV != null) {
            mSwitchIb.setVisibility(View.GONE);
            mCaptureIV.setBackgroundDrawable(new BitmapDrawable(bm));
            showAction();//显示是否保存照片的组件
        }

    }

    /**
     * 显示用去选择是否保存照片的组件
     */
    @Override
    public void showAction() {
        mShootIV.setEnabled(false);
        mShootTV.setEnabled(false);
        mOkBt.setVisibility(View.VISIBLE);
        mCancelBt.setVisibility(View.VISIBLE);
        mAlbumBt.setVisibility(View.GONE);
    }

    /**
     * 隐藏用去选择是否保存照片的组件
     */
    @Override
    public void hideAction() {
        mShootIV.setEnabled(true);
        mShootTV.setEnabled(true);
        mOkBt.setVisibility(View.GONE);
        mCancelBt.setVisibility(View.GONE);
        mAlbumBt.setVisibility(View.VISIBLE);
        mSwitchIb.setVisibility(View.VISIBLE);
    }

    @Override
    public void beginCrop() {
        if (mCaptureBitmap == null || sdcardTempFile == null) {
            showMessage("保存图片失败 ~");
            return;
        }
        if (compressBitmap(sdcardTempFile, mCaptureBitmap)) {//压缩到本地
            mRootView.beginCrop(Uri.fromFile(sdcardTempFile));//让activity发送到剪切页面
        }
        else {
            showMessage("保存图片失败 ~");
        }

    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        mRootView.launchAlbum();//打开相册
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_camera_close) {
            mRootView.closeCameraFragment();//关闭本页面
        }
        else if (view.getId() == R.id.bt_camera_switch) {
            mRootView.switchCamera();
        }
        else if (view.getId() == R.id.iv_camera_shoot) {
            mRootView.captureFrame();//拍照
        }
        else if (view.getId() == R.id.bt_camera_album) {
            openAlbum();
        }
        else if (view.getId() == R.id.bt_camera_ok) {
            beginCrop();//开始剪切
            hideAction();//隐藏是否保存照片的组件
        }
        else if (view.getId() == R.id.bt_camera_cancel) {
            clearImage();
            hideAction();//隐藏是否保存照片的组件
        }
    }
}

