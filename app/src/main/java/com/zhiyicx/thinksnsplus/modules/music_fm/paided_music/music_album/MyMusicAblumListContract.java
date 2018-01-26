package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IMusicRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MyMusicAblumListContract {
    interface View extends ITSListView<MusicAlbumListBean,Presenter> {}
    interface Presenter extends ITSListPresenter<MusicAlbumListBean> {
        void updateOneMusic(MusicAlbumListBean albumListBean);
    }
}
