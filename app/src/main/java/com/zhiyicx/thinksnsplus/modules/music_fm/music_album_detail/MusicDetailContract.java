package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.Path;
import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicDetailContract {

    interface View extends IBaseView<Presenter> {
        void setMusicAblum(MusicAlbumDetailsBean musicAblum);

        void setCollect(boolean isCollected);

        MusicAlbumDetailsBean getCurrentAblum();

        MusicAlbumListBean getmMusicAlbumListBean();
    }

    interface Presenter extends IBasePresenter {
        void getMusicAblum(String id);

        void shareMusicAlbum();

        void handleCollect(boolean isUnCollected, String special_id);

        void getMusicDetails(String music_id);
    }

    interface Repository {
        Observable<BaseJson<MusicAlbumDetailsBean>> getMusicAblum(String id);

        Observable<BaseJson<MusicDetaisBean>> getMusicDetails(String music_id);

        void handleCollect(boolean isCollected, String special_id);

        void shareAblum(String special_id);

    }
}
