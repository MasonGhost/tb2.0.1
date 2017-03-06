package com.zhiyicx.thinksnsplus.modules.gallery;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/4
 * @Contact master.jungle68@gmail.com
 */

public interface CustomImageSizeModel {
    String requestCustomSizeUrl(int width, int height);
    String requestCustomSizeUrl();
    ImageBean getImageBean();
}
