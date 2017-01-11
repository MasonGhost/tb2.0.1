package com.zhiyicx.baseproject.impl.imageloader;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
        DrawableRequestBuilder requestBuilder = manager.load(TextUtils.isEmpty(config.getUrl()) ? config.getResourceId() : config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop();
        if (config.getTransformation() != null) {
            requestBuilder.transform(config.getTransformation());
        }
        if (config.getPlaceholder() != 0)// 设置占位符
        {
            requestBuilder.placeholder(config.getPlaceholder());
        }
        if (config.getErrorPic() != 0)// 设置错误的图片
        {
            requestBuilder.error(config.getErrorPic());
        }
        requestBuilder
                .into(config.getImageView());
    }
}
