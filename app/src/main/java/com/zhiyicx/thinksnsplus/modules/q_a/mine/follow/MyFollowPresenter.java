package com.zhiyicx.thinksnsplus.modules.q_a.mine.follow;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.QATopicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.q_a.mine.container.MyFollowContainerFragment.TYPE_TOPIC;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyFollowPresenter extends AppBasePresenter<MyFollowContract.View>
        implements MyFollowContract.Presenter {

    @Inject
    QATopicBeanGreenDaoImpl mQaTopicBeanGreenDao;

    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public MyFollowPresenter(MyFollowContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        List<BaseListBean> listBeen = new ArrayList<>();
        Subscription subscription = null;
        if (mRootView.getType().equals(TYPE_TOPIC)) {
            subscription = mBaseQARepository.getFollowTopic("follow", maxId)
                    .subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
                        @Override
                        protected void onSuccess(List<QATopicBean> data) {
                            listBeen.addAll(data);
                            mRootView.onNetResponseSuccess(listBeen, isLoadMore);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRootView.onResponseError(e, isLoadMore);
                        }
                    });
        } else {
            subscription = mBaseQARepository.getQAQuestion("", maxId, "follow")
                    .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                        @Override
                        protected void onSuccess(List<QAListInfoBean> data) {
                            listBeen.addAll(data);
                            mRootView.onNetResponseSuccess(listBeen, isLoadMore);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mRootView.onResponseError(e, isLoadMore);
                        }
                    });
        }
        if (subscription != null) {
            addSubscrebe(subscription);
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        List<BaseListBean> listBeen = new ArrayList<>();
        if (mRootView.getType().equals(TYPE_TOPIC)) {
            if (mQaTopicBeanGreenDao.getUserFollowTopic() != null) {
                listBeen.addAll(mQaTopicBeanGreenDao.getUserFollowTopic());
            }
        }
        mRootView.onCacheResponseSuccess(listBeen, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleTopicFollowState(int position, QATopicBean qaTopicBean) {
        boolean isFollow = !qaTopicBean.getHas_follow();
        qaTopicBean.setHas_follow(isFollow);
        if (isFollow) {
            qaTopicBean.setFollows_count(qaTopicBean.getFollows_count() + 1);
        } else {
            qaTopicBean.setFollows_count(qaTopicBean.getFollows_count() - 1);
        }
        mRootView.updateTopicFollowState(qaTopicBean);
        mQaTopicBeanGreenDao.updateSingleData(qaTopicBean);
        mBaseQARepository.handleTopicFollowState(String.valueOf(qaTopicBean.getId()), isFollow);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_QA_SUBSCRIB)
    public void uploadTopicSubscribState(QATopicBean topicBean) {
        if (topicBean != null) {
            mRootView.updateTopicFollowState(topicBean);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
