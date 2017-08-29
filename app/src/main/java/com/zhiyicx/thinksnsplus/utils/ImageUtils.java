package com.zhiyicx.thinksnsplus.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.baseproject.widget.textview.CircleImageDrawable;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import java.io.InputStream;
import java.util.Locale;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class ImageUtils {
    public static final String SHAREPREFERENCE_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";
    public static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";

    public static final String SHAREPREFERENCE_USER_COVER_SIGNATURE = "sharepreference_user_cover_signature";
    public static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE = "sharepreference_user_cover_signature";
    public static final long DEFAULT_USER_CACHE_TIME = 3 * 24 * 60_1000;
    public static final long DEFAULT_SHAREPREFERENCES_OFFSET_TIME = 10_1000;
    public static long laste_request_time;

    private static long mHeadPicSigture;
    private static long mCoverSigture;

    public static void updateCurrentLoginUserHeadPicSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE, System.currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    public static void updateCurrentLoginUserCoverSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE, System.currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    /**
     * 加载用户背景图
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadUserCover(UserInfoBean userInfoBean, ImageView imageView) {
        long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();

        if (userInfoBean.getUser_id() == currentLoginUerId) {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE);
        } else {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_USER_COVER_SIGNATURE);
        }
        if (System.currentTimeMillis() - mCoverSigture > DEFAULT_USER_CACHE_TIME) {
            mCoverSigture = System.currentTimeMillis();
        }
        SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE : SHAREPREFERENCE_USER_COVER_SIGNATURE, mHeadPicSigture);
        Glide.with(imageView.getContext())
                .load(userInfoBean.getCover())
                .signature(new StringSignature(String.valueOf(mCoverSigture)))
                .placeholder(R.mipmap.default_pic_personal)
                .error(R.mipmap.default_pic_personal)
                .into(imageView);
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
        loadUserAvatar(userInfoBean, imageView.getIvAvatar(), withBorder);
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(userInfoBean.getVerified().getIcon())
                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap.pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap.pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 问答那里的图文混排头像处理
     * @param userInfoBean
     * 问答那里的图文混排头像处理
     * @param userInfoBean 回答者的用户信息
     * @param contentTextView
     * @param content 回答的内容
     * @param isAnonymity 是否匿名回答
     * @param withBorder
     * @description 单纯的一个丑字根本描述不了这段代码 by tym
     */
    public static void loadQAUserHead(UserInfoBean userInfoBean, TextView contentTextView, String content, boolean isAnonymity, boolean withBorder) {
        loadQAUserAvatar(userInfoBean, contentTextView, content, isAnonymity, withBorder);
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
            avatar = TextUtils.isEmpty(userInfoBean.getAvatar()) ? getUserAvatar(userInfoBean.getUser_id()) : userInfoBean.getAvatar();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() == currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE : SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        Glide.with(imageView.getContext())
                .load(avatar)
                .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                .placeholder(R.mipmap.pic_default_portrait1)
                .error(R.mipmap.pic_default_portrait1)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);
    }

    /**
     * 问答那里的图文混排头像处理
     * @param userInfoBean
     * @param withBorder
     */
    private static void loadQAUserAvatar(UserInfoBean userInfoBean, TextView contentTextView, String content, boolean isAnonymity, boolean withBorder) {
        String avatar = "";
        if (userInfoBean != null) {
            avatar = userInfoBean.getAvatar();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getExtra().getUser_id() == currentLoginUerId) {

                if (userInfoBean.getExtra().getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(AppApplication.getContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(AppApplication.getContext(), SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(AppApplication.getContext()
                        , userInfoBean.getExtra().getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE : SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        Glide.with(AppApplication.getContext())
                .load(avatar)
                .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                .placeholder(R.mipmap.pic_default_portrait1)
                .error(R.mipmap.pic_default_portrait1)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(AppApplication.getContext(), AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(AppApplication.getContext(), R.color.white))
                        : new GlideCircleTransform(AppApplication.getContext()))
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Bitmap avatar = ConvertUtils.drawable2BitmapWithWhiteBg(AppApplication.getContext()
                                , resource, R.mipmap.pic_default_portrait1);
                        Bitmap newBmp = Bitmap.createScaledBitmap(avatar,
                                contentTextView.getLineHeight(), contentTextView.getLineHeight(), true);

                        CircleImageDrawable headImage = new CircleImageDrawable(newBmp);

                        headImage.setBounds(0, 0, contentTextView.getLineHeight(), contentTextView.getLineHeight());
                        ImageSpan imgSpan = new CenterImageSpan(headImage, isAnonymity);
                        SpannableString spannableString = SpannableString.valueOf("T" +"\b\b"+userInfoBean.getName()+"\b :"+ RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, content));
                        spannableString.setSpan(imgSpan, 0, 1, Spannable
                                .SPAN_EXCLUSIVE_EXCLUSIVE);
                        contentTextView.setText(spannableString);


                        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
                            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                                userInfoBean.getVerified().setIcon("");
                            }
                            int w = (int) (headImage.getBounds().width() / 3.5f);
                            Glide.with(AppApplication.getContext())
                                    .load(userInfoBean.getVerified().getIcon())
                                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                                    .override(w, w)
                                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap.pic_identi_individual)
                                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap.pic_identi_individual)
                                    .transform(withBorder ?
                                            new GlideCircleBorderTransform(AppApplication.getContext(), AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(AppApplication.getContext(), R.color.white))
                                            : new GlideCircleTransform(AppApplication.getContext()))
                                    .into(new SimpleTarget<GlideDrawable>() {
                                        @Override
                                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                            ImageSpan imgSpan = new CenterImageSpan(headImage, resource, isAnonymity);
                                            SpannableString spannableString = SpannableString.valueOf("T" +"\b\b"+userInfoBean.getName()+"\b :"+ RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, content));
                                            spannableString.setSpan(imgSpan, 0, 1, Spannable
                                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                                            contentTextView.setText(spannableString);
                                        }

                                        @Override
                                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                            super.onLoadFailed(e, errorDrawable);
                                            errorDrawable.setBounds(0, 0, w, w);
                                            ImageSpan imgSpan = new CenterImageSpan(headImage, errorDrawable, isAnonymity);
                                            SpannableString spannableString = SpannableString.valueOf("T" + RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, content));
                                            spannableString.setSpan(imgSpan, 0, 1, Spannable
                                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                                            contentTextView.setText(spannableString);
                                        }

                                    });
                        }


                    }
                });
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
        if (TextUtils.isEmpty(userInfoBean.getAvatar())) {
            return String.format(ApiConfig.IMAGE_AVATAR_PATH_V2, userInfoBean.getUser_id());
        } else {
            return userInfoBean.getAvatar();
        }

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
        String url = String.format(Locale.getDefault(), ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
        if (!canLook) {
            url = "zhiyicx";
        }
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
        return String.format(Locale.getDefault(), ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
    }

    public static class V2ImageHeaderedLoader extends BaseGlideUrlLoader<String> {
        final Headers HEADERS;

        public V2ImageHeaderedLoader(Context context, String token) {
            super(context);
            HEADERS = new LazyHeaders.Builder()
                    .addHeader("Authorization", token)
                    .build();
        }

        @Override
        protected String getUrl(String model, int width, int height) {
            LogUtils.e("getUrl::" + model);
            return model;
        }

        @Override
        public DataFetcher<InputStream> getResourceFetcher(String model, int width, int height) {
            return super.getResourceFetcher(model, width, height);
        }

        @Override
        protected Headers getHeaders(String model, int width, int height) {
            return HEADERS;
        }

        public static class StreamFactory implements ModelLoaderFactory<String, InputStream> {
            String token;

            public StreamFactory(String token) {
                this.token = token;
            }

            @Override
            public StreamModelLoader<String> build(Context context, GenericLoaderFactory factories) {
                return new V2ImageHeaderedLoader(context, token);
            }

            @Override
            public void teardown() { /* nothing to free */ }
        }
    }

}
