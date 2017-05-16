package com.zhiyicx.thinksnsplus.utils;

import android.content.Context;
import android.widget.ImageView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class BannerImageLoaderUtil extends com.youth.banner.loader.ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        AppApplication.AppComponentHolder.getAppComponent()
                .imageLoader()
                .loadImage(context, GlideImageConfig.builder()
                        .imagerView(imageView)
                        .url((String) path)
                        .errorPic(R.drawable.shape_default_image)
                        .build());
    }

}