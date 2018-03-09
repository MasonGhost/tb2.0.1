package com.zhiyicx.thinksnsplus.utils;

import java.util.Comparator;

/**
 * @Author Jliuer
 * @Date 2017/6/9 16:00
 * @Email Jliuer@aliyun.com
 * @Description 时间排序工具 基类
 */
public abstract class BaseTimeStringSortClass<D> implements Comparator<D> {

    /**
     * 反转
     */
    private boolean reversed = true;

    public BaseTimeStringSortClass(boolean reversed) {
        this.reversed = reversed;
    }

    public BaseTimeStringSortClass() {
    }

    @Override
    public int compare(D data1, D data2) {
        if (reversed) {
            return getData2Time(data2).compareTo(getData1Time(data1));
        } else {
            return getData1Time(data1).compareTo(getData2Time(data2));
        }
    }

    protected abstract String getData1Time(D data);

    protected abstract String getData2Time(D data);

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

}