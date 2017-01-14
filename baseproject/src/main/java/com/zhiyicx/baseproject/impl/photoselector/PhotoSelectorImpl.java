package com.zhiyicx.baseproject.impl.photoselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yalantis.ucrop.UCrop;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.ToastUtils;


import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static android.app.Activity.RESULT_OK;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public class PhotoSelectorImpl implements IPhotoSelector<ImageBean> {

    private static final int CAMERA_PHOTO_CODE = 8888;

    private IPhotoBackListener mTIPhotoBackListener;
    private Fragment mFragment;
    private Context mContext;
    private File takePhotoFolder;
    private Uri mTakePhotoUri;

    public PhotoSelectorImpl(IPhotoBackListener iPhotoBackListener, Fragment mFragment) {
        takePhotoFolder = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "TSPlusPhotoFolder/");
        mTIPhotoBackListener = iPhotoBackListener;
        this.mFragment = mFragment;
        this.mContext = mFragment.getContext();
    }

    @Override
    public void getPhotoListFromSelector(int maxCount) {
        // 选择相册
        PhotoPicker.builder()
                .setPreviewEnabled(true) // 是否可预览
                .setGridColumnCount(3)      // 每行的图片数量
                .setPhotoCount(maxCount)    //  每次能够选择的最
                .setShowCamera(true)        // 是否需要展示相机
                .start(mContext, mFragment);
    }

    @Override
    public void getPhotoFromCamera() {

        boolean suc = FileUtils.createOrExistsDir(takePhotoFolder);
        File toFile = new File(takePhotoFolder, "IMG" + format() + ".jpg");
        if (suc) {
            mTakePhotoUri = Uri.fromFile(toFile);
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
            mFragment.startActivityForResult(captureIntent, CAMERA_PHOTO_CODE);
        } else {
            // 无法启动相机
            mTIPhotoBackListener.getPhotoFailure("cannot start camera");
        }
    }

    @Override
    public void startToCraft(String imgPath) {
        String destinationFileName = "SampleCropImage.jpg";
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(imgPath)), Uri.fromFile(new File(mFragment.getActivity().getCacheDir(), destinationFileName)));
        uCrop.withAspectRatio(1, 1);//方形
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);    // 图片质量压缩
        options.setCircleDimmedLayer(false); // 是否裁剪圆形
        options.setHideBottomControls(true);// 是否隐藏底部的控制面板
        options.setCropFrameColor(Color.WHITE);// 设置内矩形边框线条颜色
        options.setShowCropGrid(false);// 是否展示内矩形的分割线
        options.setDimmedLayerColor(Color.argb(0xbb, 0xff, 0xff, 0xff));// 设置蒙层的颜色
        options.setRootViewBackgroundColor(Color.WHITE);// 设置图片背景颜色
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.themeColor));
        uCrop.withOptions(options);
        uCrop.start(mContext, mFragment);
    }

    @Override
    public boolean isNeededCraft() {
        return true;
    }

    /**
     * 集中处理返回结果，一般在Fragment或者activity的onActivityResult方法中调用它
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // 从本地相册选择图片
            if ((requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
                List<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                // 是否需要剪裁，不需要就直接返回结果
                if (isNeededCraft()) {
                    startToCraft(photos.get(0));
                } else {
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(photos.get(0));
                    imageBeanList.add(imageBean);
                    mTIPhotoBackListener.getPhotoSuccess(imageBeanList);
                }
            }
            // 从相机中获取照片
            if (requestCode == CAMERA_PHOTO_CODE && mTakePhotoUri != null) {
                String path = mTakePhotoUri.getPath();
                if (new File(path).exists()) {
                    // 是否需要剪裁，不需要就直接返回结果
                    if (isNeededCraft()) {
                        startToCraft(path);
                    } else {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImgUrl(path);
                        List<ImageBean> imageBeanList = new ArrayList<>();
                        imageBeanList.add(imageBean);
                        mTIPhotoBackListener.getPhotoSuccess(imageBeanList);
                    }
                } else {
                    mTIPhotoBackListener.getPhotoFailure("cannot get photo");
                }
            }
            // 裁剪图片正确
            if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    List<ImageBean> photos = new ArrayList<>(1);
                    ImageBean imageBean = packageCropResult(data);
                    photos.add(imageBean);// 获取裁剪图片的路径
                    mTIPhotoBackListener.getPhotoSuccess(photos);
                } else {
                    // 无法裁剪
                    mTIPhotoBackListener.getPhotoFailure("cannot crop");
                }
            }
            // 裁剪图片错误
            if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
                if (cropError != null) {
                    ToastUtils.showToast(cropError.getMessage());
                } else {
                    ToastUtils.showToast("Unexpected error");
                }
                mTIPhotoBackListener.getPhotoFailure(cropError.getMessage());
            }
        }
    }

    /**
     * 获取到最终图片后的回调接口
     */
    public interface IPhotoBackListener {
        void getPhotoSuccess(List<ImageBean> photoList);

        void getPhotoFailure(String errorMsg);
    }

    /**
     * 封装裁剪的图片信息
     *
     * @param data
     * @return
     */
    private ImageBean packageCropResult(Intent data) {
        Uri resultUri = UCrop.getOutput(data);
        int height = UCrop.getOutputImageHeight(data);
        int width = UCrop.getOutputImageWidth(data);
        String imgPath = resultUri.getPath();
        ImageBean imageBean = new ImageBean();
        imageBean.setImgHeight(height);
        imageBean.setImgWidth(width);
        imageBean.setImgUrl(imgPath);
        return imageBean;
    }

    /**
     * 格式化照片时间，作为文件名称
     *
     * @return
     */
    private String format() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }

}