package com.zhiyicx.baseproject.utils;

import com.zhiyicx.baseproject.config.ApiConfig;

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
            return String.format(Locale.getDefault(),ApiConfig.IMAGE_PATH, storage, part);
        } catch (NumberFormatException e) {
            return storage;
        }
    }

}
