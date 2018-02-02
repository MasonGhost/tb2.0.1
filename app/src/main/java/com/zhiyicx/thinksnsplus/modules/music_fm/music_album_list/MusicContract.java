package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IMusicRepository;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description 音乐FM 契约类
 */
public interface MusicContract {

    interface View extends ITSListView<MusicAlbumListBean, Presenter> {

        boolean isCollection();
    }

    interface Presenter extends ITSListPresenter<MusicAlbumListBean> {
        void updateOneMusic(MusicAlbumListBean albumListBean);
        void payNote(int position, int note);
    }

    interface Repository extends IMusicRepository{

    }
}
