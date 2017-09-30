package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.QAPublishBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

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
    public Observable<List<QAListInfoBean>> getQAQuestionByTopic(String topicId, String subject, Long maxId, String type) {
        return mQAClient.getQAQustionByTopic(topicId, subject, maxId, type, (long) TSListFragment.DEFAULT_PAGE_SIZE)
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
        return dealMyFollowTopics(mQAClient.getQAFollowTopic(type, after, (long) TSListFragment.DEFAULT_PAGE_SIZE));
    }

    Observable<List<QATopicBean>> dealMyFollowTopics(Observable<List<QATopicBean>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<QATopicBean>, Observable<List<QATopicBean>>>() {
                    @Override
                    public Observable<List<QATopicBean>> call(List<QATopicBean> topicBeanList) {
                        for (QATopicBean topicBean : topicBeanList) {
                            topicBean.setHas_follow(true);
                        }
                        return Observable.just(topicBeanList);
                    }
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

    public Observable<BaseJsonV2<AnswerInfoBean>> payForOnlook(Long answer_id) {
        return mQAClient.payForOnlook(answer_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
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
//        mAnswerDraftBeanGreenDaoImpl.saveSingleData(answer);// 暂时屏蔽掉回答的草稿
    }

    @Override
    public void deleteAnswer(AnswerDraftBean answer) {
        mAnswerDraftBeanGreenDaoImpl.deleteSingleCache(answer);
    }

    @Override
    public AnswerDraftBean getDraftAnswer(long answer_mark) {
        return mAnswerDraftBeanGreenDaoImpl.getSingleDataFromCache(answer_mark);
    }
}
