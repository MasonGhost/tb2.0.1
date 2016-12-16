package com.zhiyicx.common.base;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * @Describe  Json 解析基类
 * @Author Jungle68
 * @Date 2016/12/16
 * @Contact 335891510@qq.com
 */

public class BaseJson<T extends Parcelable> implements Parcelable {

    private int status;
    private String msg;
    private T data;

    public BaseJson() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.msg);
        dest.writeString(data.getClass().getName());
        dest.writeParcelable(this.data, flags);
    }

    protected BaseJson(Parcel in) {
        this.status = in.readInt();
        this.msg = in.readString();
        String dataName = in.readString();
        try {
            this.data = in.readParcelable(Class.forName(dataName).getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<BaseJson> CREATOR = new Creator<BaseJson>() {
        @Override
        public BaseJson createFromParcel(Parcel source) {
            return new BaseJson(source);
        }

        @Override
        public BaseJson[] newArray(int size) {
            return new BaseJson[size];
        }
    };
}
