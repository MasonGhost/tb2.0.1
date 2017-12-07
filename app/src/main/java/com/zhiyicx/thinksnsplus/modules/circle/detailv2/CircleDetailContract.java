package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDetail;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CircleDetailContract {
    interface View extends ITSListView<CirclePostListBean, Presenter> {
        long getCircleId();
        void allDataReady(CircleZipBean circleZipBean);
    }

    interface Presenter extends ITSListPresenter<CirclePostListBean> {
        void reSendComment(CirclePostCommentBean commentBean, long feed_id);

        int getCurrenPosiotnInDataList(Long id);

        void deleteComment(CirclePostListBean circlePostListBean, int dynamicPositon, Long id, int commentPosition);

        void sendComment(int currentPostion, long replyToUserId, String text);

        void deletePost(CirclePostListBean circlePostListBean, int position);

        void sharePost(CirclePostListBean circlePostListBean, Bitmap shareBitMap);

        void handleLike(boolean b,Long id, int dataPosition);

        void handleCollect(CirclePostListBean circlePostListBean);

        void handleViewCount(Long id, int position);
    }

    interface Repository extends IBaseCircleRepository {
        Observable<CircleInfoDetail> getCircleInfoDetail(long circleId);
    }
}
