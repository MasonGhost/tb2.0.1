package com.zhiyicx.thinksnsplus.modules.music_fm.media_data;

import android.support.v4.media.MediaMetadataCompat;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Jliuer
 * @Date 2017/03/16
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicAblumInfo implements MusicProviderSource {

    private MusicAlbumDetailsBean mAlbumDetailsBean;

    public MusicAblumInfo(MusicAlbumDetailsBean albumDetailsBean) {
        mAlbumDetailsBean = albumDetailsBean;
    }

    private MusicAblumInfo() {
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
        LogUtils.d("Iterator<MediaMetadataCompat>"+mAlbumDetailsBean.getMusics());
        for (MusicAlbumDetailsBean.MusicsBean data:mAlbumDetailsBean.getMusics()){
            tracks.add(buildMusic(data));
        }
        return tracks.iterator();
    }

    private MediaMetadataCompat buildMusic(MusicAlbumDetailsBean.MusicsBean data) {
        MusicAlbumDetailsBean.MusicsBean.MusicInfoBean needData=data.getMusic_info();
        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        ""+needData.getId())
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE,
                        ""+needData.getStorage())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, ""+needData.getStorage())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, needData.getSinger()+"")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, needData.getLast_time()*1000)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, needData.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, ""+needData.getStorage())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, needData.getTitle())
                .build();
    }
}
