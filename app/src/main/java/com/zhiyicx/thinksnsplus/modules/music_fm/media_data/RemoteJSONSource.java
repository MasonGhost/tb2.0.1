package com.zhiyicx.thinksnsplus.modules.music_fm.media_data;

import android.support.v4.media.MediaMetadataCompat;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @Author Jliuer
 * @Date 2017/2/19/13:05
 * @Email Jliuer@aliyun.com
 * @Description 测试数据。。。
 */
public class RemoteJSONSource implements MusicProviderSource {


    protected static final String CATALOG_URL =
            "http://storage.googleapis.com/automotive-media/music.json";

    private static final String JSON_MUSIC = "music";
    private static final String JSON_TITLE = "title";
    private static final String JSON_ALBUM = "album";
    private static final String JSON_ARTIST = "artist";
    private static final String JSON_GENRE = "genre";
    private static final String JSON_SOURCE = "source";
    private static final String JSON_IMAGE = "image";
    private static final String JSON_TRACK_NUMBER = "trackNumber";
    private static final String JSON_TOTAL_TRACK_COUNT = "totalTrackCount";
    private static final String JSON_DURATION = "duration";

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        ArrayList<MediaMetadataCompat> tracks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tracks.add(buildForTest(i));
        }
        return tracks.iterator();

//        try {
//            int slashPos = CATALOG_URL.lastIndexOf('/');
//            String path = CATALOG_URL.substring(0, slashPos + 1);
//            JSONObject jsonObj = fetchJSONFromUrl(CATALOG_URL);
//
//            if (jsonObj != null) {
//                JSONArray jsonTracks = jsonObj.getJSONArray(JSON_MUSIC);
//
//                if (jsonTracks != null) {
//                    for (int j = 0; j < jsonTracks.length(); j++) {
//                        tracks.add(buildFromJSON(jsonTracks.getJSONObject(j), path));
//                    }
//                }
//            }
//
//            return tracks.iterator();
//        } catch (JSONException e) {
//            throw new RuntimeException("Could not retrieve music list", e);
//        }
    }

    private MediaMetadataCompat buildForTest(int i) {
        String testSongUrl_ = "http://hd.xiaotimi.com/2016/myxc/ok1/GKL.mp4?#.mp3";
        String testSongUrl = "http://demo.thinksns" +
                ".com/ts4/data/upload/2017/0224/16/58aff53763a55b790541.mp3";
        String testIcongUrl = "https://ss2.bdstatic" +
                ".com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962,2666166364&fm=21&gp=0.jpg";
        if (i % 2 == 0) {
            testSongUrl = testSongUrl_;
        }
        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, i + "")
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, testSongUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, testIcongUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "_tym")
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 60000 * 4 + 2333)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, "tym_")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, testIcongUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "tym")
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 6000)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 60000)
                .build();
    }

    private MediaMetadataCompat buildFromJSON(JSONObject json, String basePath) throws
            JSONException {
        String title = json.getString(JSON_TITLE);
        String album = json.getString(JSON_ALBUM);
        String artist = json.getString(JSON_ARTIST);
        String genre = json.getString(JSON_GENRE);
        String source = json.getString(JSON_SOURCE);
        String iconUrl = json.getString(JSON_IMAGE);
        int trackNumber = json.getInt(JSON_TRACK_NUMBER);
        int totalTrackCount = json.getInt(JSON_TOTAL_TRACK_COUNT);
        int duration = json.getInt(JSON_DURATION) * 1000; // ms

        if (!source.startsWith("http")) {
            source = basePath + source;
        }
        if (!iconUrl.startsWith("http")) {
            iconUrl = basePath + iconUrl;
        }

        String id = String.valueOf(source.hashCode());

        //noinspection ResourceType
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(MusicProviderSource.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
                .build();
    }

    private JSONObject fetchJSONFromUrl(String urlString) throws JSONException {
        BufferedReader reader = null;
        try {
            URLConnection urlConnection = new URL(urlString).openConnection();
            reader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "iso-8859-1"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new JSONObject(sb.toString());
        } catch (JSONException e) {
            throw e;
        } catch (Exception e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
