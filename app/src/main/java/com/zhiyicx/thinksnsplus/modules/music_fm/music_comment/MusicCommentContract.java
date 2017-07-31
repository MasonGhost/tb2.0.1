package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;

import java.util.List;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface MusicCommentContract {

    interface View extends ITSListView<CommentedBean,Presenter>{
        String getType();
        long getCommentId();
        void setHeaderInfo(MusicCommentHeader.HeaderInfo headerInfo);
    }

    interface Presenter extends ITSListPresenter<CommentedBean>{
        void requestNetData(String music_id,Long maxId, boolean isLoadMore);
        void sendComment(long reply_id,String content);

        void deleteComment(CommentedBean data);
        void reSendComment(CommentedBean data);
        void getMusicDetails(String music_id);
        void getMusicAblum(String id);
    }

    interface Repository{
        Observable<List<CommentedBean>> getMusicCommentList(String music_id,
                                                                      long max_id);

        Observable<List<CommentedBean>> getAblumCommentList(String special_id,
                                                                             Long max_id);
        void sendComment(int music_id,int reply_id, String content,String path,Long comment_mark,BackgroundTaskHandler.OnNetResponseCallBack callBack);

        void deleteComment(int music_id,int comment_id);

        Observable<MusicDetaisBean> getMusicDetails(String music_id);

        Observable<MusicAlbumDetailsBean> getMusicAblum(String id);
    }
}
