package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
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

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public MessageReviewRepository(ServiceManager serviceManager) {
        mDynamicClient=serviceManager.getDynamicClient();
    }

    @Override
    public Observable<List<TopDynamicCommentBean>> getReviewComment(int after) {
        return dealCommentBean(mDynamicClient.getReviewComment(after, TSListFragment.DEFAULT_PAGE_SIZE));
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
        return mDynamicClient.refuseTopComment(pinned_id).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2> deleteTopComment(Long feed_id, int comment_id) {
        return null;
    }
    
    private Observable<List<TopDynamicCommentBean>> dealCommentBean(Observable<List<TopDynamicCommentBean>> data){
        return data.flatMap(new Func1<List<TopDynamicCommentBean>, Observable<List<TopDynamicCommentBean>>>() {
            @Override
            public Observable<List<TopDynamicCommentBean>> call(List<TopDynamicCommentBean> rechargeListBeen) {
                final List<Object> user_ids = new ArrayList<>();
                for (TopDynamicCommentBean TopDynamicCommentBean : rechargeListBeen) {
                    user_ids.add(TopDynamicCommentBean.getUser_id());
                }
                return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                    for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                    }
                    for (int i = 0; i < rechargeListBeen.size(); i++) {
                        rechargeListBeen.get(i).setUserInfoBean(userInfoBeanSparseArray.get(rechargeListBeen.get(i).getUser_id().intValue()));
                    }
                    mUserInfoBeanGreenDao.insertOrReplace(userinfobeans.getData());
                    return rechargeListBeen;
                });
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
