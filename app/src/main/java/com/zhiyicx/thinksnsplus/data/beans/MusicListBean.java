package com.zhiyicx.thinksnsplus.data.beans;


import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;
import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListBean implements Parcelable {

    private List<Music> music;
    private String descrip;

    public static class Music implements Parcelable {

        private String title;
        private String album;
        private String artist;
        private String genre;
        private String source;
        private String image;
        private int trackNumber;
        private int totalTrackCount;
        private int duration;

        public int getTrackNumber() {
            return trackNumber;
        }

        public void setTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
        }

        public int getTotalTrackCount() {
            return totalTrackCount;
        }

        public void setTotalTrackCount(int totalTrackCount) {
            this.totalTrackCount = totalTrackCount;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
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
            dest.writeString(this.title);
            dest.writeString(this.album);
            dest.writeString(this.artist);
            dest.writeString(this.genre);
            dest.writeString(this.source);
            dest.writeString(this.image);
            dest.writeInt(this.trackNumber);
            dest.writeInt(this.totalTrackCount);
            dest.writeInt(this.duration);
        }

        public Music() {
        }

        protected Music(Parcel in) {
            this.title = in.readString();
            this.album = in.readString();
            this.artist = in.readString();
            this.genre = in.readString();
            this.source = in.readString();
            this.image = in.readString();
            this.trackNumber = in.readInt();
            this.totalTrackCount = in.readInt();
            this.duration = in.readInt();
        }

        public static final Creator<Music> CREATOR = new Creator<Music>() {
            @Override
            public Music createFromParcel(Parcel source) {
                return new Music(source);
            }

            @Override
            public Music[] newArray(int size) {
                return new Music[size];
            }
        };
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public List<Music> getMusic() {
        return music;
    }

    public void setMusic(List<Music> music) {
        this.music = music;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.music);
        dest.writeString(this.descrip);
    }

    public MusicListBean() {
    }

    protected MusicListBean(Parcel in) {
        this.music = new ArrayList<Music>();
        in.readList(this.music, Music.class.getClassLoader());
        this.descrip = in.readString();
    }

    public static final Parcelable.Creator<MusicListBean> CREATOR = new Parcelable
            .Creator<MusicListBean>() {
        @Override
        public MusicListBean createFromParcel(Parcel source) {
            return new MusicListBean(source);
        }

        @Override
        public MusicListBean[] newArray(int size) {
            return new MusicListBean[size];
        }
    };
}
