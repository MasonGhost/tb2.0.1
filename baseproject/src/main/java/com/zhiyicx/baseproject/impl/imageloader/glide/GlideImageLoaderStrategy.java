package com.zhiyicx.baseproject.impl.imageloader.glide;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.imageloader.loadstrategy.ImageLoaderStrategy;

/**
 * @Describe Glide统一加载配置
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideImageLoaderStrategy implements ImageLoaderStrategy<GlideImageConfig> {
    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        RequestManager manager = null;
        if (ctx instanceof Activity)//如果是activity则可以使用Activity的生命周期
            manager = Glide.with((Activity) ctx);
        else
            manager = Glide.with(ctx);
        String imgUrl = config.getUrl();
        boolean isFromNet = !TextUtils.isEmpty(imgUrl) && imgUrl.startsWith("http");// 是否来源于网络
        DrawableRequestBuilder requestBuilder = manager.load(TextUtils.isEmpty(imgUrl) ? config.getResourceId() : isFromNet ? imgUrl : "file://" + imgUrl)
                .diskCacheStrategy(isFromNet ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .skipMemoryCache(isFromNet ? false : true)
                .crossFade()
                .centerCrop();
        if (config.getErrorPic() != 0)// 设置错误的图片
        {
            requestBuilder.error(config.getErrorPic());
        } else {
            requestBuilder.error(R.drawable.shape_default_image);
        }
        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }
        if (config.getPlaceholder() != 0)// 设置占位符
        {
            requestBuilder.placeholder(config.getPlaceholder());
        }
        requestBuilder
                .into(config.getImageView());
    }
}
