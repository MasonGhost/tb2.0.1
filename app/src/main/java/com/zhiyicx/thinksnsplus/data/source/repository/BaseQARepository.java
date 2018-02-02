package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.QuestionCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.CollectAnswerList;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.QAPublishBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyAnswerContainerFragment;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class BaseQARepository implements IBasePublishQuestionRepository {

    protected QAClient mQAClient;

    @Inject
    Application mContext;

    @Inject
    protected QAPublishBeanGreenDaoImpl mQAPublishBeanGreenDaoImpl;

    @Inject
    protected AnswerDraftBeanGreenDaoImpl mAnswerDraftBeanGreenDaoImpl;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    protected UserInfoRepository mUserInfoRepository;


    @Inject
    public BaseQARepository(ServiceManager manager) {
        mQAClient = manager.getQAClient();
    }

    @Override
    public Observable<List<QATopicBean>> getAllTopic(String name, Long after, Long follow) {
        return mQAClient.getQATopic(name, after, follow, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QAListInfoBean>> getQAQuestion(String subject, Long maxId, String type) {
        return mQAClient.getQAQustion(subject, maxId, type, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QAListInfoBean>> getUserQAQustion(String type, Long after) {
        return mQAClient.getUserQAQustion(type, after, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QAListInfoBean>> getQAQuestionByTopic(String topicId, String subject,
                                                                 Long maxId, String type) {
        return mQAClient.getQAQustionByTopic(topicId, subject, maxId, type, (long) TSListFragment
                .DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ExpertBean>> getTopicExperts(Long maxId, int topic_id) {
        return mQAClient.getTopicExperts(topic_id, maxId, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<QATopicBean>> getFollowTopic(String type, Long after) {
        return dealMyFollowTopics(mQAClient.getQAFollowTopic(type, after, (long) TSListFragment
                .DEFAULT_PAGE_SIZE));
    }

    Observable<List<QATopicBean>> dealMyFollowTopics(Observable<List<QATopicBean>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(topicBeanList -> {
                    for (QATopicBean topicBean : topicBeanList) {
                        topicBean.setHas_follow(true);
                    }
                    return Observable.just(topicBeanList);
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public void handleTopicFollowState(String topic_id, boolean isFollow) {
        Observable.just(isFollow)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.PUT, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_HANDLE_TOPIC_FOLLOW_S, topic_id));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                });
    }

    @Override
    public void handleQuestionFollowState(String questionId, boolean isFollow) {
        Observable.just(isFollow)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.PUT, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_HANDLE_QUESTION_FOLLOW_S, questionId));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                });
    }

    @Override
    public void handleAnswerLike(boolean isLiked, long answer_id) {
        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.POST_V2, null);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, null);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig
                            .APP_PATH_LIKE_ANSWER_FORMAT, answer_id));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public Observable<BaseJsonV2<AnswerInfoBean>> payForOnlook(Long answer_id) {
        return mQAClient.payForOnlook(answer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        deleteQuestion(qestion);
        mQAPublishBeanGreenDaoImpl.saveSingleData(qestion);
    }

    @Override
    public void deleteQuestion(QAPublishBean qestion) {
        mQAPublishBeanGreenDaoImpl.deleteSingleCache(qestion);
    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mQAPublishBeanGreenDaoImpl.getSingleDataFromCache(qestion_mark);
    }

    @Override
    public void saveAnswer(AnswerDraftBean answer) {
        mAnswerDraftBeanGreenDaoImpl.saveSingleData(answer);
    }

    @Override
    public void deleteAnswer(AnswerDraftBean answer) {
        mAnswerDraftBeanGreenDaoImpl.deleteSingleCache(answer);
    }

    @Override
    public AnswerDraftBean getDraftAnswer(long answer_mark) {
        return mAnswerDraftBeanGreenDaoImpl.getSingleDataFromCache(answer_mark);
    }


    @Override
    public Observable<QAListInfoBean> getQuestionDetail(String questionId) {
        return mQAClient.getQuestionDetail(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<AnswerInfoBean>> getAnswerList(String questionId, String order_type, int size) {
        return mQAClient.getAnswerList(questionId, (long) TSListFragment.DEFAULT_PAGE_SIZE, order_type, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> deleteQuestion(Long question_id) {
        return mQAClient.deleteQuestion(String.valueOf(question_id));
    }

    @Override
    public Observable<BaseJsonV2<Object>> applyForExcellent(Long question_id) {
        return mQAClient.applyForExcellent(String.valueOf(question_id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ExpertBean>> getExpertList(int size, String topic_ids, String keyword) {
        return mQAClient.getExpertListByTopicIds(topic_ids, keyword, size).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<QATopicBean> getTopicDetail(String topic_id) {
        return mQAClient.getTopicDetail(topic_id);
    }

    @Override
    public Observable<BaseJsonV2<AnswerInfoBean>> publishAnswer(Long question_id, String body, String text_body, int anonymity) {
        return mQAClient.publishAnswer(question_id, body,text_body, anonymity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> updateAnswer(Long answer_id, String body, String text_body, int anonymity) {
        return mQAClient.uplaodAnswer(answer_id, body, text_body,anonymity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> updateQuestion(Long question_id, String body, int
            anonymity) {
        return null;
    }

    @Override
    public Observable<BaseJsonV2> createTopic(String name, String desc) {
        return mQAClient.createTopic(name, desc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<List<AnswerDigListBean>> getAnswerDigListV2(Long answer_id, Long max_id) {
        return mQAClient.getAnswerDigList(answer_id, max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(answerDigListBeen -> {

                    if (!answerDigListBeen.isEmpty()) {
                        List<Object> userids = new ArrayList<>();
                        for (int i = 0; i < answerDigListBeen.size(); i++) {
                            AnswerDigListBean answerDigListBean = answerDigListBeen.get(i);
                            userids.add(answerDigListBean.getUser_id());
                            userids.add(answerDigListBean.getTarget_user());
                        }
                        // 通过用户id列表请求用户信息和用户关注状态
                        return mUserInfoRepository.getUserInfo(userids)
                                .map(listBaseJson -> {
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : listBaseJson) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (AnswerDigListBean answerDigListBean : answerDigListBeen) {
                                        answerDigListBean.setDiggUserInfo(userInfoBeanSparseArray.get(answerDigListBean.getUser_id().intValue()));
                                        answerDigListBean.setTargetUserInfo(userInfoBeanSparseArray.get(answerDigListBean.getTarget_user()
                                                .intValue()));
                                    }
                                    mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                    return answerDigListBeen;
                                });
                    } else {
                        return Observable.just(answerDigListBeen);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }


    @Override
    public Observable<List<AnswerCommentListBean>> getAnswerCommentList(long answer_id, long max_id) {
        return mQAClient.getAnswerCommentList(answer_id, max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(answerCommentListBeen -> {
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
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public Observable<AnswerInfoBean> getAnswerDetail(long answer_id) {
        return mQAClient.getAnswerDetail(answer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(answerInfoBean -> {
                    if (answerInfoBean.getLikes() == null) {
                        return Observable.just(answerInfoBean);
                    } else {
                        final List<Object> user_ids = new ArrayList<>();
                        for (AnswerDigListBean answerDigListBean : answerInfoBean.getLikes()) {
                            user_ids.add(answerDigListBean.getUser_id());
                            user_ids.add(answerDigListBean.getTarget_user());
                        }
                        if (user_ids.isEmpty()) {
                            return Observable.just(answerInfoBean);
                        }
                        return mUserInfoRepository.getUserInfo(user_ids).map(userinfobeans -> {
                            // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                    SparseArray<>();
                            for (UserInfoBean userInfoBean : userinfobeans) {
                                userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                        .intValue(), userInfoBean);
                            }
                            for (AnswerDigListBean answerDigListBean : answerInfoBean.getLikes()) {
                                answerDigListBean.setDiggUserInfo(
                                        (userInfoBeanSparseArray.get(answerDigListBean
                                                .getUser_id().intValue())));
                                answerDigListBean.setTargetUserInfo(userInfoBeanSparseArray.get(answerDigListBean
                                        .getTarget_user().intValue()));

                            }
                            mUserInfoBeanGreenDao.insertOrReplace(userinfobeans);
                            return answerInfoBean;
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    @Override
    public Observable<BaseJsonV2<Object>> adoptionAnswer(long question_id, long answer_id) {
        return mQAClient.adoptionAnswer(question_id, answer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
//        BackgroundRequestTaskBean backgroundRequestTaskBean;
//        // 后台处理
//        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.PUT, null);
//        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_ADOPT_ANSWER_S, question_id, answer_id));
//        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void handleCollect(boolean isCollected, long answer_id) {
        Observable.just(isCollected)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.POST_V2, null);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, null);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig
                            .APP_PATH_COLLECT_ANSWER_FORMAT, answer_id));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void sendComment(String comment_content, long answer_id, long reply_to_user_id, long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", comment_content);
        params.put("comment_mark", comment_mark);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }

        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.SEND_ANSWER_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_COMMENT_QA_ANSWER_FORMAT, answer_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(long answer_id, long comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, null);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_DELETE_QA_ANSWER_COMMENT_FORMAT, answer_id,
                comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteAnswer(long answer_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, null);
        backgroundRequestTaskBean.setPath(String.format(Locale.getDefault(), ApiConfig.APP_PATH_DELETE_ANSWER_S, answer_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public Observable<List<QuestionCommentBean>> getQuestionCommentList(Long question_Id, Long max_id) {
        return mQAClient.getQuestionCommentList(String.valueOf(question_Id), max_id, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .observeOn(Schedulers.io())
                .flatMap(questionCommentListBeen -> {
                    if (questionCommentListBeen.isEmpty()) {
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
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void sendQuestionComment(String comment_content, long question_id, long reply_to_user_id, long comment_mark) {
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
    public Observable<BaseJsonV2<Object>> deleteQuestionComment(long question_id, long answer_id) {
        return mQAClient.deleteQuestionComment(String.valueOf(question_id), String.valueOf(answer_id));
    }

    @Override
    public Observable<List<AnswerInfoBean>> getUserAnswerList(String type, Long maxId) {
        if (MyAnswerContainerFragment.TYPE_FOLLOW.equals(type)) {
            return getUserCollectAnswerList((long) TSListFragment.DEFAULT_PAGE_SIZE, maxId);
        }
        return mQAClient.getUserAnswerList(type, (long) TSListFragment.DEFAULT_PAGE_SIZE, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<AnswerInfoBean>> getUserCollectAnswerList(Long limit, Long maxId) {
        return mQAClient.getUserCollectAnswerList((long) TSListFragment.DEFAULT_PAGE_SIZE, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(collectAnswerLists -> {
                    List<AnswerInfoBean> result=new ArrayList<>();
                    for (CollectAnswerList collect:collectAnswerLists){
                        result.add(collect.getCollectible());
                    }
                    return Observable.just(result);
                });
    }

    @Override
    public Observable<Object> publishQuestion(QAPublishBean qaPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(qaPublishBean));
        return mQAClient.publishQuestion(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Object> updateQuestion(QAPublishBean qaPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(qaPublishBean));
        return mQAClient.uplaodQuestion(qaPublishBean.getId(), body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> resetReward(Long question_id, double amount) {
        return mQAClient.updateQuestionReward(String.valueOf(question_id), (int) amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
