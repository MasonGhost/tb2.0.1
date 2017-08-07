package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ImageAdvert implements Serializable ,Parcelable{
    private static final long serialVersionUID = 124L;
    private String link;
    private String image;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.link);
        dest.writeString(this.image);
    }

    public ImageAdvert() {
    }

    protected ImageAdvert(Parcel in) {
        this.link = in.readString();
        this.image = in.readString();
    }

    public static final Creator<ImageAdvert> CREATOR = new Creator<ImageAdvert>() {
        @Override
        public ImageAdvert createFromParcel(Parcel source) {
            return new ImageAdvert(source);
        }

        @Override
        public ImageAdvert[] newArray(int size) {
            return new ImageAdvert[size];
        }
    };
}