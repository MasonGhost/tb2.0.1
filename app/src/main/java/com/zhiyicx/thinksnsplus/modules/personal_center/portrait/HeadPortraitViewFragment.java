package com.zhiyicx.thinksnsplus.modules.personal_center.portrait;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.PathConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
//import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import static com.zhiyicx.baseproject.utils.ImageUtils.imagePathConvertV2;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.personal_center.portrait.HeadPortraitViewActivity.BUNDLE_USER_INFO;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/12
 * @contact email:648129313@qq.com
 */

public class HeadPortraitViewFragment extends TSFragment<HeadPortraitViewContract.Presenter>
        implements HeadPortraitViewContract.View, PhotoSelectorImpl.IPhotoBackListener {

    public static final String USER_STORAGE_TASK_ID = "storage_task_id";
    public static final String USER_LOCAL_IMG_PATH = "localImgPath";

    @BindView(R.id.iv_portrait_preview)
    ImageView mIvPortraitPreview;
    @BindView(R.id.btn_change_portrait)
    Button mBtnChangePortrait;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;

    private UserInfoBean mUserInfoBean;
    private boolean mIsLoginUser;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private PhotoSelectorImpl mPhotoSelector;
    private String path;// 上传成功的图片本地路径
    private TSnackbar mTSnackbarUploadIcon;
    private int upDateHeadIconStorageId = 0;// 上传成功返回的图片id

    public static HeadPortraitViewFragment instance(Bundle bundle) {
        HeadPortraitViewFragment fragment = new HeadPortraitViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mIvPortraitPreview.getLayoutParams().width = UIUtils.getWindowWidth(getContext());
        mIvPortraitPreview.getLayoutParams().height = UIUtils.getWindowHeight(getContext()) * 2 / 3;
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build()
                .photoSelectorImpl();
        initPhotoPopupWindow();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {
        int width = UIUtils.getWindowWidth(getContext());
        int height = UIUtils.getWindowHeight(getContext()) * 2 / 3;
        mUserInfoBean = (UserInfoBean) getArguments().getSerializable(BUNDLE_USER_INFO);
        if (mUserInfoBean == null) {
            mUserInfoBean = mPresenter.getCurrentUser(AppApplication.getmCurrentLoginAuth().getUser_id());
            mIsLoginUser = true;
        } else {
            mIsLoginUser = mUserInfoBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id();
        }
        if (mUserInfoBean != null) {
            int avatarId;
            String userIconUrl;
            try {
                avatarId = Integer.parseInt(mUserInfoBean.getAvatar());
//                userIconUrl = imagePathConvertV2(avatarId
//                        , width
//                        , height
//                        , ImageZipConfig.IMAGE_70_ZIP);
                // 方法改变了 暂时先这样
                userIconUrl = "";
            } catch (Exception e) {
                userIconUrl = mUserInfoBean.getAvatar();
            }
            Glide.with(getContext())
                    .load(userIconUrl)
                    .override(width, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.pic_locked)
                    .into(mIvPortraitPreview);
        }
        if (!mIsLoginUser) {
            //  如果非登陆用户，则保存图片
            mBtnChangePortrait.setText(getString(R.string.save_to_photo));
        }
        initListener();
    }

    private void initListener() {
        RxView.clicks(mBtnChangePortrait)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mIsLoginUser) {
                        // Todo 修改头像
                        mPhotoPopupWindow.show();
                    } else {
                        // 保存图片
                        saveImage();
                    }
                });
        RxView.clicks(mBtnCancel)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());

        RxView.clicks(mIvPortraitPreview)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 全屏查看
                    List<ImageBean> imageBeanList = new ArrayList<>();
                    ArrayList<AnimationRectBean> animationRectBeanArrayList
                            = new ArrayList<>();
                    ImageBean imageBean = new ImageBean();
                    int avatar = 0;
                    try {
                        avatar = Integer.parseInt(mUserInfoBean.getAvatar());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    // 这个付费才能查看他人的头像吗？
                    Toll toll = new Toll(); // 收费信息
                    toll.setPaid(true);// 是否已經付費
                    toll.setToll_money(0);// 付费金额
                    toll.setToll_type_string("");// 付费类型
                    toll.setPaid_node(0);// 付费节点
                    imageBean.setToll(toll);
                    imageBean.setWidth(UIUtils.getWindowWidth(getContext()));
                    imageBean.setHeight(UIUtils.getWindowWidth(getContext()));
                    imageBean.setStorage_id(avatar);
                    imageBeanList.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(mIvPortraitPreview);
                    animationRectBeanArrayList.add(rect);
                    GalleryActivity.startToGallery(getContext(), 0, imageBeanList,
                            animationRectBeanArrayList);
                });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_head_portrait_view;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    public void saveImage() {
        // 通过Glide获取bitmap,有缓存读缓存
        int width = UIUtils.getWindowWidth(getContext());
        int height = UIUtils.getWindowHeight(getContext()) * 2 / 3;
        int avatar = 0;
        try {
            avatar = Integer.parseInt(mUserInfoBean.getAvatar());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
//        GlideUrl glideUrl = ImageUtils.imagePathConvertV2(avatar, width, height
//                , ImageZipConfig.IMAGE_100_ZIP, AppApplication.getTOKEN());
//        Glide.with(getActivity())
//                .load(glideUrl)
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        getSaveBitmapResultObservable(resource, glideUrl.toStringUrl());
//                    }
//                });
    }

    /**
     * 通过Rxjava在io线程中处理保存图片的逻辑，得到返回结果，否则会阻塞ui
     */
    private void getSaveBitmapResultObservable(final Bitmap bitmap, final String url) {
        Observable.just(1)// 不能empty否则map无法进行转换
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> {// .subscribeOn(Schedulers.io())  Animators may only be run on Looper threads
                    TSnackbar.make(mSnackRootView, getString(R.string.save_pic_ing), TSnackbar.LENGTH_INDEFINITE)
                            .setPromptThemBackground(Prompt.SUCCESS)
                            .addIconProgressLoading(0, true, false)
                            .setMinHeight(0, getResources().getDimensionPixelSize(R.dimen.toolbar_height))
                            .show();
                })
                .map(integer -> {
                    String imgName = ConvertUtils.getStringMD5(url) + ".jpg";
                    String imgPath = PathConfig.PHOTO_SAVA_PATH;
                    return DrawableProvider.saveBitmap(bitmap, imgName, imgPath,mActivity.getApplicationContext());
                })
                .subscribeOn(AndroidSchedulers.mainThread())// subscribeOn & doOnSubscribe 的特殊性质
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    switch (result) {
                        case "-1":
                            result = getString(R.string.save_failure1);
                            break;
                        case "-2":
                            result = getString(R.string.save_failure2);
                            break;
                        default:
                            File file = new File(result);
                            if (file.exists()) {
                                result = getString(R.string.save_success) + result;
                                FileUtils.insertPhotoToAlbumAndRefresh(getContext(), file);
                            }
                    }
                    TSnackbar.make(mSnackRootView, result, TSnackbar.LENGTH_SHORT)
                            .setPromptThemBackground(Prompt.SUCCESS)
                            .setMinHeight(0, getResources().getDimensionPixelSize(R.dimen.toolbar_height))
                            .show();
                });
    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        path = photoList.get(0).getImgUrl();
        // 开始上传
        mPresenter.changeUserHeadIcon(path);
        // 加载本地图片
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(path)
                .imagerView(mIvPortraitPreview)
                .placeholder(R.drawable.shape_default_image_circle)
                .errorPic(R.drawable.shape_default_image_circle)
                .build());
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    @Override
    public void setUpLoadHeadIconState(int upLoadState, int taskId) {
        // 上传成功，可以进行修改
        switch (upLoadState) {
            case -1:
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        getString(R.string.update_head_failure), TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.ERROR)
                        .show();
                break;
            case 0:
                mIvPortraitPreview.setClickable(false);
                mTSnackbarUploadIcon = TSnackbar.make(mSnackRootView, R.string.update_head_ing, TSnackbar.LENGTH_INDEFINITE)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .addIconProgressLoading(0, true, false);
                mTSnackbarUploadIcon.show();
                break;
            case 1:
                upDateHeadIconStorageId = taskId;
                mPresenter.updateUserInfo(packageUserHeadIcon(), upDateHeadIconStorageId);
                break;
            case 2:
                mIvPortraitPreview.setClickable(true);
                TSnackbar.getTSnackBar(mTSnackbarUploadIcon, mSnackRootView,
                        getString(R.string.update_head_success), TSnackbar.LENGTH_SHORT)
                        .setPromptThemBackground(Prompt.SUCCESS)
                        .show();
                break;
            default:
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * 用户头像再上传图片后自动提交修改，不和用户信息一起提交
     *
     * @return
     */
    private HashMap<String, String> packageUserHeadIcon() {
        HashMap<String, String> fieldMap = new HashMap<>();
        // avatar
        fieldMap.put(USER_STORAGE_TASK_ID, upDateHeadIconStorageId + "");
        fieldMap.put(USER_LOCAL_IMG_PATH, path);// 本地图片的路径，因为没有返回storage_id,用来更新图片
        return fieldMap;
    }
}
