package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QATopicFragmentContainerFragment.TOPIC_TYPE_FOLLOW;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicListPresenter extends AppBasePresenter<QATopicListConstact.Repository, QATopicListConstact.View>
        implements QATopicListConstact.Presenter {

    @Inject
    public QATopicListPresenter(QATopicListConstact.Repository repository, QATopicListConstact.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(String type, Long maxId, boolean isLoadMore) {
        mRepository.getFollowTopic(type, maxId).subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
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
    }

    @Override
    public void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        mRepository.getAllTopic(name, maxId, follow).subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
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
    }

    @Override
    public void handleTopicFollowState(int position, String topic_id, boolean isFollow) {
        mRootView.getListDatas().get(position).setHas_follow(!isFollow);
        mRepository.handleTopicFollowState(topic_id, !isFollow);
        EventBus.getDefault().post(mRootView.getListDatas().get(position), EventBusTagConfig.EVENT_QA_SUBSCRIB);
    }

    @Override
    public List<QATopicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return false;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_QA_SUBSCRIB)
    public void uploadTopicSubscribState(QATopicBean topicBean) {
        int position = mRootView.getListDatas().indexOf(topicBean);
        // 如果当前列表存在这样的数据，刷新该数据
        if (position == -1) {
            mRootView.getListDatas().add(0, topicBean);
            mRootView.refreshData();
        } else {
            if (mRootView.getType().equals(TOPIC_TYPE_FOLLOW)) {
                mRootView.getListDatas().remove(topicBean);
                mRootView.refreshData();
            } else {
                mRootView.refreshData(position);
            }
        }

    }
}
