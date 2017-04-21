package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicPlayContract {

    interface View extends IBaseView<Presenter> {
        MusicAlbumDetailsBean.MusicsBean getCurrentMusic();
    }

    interface Presenter extends IBasePresenter {
        void shareMusic();

        void handleLike(boolean isLiked, final String music_id);
    }

    interface Repository {
        void shareMusic(String music_id);
        void handleLike(boolean isLiked, String music_id);
    }
}
