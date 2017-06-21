package com.zhiyicx.baseproject.impl.photoselector;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.SparseArray;

import com.tbruyelle.rxpermissions.Permission;
import com.yalantis.ucrop.UCrop;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PermissionPopupWindow;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.iwf.photopicker.PhotoPicker;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public class PhotoSelectorImpl implements IPhotoSelector<ImageBean> {

    public static final int PHOTO_CLUMS_SIZE = 4;
    public static final int MAX_DEFAULT_COUNT = 9;
    // 添加几种默认的裁剪框形状
    public static final int NO_CRAFT = 0;// 不剪切
    public static final int SHAPE_SQUARE = 1;// 正方形
    public static final int SHAPE_RCTANGLE = 2;// 长方形，宽度占满
    private static final int SQUARE_LEFT_MARGIN = 36;// 裁剪框距离屏幕左边缘的距离；右边也是一样的
    private static final int CAMERA_PHOTO_CODE = 8888;

    public static final String TOLL_TYPE = "toll_type";
    public static final String TOLL = "toll";
    public static final String TOLL_MONEY = "toll_money";

    private IPhotoBackListener mTIPhotoBackListener;
    private BaseFragment mFragment;
    private Context mContext;
    private File takePhotoFolder;// 拍照后照片的存放目录
    private String mTakePhotoPath;// 拍照后照片的图片路径
    private int mCropShape;
    private int maxCount;// 可选的最大图片数量
    private ArrayList<ImageBean> photosList;// 存储已选择图片
    private ActionPopupWindow mActionPopupWindow;
    private ArrayList<ImageBean> mTolls = new ArrayList<>();

    public PhotoSelectorImpl(IPhotoBackListener iPhotoBackListener, BaseFragment mFragment, int
            cropShape) {
        takePhotoFolder = new File(Environment.getExternalStorageDirectory(), PathConfig
                .CAMERA_PHOTO_PATH);
        mTIPhotoBackListener = iPhotoBackListener;
        this.mFragment = mFragment;
        this.mContext = mFragment.getContext();
        this.mCropShape = cropShape;
        photosList = new ArrayList<>(MAX_DEFAULT_COUNT);
    }

    @Override
    public void getPhotoListFromSelector(int maxCount, ArrayList<String> selectedPhotos) {
        this.maxCount = maxCount;
        // 选择相册
        PhotoPicker.builder()
                .setPreviewEnabled(maxCount != 1) // 是否可预览
                .setGridColumnCount(PHOTO_CLUMS_SIZE)      // 每行的图片数量
                .setPhotoCount(maxCount)    //  每次能够选择的最
                .setShowCamera(true)        // 是否需要展示相机
                .setSelected(selectedPhotos)// 已经选择的图片
                .start(mContext, mFragment);
    }

    boolean getCameraPermissonSuccess = false;

    @Override
    public void getPhotoFromCamera(ArrayList<String> selectedPhotos) {
        // 添加相机权限设置
        mFragment.mRxPermissions
                .requestEach(Manifest.permission.CAMERA)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            // 权限被允许
                            getCameraPermissonSuccess = true;
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 权限没有被彻底禁止
                            getCameraPermissonSuccess = false;
                        } else {
                            // 权限被彻底禁止
                            mTIPhotoBackListener.getPhotoFailure("cannot start camera");
                            getCameraPermissonSuccess = false;
                            initPermissionPopUpWindow();
                            mActionPopupWindow.show();
                        }
                    }
                });

        if (!getCameraPermissonSuccess) {
            return;
        }
        this.maxCount = 1;// 从相机拿到的是单张的图片
        // 在sd卡中创建文件夹，用来保存app拍摄的图片
        boolean suc = FileUtils.createOrExistsDir(takePhotoFolder);
        File toFile = new File(takePhotoFolder, "IMG" + format() + ".jpg");
        mTakePhotoPath = toFile.getAbsolutePath();
        if (suc) {
            photosList.clear();// 清空之前的图片，重新装载
            // 添加已选择的图片，防止丢失
            if (selectedPhotos != null) {
                for (String pic : selectedPhotos) {
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(pic);
                    photosList.add(imageBean);
                }
            }

            Uri mTakePhotoUri = FileProvider.getUriForFile(mFragment.getContext(),
                    "ThinkSNSFileProvider", toFile);
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    mContext.grantUriPermission(packageName, mTakePhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            mFragment.startActivityForResult(captureIntent, CAMERA_PHOTO_CODE);

        } else {
            // 无法启动相机
            mTIPhotoBackListener.getPhotoFailure("cannot start camera");
        }

    }

    @Override
    public void startToCraft(String imgPath) {
        String destinationFileName = "SampleCropImage" + format() + ".jpg";
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(imgPath)), Uri.fromFile(new File(mFragment
                .getActivity().getCacheDir(), destinationFileName)));
        UCrop.Options options = new UCrop.Options();
        initCropShape(uCrop, options);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);    // 图片质量压缩
        options.setCircleDimmedLayer(false); // 是否裁剪圆形
        options.setHideBottomControls(true);// 是否隐藏底部的控制面板
        options.setCropFrameColor(Color.TRANSPARENT);// 设置内矩形边框线条颜色
        options.setShowCropGrid(false);// 是否展示内矩形的分割线
        options.setToolbarCancelDrawable(R.mipmap.topbar_back);
        switch (mCropShape) {
            case SHAPE_SQUARE:// 更换头像
                options.setToolbarTitle(mContext.getString(R.string.change_head_icon));
                break;
            case SHAPE_RCTANGLE:// 更换封面
                options.setToolbarTitle(mContext.getString(R.string.change_bg_cover));
                break;
            default:// 一般不会发生
                options.setToolbarTitle(mContext.getString(R.string.crop_photo));
        }
        options.setDimmedLayerColor(Color.argb(0xcc, 0xff, 0xff, 0xff));// 设置蒙层的颜色
        options.setRootViewBackgroundColor(Color.WHITE);// 设置图片背景颜色
        options.setToolbarColor(ContextCompat.getColor(mContext, R.color.white));
        uCrop.withOptions(options);
        uCrop.start(mContext, mFragment);
    }


    /**
     * 统一的判断是否需要进行裁剪的逻辑:指定是否需要裁剪
     * 添加一个imgPath参数，如果没有选择图片，就跳过剪切的逻辑
     *
     * @return
     */
    @Override
    public boolean isNeededCraft(String imgPath) {

        return mCropShape != NO_CRAFT && !TextUtils.isEmpty(imgPath);
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
            // 从相机中获取照片
            if (requestCode == CAMERA_PHOTO_CODE && !TextUtils.isEmpty(mTakePhotoPath)) {
                File file = new File(mTakePhotoPath);
                if (file.exists()) {
                    // 图片插入系统图库
                    FileUtils.insertPhotoToAlbumAndRefresh(mContext, file);
                    // 是否需要剪裁，不需要就直接返回结果
                    if (isNeededCraft(mTakePhotoPath)) {
                        startToCraft(mTakePhotoPath);
                    } else {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImgUrl(mTakePhotoPath);
                        photosList.add(imageBean);
                        mTIPhotoBackListener.getPhotoSuccess(photosList);
                    }
                } else {
                    mTIPhotoBackListener.getPhotoFailure("cannot get photo");
                }
            }
            // 裁剪图片正确
            if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    ImageBean imageBean = packageCropResult(data);
                    photosList.add(imageBean);// 获取裁剪图片的路径
                    mTIPhotoBackListener.getPhotoSuccess(photosList);
                } else {
                    // 无法裁剪
                    mTIPhotoBackListener.getPhotoFailure("cannot crop");
                }
            }
            // 从本地相册获取图片
            if (requestCode == 1000) {
                photosList.clear();// 清空之前的图片，重新装载
                ArrayList<ImageBean> tolls;
                try {
                    tolls = data.getBundleExtra(TOLL).getParcelableArrayList(TOLL);
                    mTolls.clear();
                    mTolls.addAll(tolls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ArrayList<String> photos = data.getStringArrayListExtra("photos");
                String craftPath = "";
                if (photos == null || photos.isEmpty()) {
                    craftPath = "";
                } else {
                    craftPath = photos.get(0);
                }
                if (isNeededCraft(craftPath)) {
                    startToCraft(craftPath);
                } else {
                    for (int i = 0; i < photos.size(); i++) {
                        ImageBean imageBean = new ImageBean();
                        imageBean.setImgUrl(photos.get(i));
                        try {
                            imageBean.setToll(mTolls.get(i).getToll());
                        } catch (Exception e) {
                            LogUtils.d("第"+i+"张图片没有设置收费");
                        }
                        photosList.add(imageBean);
                    }
//                    for (String imgUrl : photos) {
//                        ImageBean imageBean = new ImageBean();
//                        imageBean.setToll_type(type);
//                        imageBean.setToll_monye(money);
//                        imageBean.setImgUrl(imgUrl);
//                        photosList.add(imageBean);
//                    }
                    // 需要注意的是：用户有可能清空之前选择的所有图片，然后返回，这样就没有图片了
                    // 需要注意有可能造成的数组越界
                    mTIPhotoBackListener.getPhotoSuccess(photosList);
                }
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
        imageBean.setHeight(height);
        imageBean.setWidth(width);
        imageBean.setImgUrl(imgPath);
        return imageBean;
    }

    /**
     * 格式化照片时间，作为文件名称
     *
     * @return
     */
    private String format() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * 处理裁剪框的形状
     */
    private void initCropShape(UCrop uCrop, UCrop.Options options) {
        switch (mCropShape) {
            case SHAPE_SQUARE:// 更换头像
                uCrop.withAspectRatio(1, 1);
                options.setCropViewPadding(ConvertUtils.dp2px(mContext, SQUARE_LEFT_MARGIN), 0);
                break;
            case SHAPE_RCTANGLE:// 更换封面
                uCrop.withAspectRatio(1, 0.5f);// 矩形高度为屏幕宽度的一半
                options.setCropViewPadding(0, 0);
                break;
            default:
        }
    }

    /**
     * 添加用户完全禁止权限后的提示弹框
     */
    private void initPermissionPopUpWindow() {
        if (mActionPopupWindow != null) {
            return;
        }
        mActionPopupWindow = PermissionPopupWindow.builder()
                .permissionName(mFragment.getString(R.string.camera_permission))
                .with(mFragment.getActivity())
                .bottomStr(mFragment.getString(R.string.cancel))
                .item1Str(mFragment.getString(R.string.setting_permission_hint))
                .item2Str(mFragment.getString(R.string.setting_permission))
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        DeviceUtils.openAppDetail(mFragment.getContext());
                        mActionPopupWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .isFocus(true)
                .isOutsideTouch(true)
                .backgroundAlpha(0.8f)
                .build();
    }

}