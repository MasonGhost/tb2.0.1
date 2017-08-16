package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;
import java.util.List;

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

public class BasePublishQuestionRepository implements IBasePublishQuestionRepository {

    protected QAClient mQAClient;

    @Inject
    Application mContext;

    @Inject
    public BasePublishQuestionRepository(ServiceManager manager) {
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
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<QATopicBean>, Observable<List<QATopicBean>>>() {
                    @Override
                    public Observable<List<QATopicBean>> call(List<QATopicBean> topicBeanList) {
                        for (QATopicBean topicBean : topicBeanList) {
                            topicBean.setHas_follow(true);
                        }
                        return Observable.just(topicBeanList);
                    }
                });
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
}
