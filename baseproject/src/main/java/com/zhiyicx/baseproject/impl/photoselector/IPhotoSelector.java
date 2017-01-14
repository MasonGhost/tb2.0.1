package com.zhiyicx.baseproject.impl.photoselector;

import java.util.List;

/**
 * @author LiuChao
 * @describe 从本地相册选择图片的接口
 * @date 2017/1/13
 * @contact email:450127106@qq.com
 */

public interface IPhotoSelector<T> {
    /**
     * 从本地相册中获取图片
     *
     * @param maxCount 每次能够获取的最大图片数量
     * @return
     */
    void getPhotoListFromSelector(int maxCount);

    /**
     * 通过拍照的方式获取图片
     */
    void getPhotoFromCamera();

    /**
     * 对图片进行裁剪
     */
    void startToCraft(String imgPath);

    /**
     * 判断是否需要裁剪
     */
    boolean isNeededCraft();


}
