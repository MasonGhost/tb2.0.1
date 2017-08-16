package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/15
 * @contact email:648129313@qq.com
 */

public class QuestionDetailRepository extends BaseQARepository implements QuestionDetailContract.Repository{

    @Inject
    public QuestionDetailRepository(ServiceManager manager) {
        super(manager);
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
}
