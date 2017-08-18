package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment.QuestionCommentContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/18
 * @contact email:648129313@qq.com
 */

public class QuestionCommentRepository extends BaseQARepository implements QuestionCommentContract.Repository{

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public QuestionCommentRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<List<QuestionCommentBean>> getQuestionCommentList(Long question_Id, Long max_id) {
        return mQAClient.getQuestionCommentList(String.valueOf(question_Id), max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap(new Func1<List<QuestionCommentBean>, Observable<List<QuestionCommentBean>>>() {
                    @Override
                    public Observable<List<QuestionCommentBean>> call(List<QuestionCommentBean> questionCommentListBeen) {
                        if (questionCommentListBeen.isEmpty()){
                            return Observable.just(questionCommentListBeen);
                        } else {
                            final List<Object> user_ids = new ArrayList<>();
                            for (QuestionCommentBean commentListBean : questionCommentListBeen) {
                                user_ids.add(commentListBean.getUser_id());
                                user_ids.add(commentListBean.getReply_user());
                            }
                            if (user_ids.isEmpty()) {
                                return Observable.just(questionCommentListBeen);
                            }
                            return mUserInfoRepository.getUserInfo(user_ids)
                                    .map(userInfoBeen -> {
                                        // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                                SparseArray<>();
                                        for (UserInfoBean userInfoBean : userInfoBeen) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                                    .intValue(), userInfoBean);
                                        }
                                        for (QuestionCommentBean commentListBean : questionCommentListBeen) {
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
                                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                                        return questionCommentListBeen;
                                    });
                        }
                    }
                });
    }
}
