package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list;

import android.os.Bundle;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class TopicDetailListPresenter extends AppBasePresenter<TopicDetailListContract.Repository, TopicDetailListContract.View>
        implements TopicDetailListContract.Presenter{

    @Inject
    public TopicDetailListPresenter(TopicDetailListContract.Repository repository, TopicDetailListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getQAQuestionByTopic(String.valueOf(mRootView.getTopicId()),
                "", maxId, mRootView.getCurrentType())
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {

                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
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
    public List<QAListInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE)
    public void updateList(Bundle bundle) {
        if (bundle != null) {
            QAListInfoBean qaListInfoBean = (QAListInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
            if (qaListInfoBean != null) {
                for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                    if (qaListInfoBean.getId().equals(mRootView.getListDatas().get(i).getId())){
                        mRootView.getListDatas().remove(i);
                        mRootView.refreshData();
                        EventBus.getDefault().post("success",EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
