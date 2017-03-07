package com.zhiyicx.baseproject.utils;

import com.zhiyicx.baseproject.config.ApiConfig;

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
     * @param storage 图片对应的 id 号
     * @param part  压缩比例 0-100
     * @return
     */
    public static String imagePathConvert(String storage, int part) {
        return String.format(ApiConfig.IMAGE_PATH, storage, part);
    }

}
