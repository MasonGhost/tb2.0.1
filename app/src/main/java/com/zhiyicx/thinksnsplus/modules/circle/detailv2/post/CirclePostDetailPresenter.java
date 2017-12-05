package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.graphics.Bitmap;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailPresenter extends AppBasePresenter<CirclePostDetailContract.Repository, CirclePostDetailContract.View>
        implements CirclePostDetailContract.Presenter {

    @Inject
    public CirclePostDetailPresenter(CirclePostDetailContract.Repository repository, CirclePostDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (!isLoadMore) {
            Subscription subscription = Observable.zip(mRepository.getPostComments(mRootView.getPostId(), 0, maxId.intValue()),
                    mRepository.getPostDetail(mRootView.getCircleId(), mRootView.getPostId()),
                    mRepository.getPostDigList(mRootView.getPostId(), 0, maxId.intValue()),
                    (circlePostCommentBeans, circlePostDetailBean, postDigListBeans) -> {
                        circlePostDetailBean.setComments(circlePostCommentBeans);
                        circlePostDetailBean.setDigs(postDigListBeans);
                        return circlePostDetailBean;
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscribeForV2<CirclePostDetailBean>() {
                        @Override
                        protected void onSuccess(CirclePostDetailBean data) {
                            mRootView.allDataReady(data);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });

            addSubscrebe(subscription);
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void deleteComment(CirclePostCommentBean data) {

    }

    @Override
    public List<RealAdvertListBean> getAdvert() {
        return null;
    }

    @Override
    public void handleLike(boolean b, String s) {

    }

    @Override
    public void shareInfo(Bitmap bitmap) {

    }

    @Override
    public void deletePost() {

    }

    @Override
    public void handleCollect(boolean b, String s) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostCommentBean> data, boolean isLoadMore) {
        return false;
    }
}
