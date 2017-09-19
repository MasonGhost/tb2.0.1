package com.jess.camerafilters.base;

import android.content.Context;

import com.jess.camerafilters.R;
import com.jess.camerafilters.filter.CameraAncientFilter;
import com.jess.camerafilters.filter.CameraFilterBeauty;
import com.jess.camerafilters.filter.CameraFilterToneCurve;
import com.jess.camerafilters.filter.CameraGrayFilter;
import com.jess.camerafilters.filter.IFilter;
import com.jess.camerafilters.filter.guiFilter.GPUImageFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jess on 8/17/16 15:50
 * Contact with jess.yan.effort@gmail.com
 */
public class FilterFactory {
    //    private static int[] mCurveArrays = new int[]{
//            R.raw.cross_1, R.raw.cross_2, R.raw.cross_3, R.raw.cross_4, R.raw.cross_5,
//            R.raw.cross_6, R.raw.cross_7, R.raw.cross_8, R.raw.cross_9, R.raw.cross_10,
//            R.raw.cross_11,
//    };
    private static int[] mCurveArrays = new int[]{
            R.raw.cross_1, R.raw.cross_2, R.raw.cross_3, R.raw.cross_4, R.raw.cross_5,
            R.raw.cross_6, R.raw.cross_7, R.raw.cross_8, R.raw.cross_9, R.raw.cross_10,
            R.raw.cross_11,
    };

    private FilterFactory() {
    }

    /**
     * 内部一共有14种滤镜(包括透明滤镜index为0)
     *
     * @param context
     * @param index
     * @return
     */
    static List<GPUImageFilter> filters = new ArrayList<>();

    public static IFilter getCameraFilter(Context context, int index) {
        if (index > 7) {
            index = index % 8;
        }
//        if (index > 7 + mCurveArrays.length - 1 || index < 0) {
//            throw new IllegalArgumentException("not have this index.");
//        }
        switch (index) {
            case 0:
                return new CameraFilterBeauty(context);//CameraFilterBeauty
            case 1:
                return new CameraGrayFilter(context);
            case 2:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(R.raw.ziran));
            case 3:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(R.raw.wennuan));
            case 4:
                return new CameraAncientFilter(context);
            case 5:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(R.raw.xiaoqingxin));
            case 6:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(R.raw.cross_3));
            case 7:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(R.raw.qiuse));

            default:
                return new CameraFilterToneCurve(context,
                        context.getResources().openRawResource(mCurveArrays[index]));
        }
    }
}

