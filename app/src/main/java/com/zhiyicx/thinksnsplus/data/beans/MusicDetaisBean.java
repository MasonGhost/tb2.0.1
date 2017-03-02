package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Author Jliuer
 * @Date 2017/03/02
 * @Email Jliuer@aliyun.com
 * @Description 音乐详情
 */
public class MusicDetaisBean implements Parcelable {

    /**
     * status : true
     * code : 0
     * message : 操作成功
     * data : {"is_collect":0,"comment_count":9999,"last_time":300,
     * "lyric":"如果这都不算爱如果这都不算爱如果这都不算爱如果这都不算爱如果这都不算爱"}
     */

    private boolean status;
    private int code;
    private String message;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * is_collect : 0
         * comment_count : 9999
         * last_time : 300
         * lyric : 如果这都不算爱如果这都不算爱如果这都不算爱如果这都不算爱如果这都不算爱
         */

        private int is_collect;
        private int comment_count;
        private int last_time;
        private String lyric;

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getLast_time() {
            return last_time;
        }

        public void setLast_time(int last_time) {
            this.last_time = last_time;
        }

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.is_collect);
            dest.writeInt(this.comment_count);
            dest.writeInt(this.last_time);
            dest.writeString(this.lyric);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.is_collect = in.readInt();
            this.comment_count = in.readInt();
            this.last_time = in.readInt();
            this.lyric = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeParcelable(this.data, flags);
    }

    public MusicDetaisBean() {
    }

    protected MusicDetaisBean(Parcel in) {
        this.status = in.readByte() != 0;
        this.code = in.readInt();
        this.message = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<MusicDetaisBean> CREATOR = new Parcelable
            .Creator<MusicDetaisBean>() {
        @Override
        public MusicDetaisBean createFromParcel(Parcel source) {
            return new MusicDetaisBean(source);
        }

        @Override
        public MusicDetaisBean[] newArray(int size) {
            return new MusicDetaisBean[size];
        }
    };
}
