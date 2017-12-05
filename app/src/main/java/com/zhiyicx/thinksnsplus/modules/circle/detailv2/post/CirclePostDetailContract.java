package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import java.util.List;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:18
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CirclePostDetailContract {
    interface View extends ITSListView<CirclePostCommentBean, Presenter> {
        void setCollect(boolean isCollected);

        void setDigg(boolean isDigged);

        long getPostId();

        long getCircleId();

        void allDataReady(CirclePostDetailBean data);
    }

    interface Presenter extends ITSListPresenter<CirclePostCommentBean> {
        void deleteComment(CirclePostCommentBean data);

        List<RealAdvertListBean> getAdvert();

        void handleLike(boolean b, String s);

        void shareInfo(Bitmap bitmap);

        void deletePost();

        void handleCollect(boolean b, String s);
    }

    interface Repository extends IBaseCircleRepository {
        Observable<CirclePostDetailBean> getPostDetail(long circleId,long postId);
        Observable<List<CirclePostCommentBean>> getPostComments(long postId, int limit, int after);
    }
}
