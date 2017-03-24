package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;

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
        String getFeedId();
        void setCollect(boolean isCollected);
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean>{
        void shareInfo();

        void handleCollect(boolean isCollected, final String news_id);
    }

    interface Repository{
        Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String feed_id,
                                                                           Long max_id,
                                                                           Long limit);

        void handleCollect(boolean isCollected, String news_id);

        void sendComment(String comment_content,Long news_id,
                         int reply_to_user_id,Long comment_mark);
    }
}
