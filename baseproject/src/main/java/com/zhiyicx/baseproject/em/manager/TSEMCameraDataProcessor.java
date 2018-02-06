package com.zhiyicx.baseproject.em.manager;

import android.hardware.Camera;

import com.hyphenate.chat.EMCallManager;

/**
 * @author Jliuer
 * @Date 18/02/06 11:42
 * @Email Jliuer@aliyun.com
 * @Description 视频数据解析
 */
public class TSEMCameraDataProcessor implements EMCallManager.EMCameraDataProcessor {

    byte yDelta = 0;

    synchronized void setYDelta(byte yDelta) {
        this.yDelta = yDelta;
    }

    /**
     * @author Jliuer
     * @Date 18/01/31 17:54
     * @Email Jliuer@aliyun.com
     * @Description @Link RtcConnection 2015
     */
    @Override
    public synchronized void onProcessData(byte[] bytes, Camera camera, int width, int height, int rotation) {
        int wh = width * height;
        for (int i = 0; i < wh; i++) {
            int d = (bytes[i] & 0xFF) + yDelta;
            d = d < 16 ? 16 : d;
            d = d > 235 ? 235 : d;
            bytes[i] = (byte) d;
        }
    }

}
