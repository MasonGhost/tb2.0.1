package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.AnswerDetailsConstract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/08/16/9:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerDetailsRepository extends BaseQARepository implements AnswerDetailsConstract.Repository {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public AnswerDetailsRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<List<AnswerCommentListBean>> getAnswerCommentList(long answer_id, long max_id, long limit) {
        return mQAClient.getAnswerCommentList(answer_id, max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<AnswerCommentListBean>, Observable<List<AnswerCommentListBean>>>() {
                    @Override
                    public Observable<List<AnswerCommentListBean>> call(List<AnswerCommentListBean> answerCommentListBeen) {
                        if (answerCommentListBeen.isEmpty()) {
                            return Observable.just(answerCommentListBeen);
                        } else {
                            final List<Object> user_ids = new ArrayList<>();
                            for (AnswerCommentListBean commentListBean : answerCommentListBeen) {
                                user_ids.add(commentListBean.getUser_id());
                                user_ids.add(commentListBean.getReply_user());
                            }
                            if (user_ids.isEmpty()) {
                                return Observable.just(answerCommentListBeen);
                            }

                            return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                                // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                        SparseArray<>();
                                for (UserInfoBean userInfoBean : userinfobeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                            .intValue(), userInfoBean);
                                }
                                for (AnswerCommentListBean commentListBean : answerCommentListBeen) {
                                    commentListBean.setFromUserInfoBean
                                            (userInfoBeanSparseArray.get(commentListBean
                                                    .getUser_id().intValue()));
                                    if (commentListBean.getReply_user() == 0) { // 如果
                                        // reply_user_id = 0 回复动态
                                        UserInfoBean userInfoBean = new UserInfoBean();
                                        userInfoBean.setUser_id(0L);
                                        commentListBean.setToUserInfoBean(userInfoBean);
                                    } else {
                                        commentListBean.setToUserInfoBean
                                                (userInfoBeanSparseArray.get(commentListBean.getReply_user().intValue()));
                                    }

                                }
                                mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                                return answerCommentListBeen;
                            });
                        }
                    }
                });
    }
}
