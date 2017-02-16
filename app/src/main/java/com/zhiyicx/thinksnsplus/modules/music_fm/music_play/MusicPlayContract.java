package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;

import java.util.Map;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicPlayContract {
    interface View extends IBaseView<Presenter> {

    }

    interface Presenter extends IBasePresenter {

    }

    interface Repository {
        Observable<BaseJson<MusicListBean>> getMusicList(Map map);
    }
}
