package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;

import java.util.List;
import java.util.Map;

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
    }

    interface Presenter extends IBasePresenter {
        void getMusicAblum(String id);
    }

    interface Repository {
        Observable<BaseJson<MusicAlbumDetailsBean>> getMusicAblum(String id);
    }
}
