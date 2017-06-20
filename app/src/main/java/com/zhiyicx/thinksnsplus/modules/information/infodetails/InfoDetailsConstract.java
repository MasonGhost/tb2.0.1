package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;

import java.util.List;

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

        void shareInfo(Bitmap bitmap);

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
