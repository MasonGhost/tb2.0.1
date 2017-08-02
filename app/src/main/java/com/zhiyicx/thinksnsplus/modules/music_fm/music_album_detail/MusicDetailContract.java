package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

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

        void albumHasBeDeleted();

        void noNetWork();

        MusicAlbumDetailsBean getCurrentAblum();

        MusicAlbumListBean getmMusicAlbumListBean();
    }

    interface Presenter extends IBaseTouristPresenter {
        void getMusicAblum(String id);

        void shareMusicAlbum(Bitmap bitmap);

        void handleCollect(boolean isUnCollected, String special_id);

        void getMusicDetails(String music_id);

        MusicAlbumDetailsBean getCacheAblumDetail(int id);

        void payNote(int position,int note);
    }

    interface Repository {
        Observable<MusicAlbumDetailsBean> getMusicAblum(String id);

        Observable<MusicDetaisBean> getMusicDetails(String music_id);

        void handleCollect(boolean isCollected, String special_id);

        void shareAblum(String special_id);

    }
}
