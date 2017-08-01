package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_REWARDS;
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
    }

    interface Presenter extends ITSListPresenter<InfoCommentListBean> {

        void sendComment(int reply_id, String content);

        void shareInfo(Bitmap bitmap);

        void handleCollect(boolean isCollected, final String news_id);

        void deleteComment(InfoCommentListBean data);

        void handleLike(boolean isLiked, final String news_id);

        boolean isCollected();

        boolean isDiged();
    }

    interface Repository {
        Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String news_id,
                                                                           Long max_id,
                                                                           Long limit);

        Observable<BaseJson<InfoWebBean>> getInfoWebContent(String news_id);

        void handleCollect(boolean isCollected, String news_id);

        void handleLike(boolean isLiked, final String news_id);

        void sendComment(String comment_content, Long news_id,
                         int reply_to_user_id, Long comment_mark);

        void deleteComment(int news_id, int comment_id);


        /**
         * 对一条资讯打赏
         *
         * @param news_id 咨询 id
         * @param amount  打赏金额
         * @return
         */
        Observable<Object> rewardsInfo(long news_id, float amount);


        /**
         * 资讯打赏列表
         *
         * @param news_id    咨询 id
         * @param limit      列表返回数据条数
         * @param since      翻页标识 时间排序时为数据 id 金额排序时为打赏金额 amount
         * @param order      翻页标识 排序 正序-asc 倒序 desc
         * @param order_type 排序规则 date-按时间 amount-按金额
         * @return
         */
        Observable<List<RewardsListBean>> rewardsInfoList(long news_id, Integer limit, Integer since, String order, String order_type);

    }
}
