package com.zhiyicx.thinksnsplus.modules.q_a.mine.answer;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerInfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyAnswerPresenter extends AppBasePresenter< MyAnswerContract.View>
        implements MyAnswerContract.Presenter{

    @Inject
    AnswerInfoListBeanGreenDaoImpl mAnswerInfoListBeanGreenDao;

    @Inject
    BaseQARepository mBaseQARepository;
    @Inject
    public MyAnswerPresenter(MyAnswerContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseQARepository.getUserAnswerList(mRootView.getType(), maxId)
                .subscribe(new BaseSubscribeForV2<List<AnswerInfoBean>>() {
                    @Override
                    protected void onSuccess(List<AnswerInfoBean> data) {
                        mAnswerInfoListBeanGreenDao.saveMultiData(data);
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AnswerInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleLike(int position, AnswerInfoBean answerInfoBean) {
        boolean isLiked = !answerInfoBean.getLiked();
        answerInfoBean.setLiked(isLiked);
        if (isLiked){
            answerInfoBean.setLikes_count(answerInfoBean.getLikes_count() + 1);
        } else {
            answerInfoBean.setLikes_count(answerInfoBean.getLikes_count() - 1);
        }
        mRootView.updateList(position, answerInfoBean);
        mAnswerInfoListBeanGreenDao.insertOrReplace(answerInfoBean);
        mBaseQARepository.handleAnswerLike(isLiked, answerInfoBean.getId());
    }
}
