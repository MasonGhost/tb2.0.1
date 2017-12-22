package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.TopCircleJoinReQuestBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/07/05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewRepository implements MessageReviewContract.Repository {

    DynamicClient mDynamicClient;
    InfoMainClient mInfoMainClient;
    CircleClient mCircleClient;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MessageReviewRepository(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
        mInfoMainClient = serviceManager.getInfoMainClient();
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<List<TopDynamicCommentBean>> getDynamicReviewComment(int after) {
        return dealDynamicCommentBean(mDynamicClient.getDynamicReviewComment(after, TSListFragment.DEFAULT_PAGE_SIZE));
    }

    @Override
    public Observable<List<TopNewsCommentListBean>> getNewsReviewComment(int after) {
        return dealNewsCommentBean(mInfoMainClient.getNewsReviewComment(after, TSListFragment.DEFAULT_PAGE_SIZE));
    }

    @Override
    public Observable<List<TopPostCommentListBean>> getPostReviewComment(int after) {
        return mCircleClient.getPostReviewComment(after, TSListFragment.DEFAULT_ONE_PAGE_SIZE, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TopPostListBean>> getPostReview(Long circleId, int after) {
        return mCircleClient.getPostReview(after, TSListFragment.DEFAULT_ONE_PAGE_SIZE, circleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<TopCircleJoinReQuestBean>> getCircleJoinRequest(int after) {
        return mCircleClient.getCircleJoinRequest(after, TSListFragment.DEFAULT_ONE_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedTopComment(Long feed_id, int comment_id, int pinned_id) {
        return mDynamicClient.approvedDynamicTopComment(feed_id, comment_id, pinned_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseTopComment(int pinned_id) {
        return mDynamicClient.refuseDynamicTopComment(pinned_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedNewsTopComment(Long feed_id, int comment_id, int pinned_id) {
        return mInfoMainClient.approvedNewsTopComment(feed_id, comment_id, pinned_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseNewsTopComment(int news_id, Long comment_id, int pinned_id) {
        return mInfoMainClient.refuseNewsTopComment(news_id, comment_id, pinned_id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedPostTopComment(Integer comment_id) {
        return mCircleClient.approvedPostTopComment(comment_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refusePostTopComment(Integer comment_id) {
        return mCircleClient.refusePostTopComment(comment_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedPostTop(Long psotId) {
        return mCircleClient.approvedPostTop(psotId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refusePostTop(Long psotId) {
        return mCircleClient.refusePostTop(psotId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> refuseCircleJoin(BaseListBean result) {
        TopCircleJoinReQuestBean data = (TopCircleJoinReQuestBean) result;
        return mCircleClient.dealCircleJoin(TopCircleJoinReQuestBean.TOP_REFUSE, data.getGroup_id(), data.getId().intValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> approvedCircleJoin(Long circleId, int memberId) {
        return mCircleClient.dealCircleJoin(TopCircleJoinReQuestBean.TOP_SUCCESS, circleId.intValue(), memberId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> deleteTopComment(Long feed_id, int comment_id) {
        return null;
    }

    private Observable<List<TopDynamicCommentBean>> dealDynamicCommentBean(Observable<List<TopDynamicCommentBean>> data) {
        return data.flatMap(rechargeListBeen -> {
            final List<Object> user_ids = new ArrayList<>();
            for (TopDynamicCommentBean TopDynamicCommentBean : rechargeListBeen) {
                user_ids.add(TopDynamicCommentBean.getUser_id());
            }
            return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                for (UserInfoBean userInfoBean : userinfobeans) {
                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                }
                for (int i = 0; i < rechargeListBeen.size(); i++) {
                    rechargeListBeen.get(i).setUserInfoBean(userInfoBeanSparseArray.get(rechargeListBeen.get(i).getUser_id().intValue()));
                }
                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                return rechargeListBeen;
            });
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<TopNewsCommentListBean>> dealNewsCommentBean(Observable<List<TopNewsCommentListBean>> data) {
        return data.flatMap(new Func1<List<TopNewsCommentListBean>, Observable<List<TopNewsCommentListBean>>>() {
            @Override
            public Observable<List<TopNewsCommentListBean>> call(List<TopNewsCommentListBean> rechargeListBeen) {
                final List<Object> user_ids = new ArrayList<>();
                for (TopNewsCommentListBean TopDynamicCommentBean : rechargeListBeen) {
                    user_ids.add(TopDynamicCommentBean.getUser_id());
                }
                return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                    for (UserInfoBean userInfoBean : userinfobeans) {
                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                    }
                    for (int i = 0; i < rechargeListBeen.size(); i++) {
                        rechargeListBeen.get(i).setCommentUser(userInfoBeanSparseArray.get((int) rechargeListBeen.get(i).getUser_id()));
                        rechargeListBeen.get(i).setReplyUser(userInfoBeanSparseArray.get((int) rechargeListBeen.get(i).getTarget_user()));
                    }
                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                    return rechargeListBeen;
                });
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
