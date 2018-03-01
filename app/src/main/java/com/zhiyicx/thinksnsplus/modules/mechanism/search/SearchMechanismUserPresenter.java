package com.zhiyicx.thinksnsplus.modules.mechanism.search;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.SearchBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/7
 * @Contact master.jungle68@gmail.com
 */

public class SearchMechanismUserPresenter extends AppBasePresenter<SearchMechanismUserContract.View>
        implements SearchMechanismUserContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    SearchBeanGreenDaoImpl mSearchBeanGreenDao;

    private Subscription searchSub;


    @Inject
    public SearchMechanismUserPresenter(SearchMechanismUserContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void searchUser(String name) {
        if (TextUtils.isEmpty(name)) {// 无搜索内容
            mRootView.hideRefreshState(false);
            return;
        }

        if (searchSub != null && !searchSub.isUnsubscribed()) {
            searchSub.unsubscribe();
        }

        searchSub = mUserInfoRepository.searchUserInfo(null, name, null, null, null)
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        // 历史记录存入数据库
                        saveSearhDatq(name);
                        mRootView.onNetResponseSuccess(data, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, false);
                    }
                });
        addSubscrebe(searchSub);

    }

    @Override
    public List<SearchHistoryBean> getFirstShowHistory() {
        return mSearchBeanGreenDao.getFristShowData(SearchMechanismUserFragment.DEFAULT_FIRST_SHOW_HISTORY_SIZE, SearchHistoryBean
                .TYPE_MECHAINSIM_USER);

    }

    @Override
    public List<SearchHistoryBean> getAllSearchHistory() {
        return mSearchBeanGreenDao.getAllDataByType(SearchHistoryBean.TYPE_MECHAINSIM_USER);
    }

    @Override
    public void cleaerAllSearchHistory() {
        mSearchBeanGreenDao.clearAllSearchHistoryByType(SearchHistoryBean.TYPE_MECHAINSIM_USER);
    }

    @Override
    public void deleteSearchHistory(SearchHistoryBean searchHistoryBean) {
        mSearchBeanGreenDao.deleteSingleCache(searchHistoryBean);
    }

    /**
     * 存搜索记录
     *
     * @param searchContent
     */
    private void saveSearhDatq(String searchContent) {
        SearchHistoryBean qaSearchHistoryBean = new SearchHistoryBean(searchContent, SearchHistoryBean.TYPE_MECHAINSIM_USER);
        qaSearchHistoryBean.setCreate_time(System.currentTimeMillis());
        mSearchBeanGreenDao.saveHistoryDataByType(qaSearchHistoryBean);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore, int pageType) {

    }

    @Override
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDateFollowFansState(index);
    }

}
