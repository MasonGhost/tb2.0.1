package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleZipBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleDetailContract {
    interface View extends ITSListView<CirclePostListBean, Presenter> {
        Long getCircleId();

        void allDataReady(CircleZipBean circleZipBean);

        String getType();

        /**
         * 是否需要头信息
         *
         * @return true 需要
         */
        boolean isNeedHeaderInfo();

        BaseCircleRepository.CircleMinePostType getCircleMinePostType();

        String getSearchInput();

        CircleInfo getCircleInfo();

        void loadAllError();

        void updateCircleInfo(CircleInfo circleInfo);

        /**
         * 是否是圈外搜索，用于处理搜索记录
         *
         * @return
         */
        boolean isOutsideSerach();

        CircleZipBean getCircleZipBean();

        void scrollToTop();

        /**
         * 是否是我的帖子
         * @return
         */
        boolean isFromMine();
    }

    interface Presenter extends ITSListPresenter<CirclePostListBean> {
        void reSendComment(CirclePostCommentBean commentBean, long feed_id);

        int getCurrenPosiotnInDataList(Long id);

        void deleteComment(CirclePostListBean circlePostListBean, int dynamicPositon, Long id, int commentPosition);

        void sendComment(int currentPostion, long replyToUserId, String text);

        void deletePost(CirclePostListBean circlePostListBean, int position);

        void sharePost(CirclePostListBean circlePostListBean, Bitmap shareBitMap);

        void shareCircle(CircleInfo CircleInfo, Bitmap shareBitMap);

        void handleLike(boolean b, Long id, int dataPosition);

        void handleCollect(CirclePostListBean circlePostListBean);

        List<CircleSearchHistoryBean> getFirstShowHistory();

        void deleteSearchHistory(CircleSearchHistoryBean circleSearchHistoryBean);

        List<CircleSearchHistoryBean> getAllSearchHistory();

        void cleaerAllSearchHistory();

        void handleViewCount(Long id, int position);

        /**
         * 加入/退出圈子
         * @param circleInfo
         */
        void dealCircleJoinOrExit(CircleInfo circleInfo);

        /**
         * 圈主和管理员置顶帖子
         *
         * @param postId
         * @param day
         * @return
         */
        void stickTopPost(Long postId,int position,int day);

        /**
         * 圈主和管理员撤销置顶帖子
         *
         * @param postId
         * @return
         */
        void undoTopPost(Long postId,int position);

    }

}
