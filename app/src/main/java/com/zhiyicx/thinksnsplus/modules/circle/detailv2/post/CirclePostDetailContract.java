package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostDetailBean;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IBaseCircleRepository;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:18
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface CirclePostDetailContract {
    interface View extends ITSListView<CirclePostCommentBean, Presenter> {
    }

    interface Presenter extends ITSListPresenter<CirclePostCommentBean> {
    }

    interface Repository extends IBaseCircleRepository {
        Observable<CirclePostDetailBean> getPostDetail(long circleId,long postId);
    }
}
