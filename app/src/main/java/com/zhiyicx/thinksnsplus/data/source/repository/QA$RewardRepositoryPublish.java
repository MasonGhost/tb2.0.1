package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.QARewardContract;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class QA$RewardRepositoryPublish extends BaseQARepository implements QARewardContract.RepositoryPublish {

    @Inject
    public QA$RewardRepositoryPublish(ServiceManager manager) {
        super(manager);
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
