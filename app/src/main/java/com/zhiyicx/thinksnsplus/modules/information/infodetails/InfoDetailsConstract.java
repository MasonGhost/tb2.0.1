package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IRewardRepository;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_COUNT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS_USER_LIST;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface InfoDetailsConstract {

    interface View extends ITSListView<InfoCommentListBean, Presenter> {
        Long getNewsId();

        void setCollect(boolean isCollected);

        void setDigg(boolean isDigged);

        InfoListDataBean getCurrentInfo();

        int getInfoType();

        void infoMationHasBeDeleted();

        void loadAllError();

        void updateReWardsView(RewardsCountBean rewardsCountBean, List<RewardsListBean> rewadslist);
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean> {

        void sendComment(int reply_id, String content);

        void shareInfo(Bitmap bitmap);

        void handleCollect(boolean isCollected, final String news_id);

        void deleteComment(InfoCommentListBean data);

        void handleLike(boolean isLiked, final String news_id);

        boolean isCollected();

        boolean isDiged();

        void reqReWardsData(int id);

    }

    interface Repository extends IRewardRepository {
        Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String news_id,
                                                                           Long max_id,
                                                                           Long limit);

        Observable<BaseJson<InfoWebBean>> getInfoWebContent(String news_id);

        void handleCollect(boolean isCollected, String news_id);

        void handleLike(boolean isLiked, final String news_id);

        void sendComment(String comment_content, Long news_id,
                         int reply_to_user_id, Long comment_mark);

        void deleteComment(int news_id, int comment_id);


    }
}
