package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicCommentContract {

    interface View extends ITSListView<MusicCommentListBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<MusicCommentListBean>{
        void requestNetData(String music_id,Long maxId, boolean isLoadMore);
    }

    interface Repository{
        Observable<BaseJson<List<MusicCommentListBean>>> getMusicCommentList(String music_id,
                                                                             long max_id);
    }
}
