package com.zhiyicx.thinksnsplus.modules.gallery;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.load.model.GlideUrl;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.Locale;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/4
 * @Contact master.jungle68@gmail.com
 */

public class CustomImageSizeModelImp implements CustomImageSizeModel, Parcelable {
    private ImageBean mImageBean;
    private static final String TAG = "CustomImageSizeModelImp";

    public CustomImageSizeModelImp(ImageBean imageBean) {
        this.mImageBean = imageBean;
    }


    @Override
    public String requestCustomSizeUrl(int width, int height) {
        String url = mImageBean.getImgUrl() == null ? String.format(ApiConfig.IMAGE_PATH, mImageBean.getStorage_id(), mImageBean.getPart()) : mImageBean.getImgUrl();
        LogUtils.d(TAG, "requestCustomSizeUrl: " + url);
        return url;
    }

    @Override
    public String requestCustomSizeUrl() {
        return mImageBean.getImgUrl() == null
                ? String.format(Locale.getDefault(),ApiConfig.IMAGE_PATH, mImageBean.getStorage_id(), mImageBean.getPart())
                : mImageBean.getImgUrl();
    }

    public GlideUrl requestGlideUrl() {
        return null;
    }

    @Override
    public ImageBean getImageBean() {
        return mImageBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mImageBean, flags);
    }

    protected CustomImageSizeModelImp(Parcel in) {
        this.mImageBean = in.readParcelable(ImageBean.class.getClassLoader());
    }

    public static final Creator<CustomImageSizeModelImp> CREATOR = new Creator<CustomImageSizeModelImp>() {
        @Override
        public CustomImageSizeModelImp createFromParcel(Parcel source) {
            return new CustomImageSizeModelImp(source);
        }

        @Override
        public CustomImageSizeModelImp[] newArray(int size) {
            return new CustomImageSizeModelImp[size];
        }
    };
}