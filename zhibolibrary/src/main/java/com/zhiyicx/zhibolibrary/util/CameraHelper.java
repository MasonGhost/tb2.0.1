package com.zhiyicx.zhibolibrary.util;

import android.annotation.SuppressLint;
import android.hardware.Camera;

/**
 * Created by zhiyicx on 2016/3/26.
 */
public class CameraHelper {
    public static final int FRONT = 1;
    public static final int BACK = 2;

    @SuppressLint("NewApi")
    public static Camera openCamera(int type) {
        int frontIndex = -1;
        int backIndex = -1;
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int cameraIndex = 0; cameraIndex < cameraCount; cameraIndex++) {
            Camera.getCameraInfo(cameraIndex, info);
            if (info.facing == info.CAMERA_FACING_FRONT) {
                frontIndex = cameraIndex;
            } else if (info.facing == info.CAMERA_FACING_BACK) {
                backIndex = cameraIndex;
            }
        }

//        currentCameraType = type;
        if (type == FRONT && frontIndex != -1) {
            return Camera.open(frontIndex);
        } else if (type == BACK && backIndex != -1) {
            return Camera.open(backIndex);
        }
        return null;
    }

}
