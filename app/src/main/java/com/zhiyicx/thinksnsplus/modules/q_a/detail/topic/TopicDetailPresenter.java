package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.QATopicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class TopicDetailPresenter extends AppBasePresenter<TopicDetailContract.Repository, TopicDetailContract.View>
        implements TopicDetailContract.Presenter{

    @Inject
    QATopicBeanGreenDaoImpl mQaTopicBeanGreenDao;

    @Inject
    public TopicDetailPresenter(TopicDetailContract.Repository repository, TopicDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (mRootView.getCurrentTopicBean() == null){
            getTopicDetail("1");
        }
        Subscription subscription = mRepository.getQAQuestionByTopic("1", "", maxId, mRootView.getCurrentType())
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {

                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
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

    @Override
    public void getTopicDetail(String topic_id) {
        Subscription subscription = mRepository.getTopicDetail(topic_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<QATopicBean>() {

                    @Override
                    protected void onSuccess(QATopicBean data) {
                        mRootView.setTopicDetail(data);
                        mQaTopicBeanGreenDao.insertOrReplace(data);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleTopicFollowState(String topic_id, boolean isFollow) {
        mRootView.getCurrentTopicBean().setHas_follow(isFollow);
        if (isFollow){
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() + 1);
        } else {
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() - 1);
        }
        mRootView.updateFollowState();
        mQaTopicBeanGreenDao.updateSingleData(mRootView.getCurrentTopicBean());
        mRepository.handleTopicFollowState(topic_id, isFollow);
    }
}
