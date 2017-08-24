package com.zhiyicx.thinksnsplus.modules.my_music.single_music;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface SingleMusicContract {
    interface View extends ITSListView<Presenter,>{}
    interface Presenter extends ITSListPresenter<MusicDetaisBean>{}
    interface Repository{}
}
