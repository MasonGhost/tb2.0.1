package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.create_topic.CreateTopicContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/09/15/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateTopicRepository extends BaseQARepository implements CreateTopicContract.Repository {

    @Inject
    public CreateTopicRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<BaseJsonV2> createTopic(String name, String desc) {
        return mQAClient.createTopic(name, desc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
