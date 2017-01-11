package com.zhiyicx.baseproject.impl.imageloader.glide;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.common.utils.imageloader.config.ImageConfig;

/**
 * @Describe Glide图片加载builder
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideImageConfig extends ImageConfig {
    private BitmapTransformation transformation;


    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.resourceId =builder.resourceId;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
        this.transformation=builder.transformation;
    }

    public BitmapTransformation getTransformation() {
        return transformation;
    }

    public static Buidler builder() {
        return new Buidler();
    }


    public static final class Buidler {
        private String url;
        private Integer resourceId;
        private ImageView imageView;
        private int placeholder;
        private int errorPic;
        private BitmapTransformation transformation;

        private Buidler() {
        }

        public Buidler url(String url) {
            this.url = url;
            return this;
        }

        public Buidler resourceId(Integer resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Buidler transformation(BitmapTransformation transformation) {
            this.transformation = transformation;
            return this;
        }

        public Buidler placeholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Buidler errorPic(int errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public Buidler imagerView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public GlideImageConfig build() {
            if (url == null && resourceId == 0)
                throw new IllegalStateException("url or resourceId is required");
            if (imageView == null) throw new IllegalStateException("imageview is required");
            return new GlideImageConfig(this);
        }
    }
}
