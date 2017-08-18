package com.zhiyicx.thinksnsplus.data.source.repository;

import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.comment.QuestionCommentContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    @Override
    public void sendComment(String comment_content, long question_id, long reply_to_user_id, long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", comment_content);
        params.put("comment_mark", comment_mark);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_QUESTION_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_SEND_QUESTION_COMMENT_S, question_id + ""));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public Observable<BaseJsonV2<Object>> deleteComment(long question_id, long answer_id) {
        return mQAClient.deleteQuestionComment(String.valueOf(question_id), String.valueOf(answer_id));
    }
}
