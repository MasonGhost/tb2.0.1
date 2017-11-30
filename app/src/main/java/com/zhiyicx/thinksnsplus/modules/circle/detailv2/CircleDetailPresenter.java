package com.zhiyicx.thinksnsplus.modules.circle.detailv2;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.source.local.CirclePostCommentBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Jliuer
 * @Date 2017/11/22/14:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleDetailPresenter extends AppBasePresenter<CircleDetailContract.Repository, CircleDetailContract.View>
        implements CircleDetailContract.Presenter {

    @Inject
    CirclePostCommentBeanGreenDaoImpl mCirclePostCommentBeanGreenDao;

    @Inject
    public CircleDetailPresenter(CircleDetailContract.Repository repository, CircleDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (!isLoadMore) {
            Observable.zip(mRepository.getCircleInfoDetail(mRootView.getCircleId()), mRepository.getPostListFromCircle(mRootView.getCircleId(), maxId),
                    CircleZipBean::new)
                    .map(circleZipBean -> {
                        List<CirclePostListBean> data = circleZipBean.getCirclePostListBeanList();
                        for (int i = 0; i < data.size(); i++) { // 把自己发的评论加到评论列表的前面
                            List<CirclePostCommentBean> circlePostCommentBeans = mCirclePostCommentBeanGreenDao.getMySendingComment(data.get(i).getMaxId().intValue());
                            if (!circlePostCommentBeans.isEmpty()) {
                                circlePostCommentBeans.addAll(data.get(i).getComments());
                                data.get(i).getComments().clear();
                                data.get(i).getComments().addAll(circlePostCommentBeans);
                            }
                        }
                        return circleZipBean;
                    })
                    .subscribe(new BaseSubscribeForV2<CircleZipBean>() {
                        @Override
                        protected void onSuccess(CircleZipBean data) {
                            mRootView.onNetResponseSuccess(data.getCirclePostListBeanList(),isLoadMore);
                            mRootView.allDataReady(data);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                        }
                    });
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostListBean> data, boolean isLoadMore) {
        return false;
    }
}
