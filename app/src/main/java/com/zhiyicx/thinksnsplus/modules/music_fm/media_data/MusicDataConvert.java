package com.zhiyicx.thinksnsplus.modules.music_fm.media_data;

import android.support.v4.media.MediaMetadataCompat;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Jliuer
 * @Date 2017/03/16
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressWarnings("ALL")
public class MusicDataConvert implements MusicProviderSource {

    private MusicAlbumDetailsBean mAlbumDetailsBean;
    public static final String METADATA_KEY_GENRE = "_zhiyicx_";

    public MusicDataConvert(MusicAlbumDetailsBean albumDetailsBean) {
        mAlbumDetailsBean = albumDetailsBean;
    }

    private MusicDataConvert() {
    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
        if (mAlbumDetailsBean == null) {
            return tracks.iterator();
        }
        for (MusicDetaisBean data : mAlbumDetailsBean.getMusics()) {
            // 跳过收费
            if (data.getStorage().getAmount() == 0 || data.getStorage().isPaid()){
                LogUtils.d("Iterator<MediaMetadataCompat> :::" + data.getTitle());
                tracks.add(buildMusic(data));
            }
        }
        return tracks.iterator();
    }

    private MediaMetadataCompat buildMusic(MusicDetaisBean needData) {
        String musicUrl = String.format(ApiConfig.APP_DOMAIN+ApiConfig.MUSIC_PATH,
                needData.getStorage().getId());

        String imageUrl = String.format(ImageUtils.imagePathConvertV2(needData.getSinger().getCover().getId(), 50, 50, 100));
        LogUtils.d("buildMusic--needData.getId:::" + needData.getId());
        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        "" + needData.getId())

                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE,
                        "" + musicUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, needData.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, needData.isHas_like() + "")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, needData.getSinger().getName())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, needData.getLast_time() * 1000)
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR,
                        (needData.getStorage().getAmount() == 0 || needData.getStorage().isPaid()) ? 1L : -1L)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, needData.getLast_time() * 1000)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, METADATA_KEY_GENRE)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, imageUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, needData.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, musicUrl)
                .build();
    }
}
