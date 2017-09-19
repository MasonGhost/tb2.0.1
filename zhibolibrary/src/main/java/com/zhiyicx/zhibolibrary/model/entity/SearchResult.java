package com.zhiyicx.zhibolibrary.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class SearchResult implements Serializable, Parcelable {

    public UserInfo user;// 所属用户UID
    public Stream stream;
    public Video video;
    public ImInfo im;
    public int is_follow;//是否关注

    public SearchResult() {
    }
    public SearchResult(String uid) {
        this.user=new UserInfo();
        user.uid=uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.user);
        dest.writeSerializable(this.stream);
        dest.writeSerializable(this.video);
        dest.writeSerializable(this.im);
        dest.writeInt(this.is_follow);
    }

    protected SearchResult(Parcel in) {
        this.user = (UserInfo) in.readSerializable();
        this.stream = (Stream) in.readSerializable();
        this.video = (Video) in.readSerializable();
        this.im = (ImInfo) in.readSerializable();
        this.is_follow = in.readInt();
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
