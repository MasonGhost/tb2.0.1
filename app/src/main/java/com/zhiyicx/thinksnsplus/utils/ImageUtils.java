package com.zhiyicx.thinksnsplus.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.signature.StringSignature;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class ImageUtils {
    private static final String SHAREPREFERENCE_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";

    private static final String SHAREPREFERENCE_USER_COVER_SIGNATURE = "sharepreference_user_cover_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE = "sharepreference_user_cover_signature";
    private static final long DEFAULT_USER_CACHE_TIME = 3 * 24 * 60_1000;
    private static final long DEFAULT_SHAREPREFERENCES_OFFSET_TIME = 10_1000;
    private static long laste_request_time;

    private static long mHeadPicSigture;
    private static long mCoverSigture;

    /**
     * mWidthPixels = DeviceUtils.getScreenWidth(context);
     * mHightPixels = DeviceUtils.getScreenHeight(context);
     * mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen
     * .dynamic_list_image_marginright);
     * mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
     * mImageContainerWith = mWidthPixels - mMargin;
     * // 最大高度是最大宽度的4/3 保持 宽高比 3：4
     * mImageMaxHeight = mImageContainerWith * 4 / 3;
     */

    public static int getmWidthPixels() {
        return DeviceUtils.getScreenWidth(AppApplication.getContext());
    }

    public static int getmHightPixels() {
        return DeviceUtils.getScreenHeight(AppApplication.getContext());
    }

    public static boolean isLongImage(float netHeight, float netWidth) {
        float net = netHeight / netWidth;
        float result = 0;
        if (net >= 3 || net <= .3f) {

            result = getmWidthPixels() / netWidth;

            if (result <= .3f) {

            } else {
                result = result * netHeight / getmHightPixels();
            }
        }
        return (result >= 3 || result <= .3f) && result > 0;
    }

    public static int getmMargin() {
        return 2 * AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.dynamic_list_image_marginright);
    }

    public static int getmDiverwith() {
        return AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small);
    }

    public static int getmImageContainerWith() {
        return getmWidthPixels() - getmMargin();
    }

    public static int getmImageMaxHeight() {
        return getmImageContainerWith() * 4 / 3;
    }

    public static void updateCurrentLoginUserHeadPicSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    public static void updateCurrentLoginUserCoverSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    /**
     * 加载用户背景图
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadUserCover(UserInfoBean userInfoBean, ImageView imageView) {
        if (checkImageContext(imageView)) {
            return;
        }

        long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();

        if (userInfoBean.getUser_id() == currentLoginUerId) {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                    SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE);
        } else {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_USER_COVER_SIGNATURE);
        }
        if (System.currentTimeMillis() - mCoverSigture > DEFAULT_USER_CACHE_TIME) {
            mCoverSigture = System.currentTimeMillis();
        }
        SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE :
                        SHAREPREFERENCE_USER_COVER_SIGNATURE, mHeadPicSigture);
        Glide.with(imageView.getContext())
                .load(userInfoBean.getCover())
                .signature(new StringSignature(String.valueOf(mCoverSigture)))
                .placeholder(R.mipmap.default_pic_personal)
                .error(R.mipmap.default_pic_personal)
                .into(imageView);
    }

    public static boolean checkImageContext(View imageView) {
        if (imageView == null || imageView.getContext() == null) {
            return true;
        }
        if (imageView.getContext() instanceof Activity) {
            if (((Activity) imageView.getContext()).isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加载用户头像
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadCircleUserHeadPic(UserInfoBean userInfoBean, UserAvatarView imageView) {
        loadUserHead(userInfoBean, imageView, false);
    }

    /**
     * 加载用户头像
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     * @param anonymity    是否匿名展示
     */
    public static void loadCircleUserHeadPic(UserInfoBean userInfoBean, UserAvatarView imageView, boolean anonymity) {
        loadUserHead(userInfoBean, imageView, false, anonymity);
    }


    /**
     * 加载用户头像带有白色边框
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadCircleUserHeadPicWithBorder(UserInfoBean userInfoBean, UserAvatarView imageView) {
        loadUserHead(userInfoBean, imageView, true);
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     */
    public static void loadUserHead(UserInfoBean userInfoBean, UserAvatarView imageView, boolean withBorder) {
        if (checkImageContext(imageView)) {
            return;
        }

        loadUserAvatar(userInfoBean, imageView.getIvAvatar(), withBorder);
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(userInfoBean.getVerified().getIcon())
                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                    .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     * @param anonymity    是否匿名展示
     */
    public static void loadUserHead(UserInfoBean userInfoBean, UserAvatarView imageView, boolean withBorder, boolean anonymity) {
        if (checkImageContext(imageView)) {
            return;
        }

        FilterImageView imageView1 = imageView.getIvAvatar();
        imageView1.setIsText(anonymity);
        loadUserAvatar(userInfoBean, imageView1, withBorder);
//        if (anonymity){
//            // 匿名用户不要认证图标
//            userInfoBean.setVerified(null);
//        }
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(userInfoBean.getVerified().getIcon())
                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                    .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     */
    public static void loadUserHead(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        loadUserAvatar(userInfoBean, imageView, withBorder);
    }

    private static void loadUserAvatar(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        String avatar = "";
        if (userInfoBean != null && userInfoBean.getUser_id() != null) {
            avatar = TextUtils.isEmpty(userInfoBean.getAvatar()) ? "" : userInfoBean.getAvatar();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() ==
                    currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE :
                                SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        int defaultAvatar = getDefaultAvatar(userInfoBean);
        Glide.with(imageView.getContext())
                .load(avatar)
                .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                .placeholder(withBorder ? defaultAvatar : defaultAvatar)
                .error(withBorder ? defaultAvatar : defaultAvatar)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);
    }

    /**
     * 获取用户头像地址
     *
     * @param userId user's  id
     * @return
     */
    public static String getUserAvatar(Long userId) {
        if (userId == null) {
            userId = 0L;
        }
        return String.format(ApiConfig.IMAGE_AVATAR_PATH_V2, userId);

    }

    /**
     * 获取用户头像地址
     *
     * @param userInfoBean user's  info
     * @return
     */
    public static String getUserAvatar(UserInfoBean userInfoBean) {
        if (userInfoBean == null || userInfoBean.getAvatar() == null) {
            return "";
        } else {
            return userInfoBean.getAvatar();
        }
    }

    /**
     * 获取用户默认头像
     *
     * @param userInfoBean user's  info
     * @return
     */
    public static int getDefaultAvatar(UserInfoBean userInfoBean) {
        int defaultAvatar;
        if (userInfoBean == null) {
            return R.mipmap.pic_default_secret;
        }
        switch (userInfoBean.getSex()) {

            case UserInfoBean.FEMALE:
                defaultAvatar = R.mipmap.pic_default_woman;
                break;
            case UserInfoBean.MALE:
                defaultAvatar = R.mipmap.pic_default_man;

                break;
            case UserInfoBean.SECRET:
                defaultAvatar = R.mipmap.pic_default_secret;
                break;
            default:
                defaultAvatar = R.mipmap.pic_default_secret;

        }
        return defaultAvatar;
    }

    /**
     * 图片地址转换 V2 api
     *
     * @param canLook 是否可查看
     * @param storage 图片对应的 id 号，也可能是本地的图片路径
     * @param part    压缩比例 0-100
     * @return
     */
    public static GlideUrl imagePathConvertV2(boolean canLook, int storage, int w, int h, int part, String token) {
        String url = String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
        return imagePathConvertV2(url, token);
    }

    /**
     * 图片地址转换 V2 api
     *
     * @param url   图片地址
     * @param token 图片token
     * @return
     */
    public static GlideUrl imagePathConvertV2(String url, String token) {
//        LogUtils.d("imagePathConvertV2:" + url);
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     * @param storage 图片资源id
     * @param w       宽
     * @param h       高
     * @param part    压缩比例
     * @param token   token
     * @return
     */
    public static GlideUrl imagePathConvertV2(int storage, int w, int h, int part, String token) {
        return new GlideUrl(imagePathConvertV2(storage, w, h, part), new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    public static String imagePathConvertV2(int storage, int w, int h, int part) {
        return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
    }

    public static long[] getBitmapSize(String url) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, op);
        return new long[]{op.outWidth, op.outHeight};
    }

    /**
     * 默认加载图片
     *
     * @param imageView target view to display image
     * @param url       image resuorce path
     */
    public static void loadImageDefault(ImageView imageView, String url) {
        if (checkImageContext(imageView)) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.shape_default_image)
                .placeholder(R.drawable.shape_default_error_image)
                .into(imageView);

    }

    public static Bitmap create2Code(String str, int size) {
        Bitmap result = QRCodeEncoder.syncEncodeQRCode(str, size);
        if (result != null) {
            Bitmap logo = BitmapFactory.decodeResource(AppApplication.getContext().getResources(), R.mipmap.icon);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(logo, result.getWidth() / 2 - logo.getWidth() / 2, result.getHeight() / 2 - logo.getHeight() / 2, null);
        }
        return result;
    }


    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取listView实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sdcard/screen_test.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getBitmapByView2(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
