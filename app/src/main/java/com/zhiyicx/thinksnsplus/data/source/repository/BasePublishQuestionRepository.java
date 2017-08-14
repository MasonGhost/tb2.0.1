package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.remote.QAClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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
    public Observable<List<QAListInfoBean>> getQAQustion(String subject, Long maxId, String type) {
        return mQAClient.getQAQustion(subject, maxId, type, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ExpertBean>> getTopicExperts(Long maxId, int topic_id) {
        return mQAClient.getTopicExperts(topic_id, maxId, (long) TSListFragment.DEFAULT_PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
