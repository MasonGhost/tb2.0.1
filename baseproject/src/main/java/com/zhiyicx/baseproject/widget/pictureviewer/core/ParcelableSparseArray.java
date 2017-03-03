package com.zhiyicx.baseproject.widget.pictureviewer.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/3
 * @Contact master.jungle68@gmail.com
 */

public class ParcelableSparseArray<E> extends SparseArray<E> implements Parcelable {


    public ParcelableSparseArray(int capacity) {
        super(capacity);
    }

    public ParcelableSparseArray() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected ParcelableSparseArray(Parcel in) {
    }

    public static final Creator<ParcelableSparseArray> CREATOR = new Creator<ParcelableSparseArray>() {
        @Override
        public ParcelableSparseArray createFromParcel(Parcel source) {
            return new ParcelableSparseArray(source);
        }

        @Override
        public ParcelableSparseArray[] newArray(int size) {
            return new ParcelableSparseArray[size];
        }
    };
}