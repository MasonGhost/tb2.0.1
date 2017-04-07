package com.zhiyicx.thinksnsplus.modules.music_fm.media_data;

import android.support.v4.media.MediaMetadataCompat;

import com.zhiyicx.baseproject.config.ApiConfig;
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
    public static final String METADATA_KEY_GENRE="_tym_";

    public MusicAblumInfo(MusicAlbumDetailsBean albumDetailsBean) {
        mAlbumDetailsBean = albumDetailsBean;
    }

    private MusicAblumInfo() {
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
        for (MusicAlbumDetailsBean.MusicsBean data:mAlbumDetailsBean.getMusics()){
            tracks.add(buildMusic(data));
        }
        return tracks.iterator();
    }

    private MediaMetadataCompat buildMusic(MusicAlbumDetailsBean.MusicsBean data) {
        MusicAlbumDetailsBean.MusicsBean.MusicInfoBean needData=data.getMusic_info();
        String musicUrl = String.format(ApiConfig.NO_PROCESS_IMAGE_PATH,
                needData.getStorage());

        String imageUrl = String.format(ApiConfig.NO_PROCESS_IMAGE_PATH,
                needData.getSinger().getCover().getId(),50);
        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        ""+needData.getId())
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE,
                        ""+musicUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, needData.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, needData.getSinger().getName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, needData.getLast_time()*1000)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, METADATA_KEY_GENRE)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, imageUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, needData.getTitle())
                .build();
    }
}
