package com.zhiyicx.thinksnsplus.modules.music_fm.media_data;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

/**
 * @Author Jliuer
 * @Date 2017/2/21/14:21
 * @Email Jliuer@aliyun.com
 * @Description 音乐资源获取
 */
public interface MusicProviderSource {
    String CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__";
    Iterator<MediaMetadataCompat> iterator();
}
