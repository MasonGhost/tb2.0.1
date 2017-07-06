package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewRepository implements MessageReviewContract.Repository {

    DynamicClient mDynamicClient;

    @Inject
    public MessageReviewRepository(ServiceManager serviceManager) {
        mDynamicClient=serviceManager.getDynamicClient();
    }

    @Override
    public Observable<List<TopDynamicCommentBean>> getReviewComment(int after) {
        return mDynamicClient.getReviewComment(after, TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedTopComment(Long feed_id, int comment_id, int pinned_id) {
        return mDynamicClient.approvedTopComment(feed_id,comment_id,pinned_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseTopComment(int pinned_id) {
        return null;
    }

    @Override
    public Observable<BaseJsonV2> deleteTopComment(Long feed_id, int comment_id) {
        return null;
    }
}
