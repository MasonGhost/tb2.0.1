package com.zhiyicx.thinksnsplus.modules.music_fm.music_album;

import android.support.v4.media.MediaBrowserCompat;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;

import java.util.Map;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 音乐FM 契约类
 */

public interface MusicContract {

    interface View extends IBaseView<Presenter> {
        void setMediaBrowserCompat(MediaBrowserCompat mediaBrowserCompat);
    }

    interface Presenter extends ITSListPresenter<MusicListBean> {
        void getMusicList();
    }

    interface Repository {
        Observable<BaseJson<MusicListBean>> getMusicList(Map map);
    }
}
