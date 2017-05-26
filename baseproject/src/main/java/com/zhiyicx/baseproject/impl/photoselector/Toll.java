package com.zhiyicx.baseproject.impl.photoselector;

import android.os.Parcel;
import android.os.Parcelable;

public class Toll implements Parcelable {
        int toll_type;
        float toll_money;

        public Toll(int toll_type, float toll_money) {
            this.toll_type = toll_type;
            this.toll_money = toll_money;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.toll_type);
            dest.writeFloat(this.toll_money);
        }

        protected Toll(Parcel in) {
            this.toll_type = in.readInt();
            this.toll_money = in.readFloat();
        }

        public static final Creator<Toll> CREATOR = new Creator<Toll>() {
            @Override
            public Toll createFromParcel(Parcel source) {
                return new Toll(source);
            }

            @Override
            public Toll[] newArray(int size) {
                return new Toll[size];
            }
        };
    }