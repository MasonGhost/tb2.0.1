package com.zhiyicx.baseproject.impl.imageloader;

import android.widget.ImageView;

import com.zhiyicx.common.utils.imageloader.config.ImageConfig;

/**
 * @Describe Glide图片加载builder
 * @Author Jungle68
 * @Date 2016/12/15
 * @Contact 335891510@qq.com
 */

public class GlideImageConfig extends ImageConfig {

    private GlideImageConfig(Buidler builder) {
        this.url = builder.url;
        this.resourceId = resourceId;
        this.imageView = builder.imageView;
        this.placeholder = builder.placeholder;
        this.errorPic = builder.errorPic;
    }

    public static Buidler builder() {
        return new Buidler();
    }


    public static final class Buidler {
        private String url;
        protected Integer resourceId;
        private ImageView imageView;
        private int placeholder;
        protected int errorPic;

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
            if (url == null) throw new IllegalStateException("url is required");
            if (imageView == null) throw new IllegalStateException("imageview is required");
            return new GlideImageConfig(this);
        }
    }
}
