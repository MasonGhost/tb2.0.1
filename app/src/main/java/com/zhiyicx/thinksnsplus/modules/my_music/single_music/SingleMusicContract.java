package com.zhiyicx.thinksnsplus.modules.my_music.single_music;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayContract;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface SingleMusicContract {
    interface View extends ITSListView<MusicDetaisBean,Presenter>{}
    interface Presenter extends ITSListPresenter<MusicDetaisBean>{}
}
