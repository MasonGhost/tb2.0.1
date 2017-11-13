package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IMusicRepository;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicPlayContract {

    interface View extends IBaseView<Presenter> {
        MusicDetaisBean getCurrentMusic();
        List<MusicDetaisBean> getListDatas();

        void refreshData(int position);
        MusicAlbumDetailsBean getCurrentAblum();
    }

    interface Presenter extends IBaseTouristPresenter {
        void shareMusic(Bitmap bitmap);
        void payNote(int position, int note);
        void handleLike(boolean isLiked, final String music_id);
    }

    interface Repository extends IMusicRepository{

    }
}
