package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

@FragmentScoped
public class AddTopicPresenter extends AppBasePresenter< AddTopicContract.View>
        implements AddTopicContract.Presenter {

    @Inject
    BaseQARepository mQA$RewardRepositoryPublish;
    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public AddTopicPresenter( AddTopicContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        Subscription subscribe = mBaseQARepository.getAllTopic(name, maxId, follow)
                .subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
                    @Override
                    protected void onSuccess(List<QATopicBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void updateQuestion(QAPublishBean qaPublishBean) {
        Subscription subscribe = mQA$RewardRepositoryPublish.updateQuestion(qaPublishBean)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.update_ing)))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 解析数据，在跳转到问题详情页时需要用到
                        QAListInfoBean qaListInfoBean = new QAListInfoBean();
                        qaListInfoBean.setUser_id(AppApplication.getMyUserIdWithdefault());
                        qaListInfoBean.setLook(qaPublishBean.getLook());
                        mRootView.updateSuccess(qaListInfoBean);
                        mBaseQARepository.deleteQuestion(qaPublishBean);
                        qaPublishBean.setMark(qaPublishBean.getMark() - 1);
                        mBaseQARepository.deleteQuestion(qaPublishBean);
                        mRootView.showSnackMessage(mContext.getString(R.string.update_success), Prompt.DONE);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mBaseQARepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mBaseQARepository.saveQuestion(qestion);
    }
}
