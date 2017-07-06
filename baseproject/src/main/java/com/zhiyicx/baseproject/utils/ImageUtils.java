package com.zhiyicx.baseproject.utils;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.file_descriptor.FileDescriptorFileLoader;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.InputStream;
import java.util.Locale;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class ImageUtils {

    /**
     * 图片地址转换
     *
     * @param storage 图片对应的 id 号，也可能是本地的图片路径
     * @param part    压缩比例 0-100
     * @return
     */
    public static String imagePathConvert(String storage, int part) {
        try {
            // 如果图片的storage能够转成一个整数
            Integer.parseInt(storage);
            return String.format(Locale.getDefault(), ApiConfig.IMAGE_PATH, storage, part);
        } catch (NumberFormatException e) {
            return storage;
        }
    }

    /**
     * 图片地址转换 V2 api
     *
     * @param canLook  是否可查看
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
     * @param url 图片地址
     * @param token 图片token
     * @return
     */
    public static GlideUrl imagePathConvertV2(String url, String token) {
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     *
     * @param storage 图片资源id
     * @param w 宽
     * @param h 高
     * @param part 压缩比例
     * @param token token
     * @return
     */
    public static GlideUrl imagePathConvertV2(int storage, int w, int h, int part, String token) {
        return new GlideUrl(imagePathConvertV2(storage,w,h,part), new LazyHeaders.Builder()
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
