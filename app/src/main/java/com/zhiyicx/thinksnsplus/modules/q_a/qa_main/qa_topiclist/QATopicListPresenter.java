package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_topiclist;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.QASearchBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QATopicFragmentContainerFragment.TOPIC_TYPE_FOLLOW;
import static com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QATopicFragmentContainerFragment.TOPIC_TYPE_SEARCH;
import static com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListPresenter.DEFAULT_FIRST_SHOW_HISTORY_SIZE;

/**
 * @Author Jliuer
 * @Date 2017/07/26/9:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QATopicListPresenter extends AppBasePresenter<QATopicListConstact.View>
        implements QATopicListConstact.Presenter {

    @Inject
    QASearchBeanGreenDaoImpl mQASearchBeanGreenDao;
    @Inject
    BaseQARepository mBaseQARepository;

    private Subscription all;

    @Inject
    public QATopicListPresenter(QATopicListConstact.View rootView) {
        super(rootView);
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
        Subscription subscribe = mBaseQARepository.getFollowTopic(type, maxId).subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
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
    public void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        if (all != null && !all.isUnsubscribed()) {
            all.unsubscribe();
        }
        if (mRootView.getType().equals(TOPIC_TYPE_SEARCH) && TextUtils.isEmpty(name)) {
            // 无搜索内容
            mRootView.hideRefreshState(isLoadMore);
            return;
        }
        all = mBaseQARepository.getAllTopic(name, maxId, follow).subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
            @Override
            protected void onSuccess(List<QATopicBean> data) {
                saveSearhDatq(name);
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
        addSubscrebe(all);
    }


    @Override
    public void handleTopicFollowState(int position, String topic_id, boolean isFollow) {
        mRootView.getListDatas().get(position).setHas_follow(!isFollow);
        mBaseQARepository.handleTopicFollowState(topic_id, !isFollow);
        EventBus.getDefault().post(mRootView.getListDatas().get(position), EventBusTagConfig.EVENT_QA_SUBSCRIB);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return false;
    }

    /**
     * 存搜索记录
     *
     * @param searchContent save content
     */
    private void saveSearhDatq(String searchContent) {
        if (TextUtils.isEmpty(searchContent)) {
            return;
        }
        QASearchHistoryBean qaSearchHistoryBean = new QASearchHistoryBean(searchContent, QASearchHistoryBean.TYPE_QA_TOPIC);
        mQASearchBeanGreenDao.saveHistoryDataByType(qaSearchHistoryBean, QASearchHistoryBean.TYPE_QA_TOPIC);
    }

    @Override
    public List<QASearchHistoryBean> getFirstShowHistory() {
        return mQASearchBeanGreenDao.getFristShowData(DEFAULT_FIRST_SHOW_HISTORY_SIZE, QASearchHistoryBean.TYPE_QA_TOPIC);
    }

    @Override
    public void cleaerAllSearchHistory() {
        mQASearchBeanGreenDao.clearAllQATopicSearchHistory();
    }

    @Override
    public List<QASearchHistoryBean> getAllSearchHistory() {
        return mQASearchBeanGreenDao.getQATopicSearchHistory();
    }

    @Override
    public void deleteSearchHistory(QASearchHistoryBean qaSearchHistoryBean) {
        mQASearchBeanGreenDao.deleteSingleCache(qaSearchHistoryBean);
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
