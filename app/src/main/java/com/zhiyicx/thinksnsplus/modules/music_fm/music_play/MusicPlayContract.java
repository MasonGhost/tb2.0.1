package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;

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
        void digMusic(boolean b);
        void cancleDigMusic(boolean b);
    }

    interface Presenter extends IBasePresenter {
        void digMusic(String music_id);
        void cancleDigMusic(String music_id);
    }

    interface Repository {
        Observable<BaseJson<Integer>> doDigg(String music_id);
        Observable<BaseJson<Integer>> cancleDigg(String music_id);
    }
}
