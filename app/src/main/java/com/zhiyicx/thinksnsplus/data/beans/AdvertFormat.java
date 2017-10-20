package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.ImageAdvert;

import java.io.Serializable;

public class AdvertFormat implements Serializable, Parcelable {
        private static final long serialVersionUID = 124L;
        private ImageAdvert image;
        private DynamicListAdvert analog;

        public ImageAdvert getImage() {
            return image;
        }

        public void setImage(ImageAdvert image) {
            this.image = image;
        }

        public DynamicListAdvert getAnalog() {
            return analog;
        }

        public void setAnalog(DynamicListAdvert analog) {
            this.analog = analog;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.image, flags);
            dest.writeParcelable(this.analog, flags);
        }

        public AdvertFormat() {
        }

        protected AdvertFormat(Parcel in) {
            this.image = in.readParcelable(ImageAdvert.class.getClassLoader());
            this.analog = in.readParcelable(DynamicListAdvert.class.getClassLoader());
        }

        public static final Creator<AdvertFormat> CREATOR = new Creator<AdvertFormat>() {
            @Override
            public AdvertFormat createFromParcel(Parcel source) {
                return new AdvertFormat(source);
            }

            @Override
            public AdvertFormat[] newArray(int size) {
                return new AdvertFormat[size];
            }
        };
    }