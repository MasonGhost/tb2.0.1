package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_MUSIC_COLLECT_ABLUM_LIST;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IMusicRepository {

    void handleCollect(boolean isCollected, String special_id);

    void shareAblum(String special_id);

    Observable<List<MusicAlbumListBean>> getMusicAblumList(long max_id);

    List<MusicAlbumListBean> getMusicAlbumFromCache(long maxId);

    // 获取收藏专辑列表
    Observable<List<MusicAlbumListBean>> getCollectMusicList(Long max_id, Long limit);

    List<MusicAlbumListBean> getMusicCollectAlbumFromCache(long maxId);

    Observable<List<MusicCommentListBean>> getMusicCommentList(String music_id,
                                                               long max_id);

    Observable<List<MusicCommentListBean>> getAblumCommentList(String special_id,
                                                               Long max_id);

    void sendComment(int music_id, int reply_id, String content, String path, Long comment_mark, BackgroundTaskHandler.OnNetResponseCallBack
            callBack);

    void deleteComment(int music_id, int comment_id);

    Observable<MusicDetaisBean> getMusicDetails(String music_id);

    Observable<MusicAlbumDetailsBean> getMusicAblum(String id);

    void shareMusic(String music_id);

    void handleLike(boolean isLiked, String music_id);

    Observable<List<MusicDetaisBean>> getMyPaidsMusicList(long max_id);

    Observable<List<MusicAlbumListBean>> getMyPaidsMusicAlbumList(long max_id);

}
