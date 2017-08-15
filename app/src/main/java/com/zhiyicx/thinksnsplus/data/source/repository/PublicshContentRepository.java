package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.QAAnswerBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.PublishContentConstact;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublicshContentRepository extends BasePublishQuestionRepository implements PublishContentConstact.Repository {

    @Inject
    public PublicshContentRepository(ServiceManager serviceManager) {
        super(serviceManager);
    }

    @Override
    public Observable<BaseJsonV2<QAAnswerBean>> publishAnswer(Long question_id,String body, int anonymity) {
        return mQAClient.publishAnswer(question_id,body,anonymity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
