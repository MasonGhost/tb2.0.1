package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleJoinedBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CircleSearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CircleSearchBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa.QASearchListPresenter.DEFAULT_FIRST_SHOW_HISTORY_SIZE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class BaseCircleListPresenter extends AppBasePresenter<BaseCircleListContract.Repository, BaseCircleListContract.View>
        implements BaseCircleListContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;

    @Inject
    CircleSearchBeanGreenDaoImpl mCircleSearchBeanGreenDao;

    Subscription mSearchSub;

    @Inject
    public BaseCircleListPresenter(BaseCircleListContract.Repository repository, BaseCircleListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        switch (mRootView.getMineCircleType()) {
            case JOIN:
            case AUDIT:
                Subscription subscribe = mRepository.getMyJoinedCircle(TSListFragment.DEFAULT_PAGE_SIZE
                        , maxId.intValue(), mRootView.getMineCircleType().value)
                        .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {

                            @Override
                            protected void onSuccess(List<CircleInfo> data) {
                                mRootView.onNetResponseSuccess(data, isLoadMore);
                            }

                            @Override
                            protected void onFailure(String message, int code) {
                                super.onFailure(message, code);
                                mRootView.showMessage(message);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                super.onException(throwable);
                                mRootView.onResponseError(throwable, isLoadMore);
                            }
                        });
                addSubscrebe(subscribe);

                break;
            case SEARCH:
                if (mSearchSub != null && !mSearchSub.isUnsubscribed()) {
                    mSearchSub.unsubscribe();
                }
                final String searchContent = mRootView.getSearchInput();
                if (TextUtils.isEmpty(searchContent)) {// 无搜索内容
                    mRootView.hideRefreshState(isLoadMore);
                    return;
                }
                mSearchSub = mRepository.getAllCircle(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), searchContent, null)
                        .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                            @Override
                            protected void onSuccess(List<CircleInfo> data) {
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
                addSubscrebe(mSearchSub);


                break;

            default:


        }


    }

    /**
     * 存搜索记录
     *
     * @param searchContent
     */
    private void saveSearhDatq(String searchContent) {
        CircleSearchHistoryBean cricleSearchHistoryBean = new CircleSearchHistoryBean(searchContent, CircleSearchHistoryBean.TYPE_CIRCLE);
        mCircleSearchBeanGreenDao.saveHistoryDataByType(cricleSearchHistoryBean, CircleSearchHistoryBean.TYPE_CIRCLE);
    }


    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {

        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;

        mRepository.dealCircleJoinOrExit(circleInfo)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        if (isJoined) {
                            circleInfo.setJoined(null);
                            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
                        } else {
                            // 如果是 封闭的或者 收费的 ，就不及时更新
                            if (CircleInfo.CirclePayMode.PRIVATE.value.equals(circleInfo.getMode())
                                    || CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode())) {
                                return;
                            }
                            circleInfo.setJoined(new CircleJoinedBean());
                            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);
                        }
                        mCircleInfoGreenDao.updateSingleData(circleInfo);
                        mRootView.refreshData(position);
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

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        mCircleInfoGreenDao.saveMultiData(data);
        return isLoadMore;
    }


    @Override
    public List<CircleSearchHistoryBean> getFirstShowHistory() {
        return mCircleSearchBeanGreenDao.getFristShowData(DEFAULT_FIRST_SHOW_HISTORY_SIZE, QASearchHistoryBean.TYPE_QA);
    }

    @Override
    public void cleaerAllSearchHistory() {
        mCircleSearchBeanGreenDao.clearAllQASearchHistory();
    }

    @Override
    public List<CircleSearchHistoryBean> getAllSearchHistory() {
        return mCircleSearchBeanGreenDao.getCircleSearchHistory();
    }

    @Override
    public void deleteSearchHistory(CircleSearchHistoryBean qaSearchHistoryBean) {
        mCircleSearchBeanGreenDao.deleteSingleCache(qaSearchHistoryBean);
    }

}
