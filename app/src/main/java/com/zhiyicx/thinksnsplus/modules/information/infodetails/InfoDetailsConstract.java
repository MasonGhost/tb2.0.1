package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.info.InfoWebBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoDetailsConstract {

    interface View extends ITSListView<InfoCommentListBean,Presenter> {
        Long getNewsId();
        void setCollect(boolean isCollected);
        void setDigg(boolean isDigged);
        InfoListDataBean getCurrentInfo();
        int getInfoType();
        void infoMationHasBeDeleted();
        void loadAllError();
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean>{

        void sendComment(int reply_id,String content);

        void shareInfo();

        void handleCollect(boolean isCollected, final String news_id);

        void deleteComment(InfoCommentListBean data);

        void handleLike(boolean isLiked, final String news_id);

        boolean isCollected();

        boolean isDiged();
    }

    interface Repository{
        Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String news_id,
                                                                           Long max_id,
                                                                           Long limit);

        Observable<BaseJson<InfoWebBean>> getInfoWebContent(String news_id);

        void handleCollect(boolean isCollected, String news_id);

        void handleLike(boolean isLiked, final String news_id);

        void sendComment(String comment_content,Long news_id,
                         int reply_to_user_id,Long comment_mark);

        void deleteComment(int news_id,int comment_id);
    }
}
