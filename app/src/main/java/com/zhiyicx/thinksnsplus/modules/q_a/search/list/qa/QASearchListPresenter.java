package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.QASearchBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/18
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class QASearchListPresenter extends AppBasePresenter<
        QASearchListContract.View> implements QASearchListContract.Presenter {


    public static final int DEFAULT_FIRST_SHOW_HISTORY_SIZE = 5;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    BaseQARepository mBaseQARepository;
    @Inject
    QASearchBeanGreenDaoImpl mQASearchBeanGreenDao;


    private Subscription all;

    @Inject
    public QASearchListPresenter(QASearchListContract.View rootView) {
        super(rootView);

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (all != null && !all.isUnsubscribed()) {
            all.unsubscribe();
        }
        final String searchContent = mRootView.getSearchInput();
        if (TextUtils.isEmpty(searchContent)) {// 无搜索内容
            mRootView.hideRefreshState(isLoadMore);
            return;
        }
        all = mBaseQARepository.getQAQuestion(searchContent, maxId, "all")
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {
                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        // 历史记录存入数据库
                        saveSearhDatq(searchContent);
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(all);

    }

    /**
     * 存搜索记录
     *
     * @param searchContent
     */
    private void saveSearhDatq(String searchContent) {
        QASearchHistoryBean qaSearchHistoryBean = new QASearchHistoryBean(searchContent, QASearchHistoryBean.TYPE_QA);
        mQASearchBeanGreenDao.saveHistoryDataByType(qaSearchHistoryBean, QASearchHistoryBean.TYPE_QA);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return true;
    }


    @Override
    public List<QASearchHistoryBean> getFirstShowHistory() {
        return mQASearchBeanGreenDao.getFristShowData(DEFAULT_FIRST_SHOW_HISTORY_SIZE, QASearchHistoryBean.TYPE_QA);
    }

    @Override
    public void cleaerAllSearchHistory() {
        mQASearchBeanGreenDao.clearAllQASearchHistory();
    }

    @Override
    public List<QASearchHistoryBean> getAllSearchHistory() {
        return mQASearchBeanGreenDao.getQASearchHistory();
    }

    @Override
    public void deleteSearchHistory(QASearchHistoryBean qaSearchHistoryBean) {
        mQASearchBeanGreenDao.deleteSingleCache(qaSearchHistoryBean);
    }

}
