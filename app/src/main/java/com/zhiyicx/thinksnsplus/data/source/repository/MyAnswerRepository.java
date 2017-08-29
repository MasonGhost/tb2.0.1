package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.q_a.mine.answer.MyAnswerContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyAnswerRepository extends BaseQARepository implements MyAnswerContract.Repository{

    @Inject
    public MyAnswerRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<List<AnswerInfoBean>> getUserAnswerList(String type, Long maxId) {
        return mQAClient.getUserAnswerList(type, (long) TSListFragment.DEFAULT_PAGE_SIZE, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
