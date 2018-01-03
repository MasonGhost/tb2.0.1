package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/5
 * @Contact master.jungle68@gmail.com
 */
public class PhotoViewDataCacheBean implements Serializable {

    private static final long serialVersionUID = 336871008;

    private ArrayList<String> allPhotos;
    private ArrayList<String> selectedPhoto;
    private ArrayList<AnimationRectBean> animationRectBeanArrayList;
    private int maxCount;
    private int currentPosition;
    private boolean isToll;
    private ArrayList<ImageBean> selectedPhotos;

    public ArrayList<String> getAllPhotos() {
        return allPhotos;
    }

    public void setAllPhotos(ArrayList<String> allPhotos) {
        this.allPhotos = allPhotos;
    }

    public ArrayList<String> getSelectedPhoto() {
        return selectedPhoto;
    }

    public void setSelectedPhoto(ArrayList<String> selectedPhoto) {
        this.selectedPhoto = selectedPhoto;
    }

    public ArrayList<AnimationRectBean> getAnimationRectBeanArrayList() {
        return animationRectBeanArrayList;
    }

    public void setAnimationRectBeanArrayList(ArrayList<AnimationRectBean> animationRectBeanArrayList) {
        this.animationRectBeanArrayList = animationRectBeanArrayList;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isToll() {
        return isToll;
    }

    public void setToll(boolean toll) {
        isToll = toll;
    }

    public ArrayList<ImageBean> getSelectedPhotos() {
        return selectedPhotos;
    }

    public void setSelectedPhotos(ArrayList<ImageBean> selectedPhotos) {
        this.selectedPhotos = selectedPhotos;
    }
}
