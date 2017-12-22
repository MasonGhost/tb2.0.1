package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleCommentZip;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 2017/12/05/10:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailRepository extends BaseCircleRepository implements CirclePostDetailContract.Repository {

    @Inject
    public CirclePostDetailRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<CirclePostListBean> getPostDetail(long circleId, long postId) {
        return mCircleClient.getPostDetail(circleId,postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CirclePostCommentBean>> getPostComments(long postId, int limit, int after) {
        return getPostCommentList(postId, (long) after).flatMap(circleCommentZip -> {
            circleCommentZip.getPinneds().addAll(circleCommentZip.getComments());
            return Observable.just(circleCommentZip.getPinneds())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        });
    }

    public Observable<CircleCommentZip> getPostCommentList(long postId, Long maxId) {
        return mCircleClient.getPostComments(postId, TSListFragment.DEFAULT_ONE_PAGE_SIZE, maxId.intValue())
                .subscribeOn(Schedulers.io())
                .flatMap(circleCommentZip -> {
                    final List<Object> user_ids = new ArrayList<>();

                    if (circleCommentZip.getPinneds() != null) {
                        for (CirclePostCommentBean commentListBean : circleCommentZip.getPinneds()) {
                            user_ids.add(commentListBean.getUser_id());
                            commentListBean.setPinned(true);
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTo_user_id());
                        }
                    }
                    if (circleCommentZip.getComments() != null) {
                        for (CirclePostCommentBean commentListBean : circleCommentZip.getComments()) {
                            user_ids.add(commentListBean.getUser_id());
                            commentListBean.setPinned(false);
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTo_user_id());
                        }
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(circleCommentZip);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userInfoBeanList -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                        SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeanList) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                            .intValue(), userInfoBean);
                                }
                                dealCommentData(circleCommentZip.getPinneds(), userInfoBeanSparseArray);
                                dealCommentData(circleCommentZip.getComments(), userInfoBeanSparseArray);
                                mUserInfoBeanGreenDao.insertOrReplace(userInfoBeanList);
                                return circleCommentZip;
                            });
                });
    }

    private void dealCommentData(List<CirclePostCommentBean> list, SparseArray<UserInfoBean> userInfoBeanSparseArray) {
        if (list != null) {
            for (CirclePostCommentBean commentListBean : list) {
                commentListBean.setCommentUser(userInfoBeanSparseArray.get((int) commentListBean.getUser_id()));
                if (commentListBean.getReply_to_user_id() == 0) {
                    // reply_user_id = 0 回复动态
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(0L);
                    commentListBean.setReplyUser(userInfoBean);
                } else {
                    commentListBean.setReplyUser(userInfoBeanSparseArray.get((int) commentListBean.getReply_to_user_id()));
                }
            }
        }
    }
}
