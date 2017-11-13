package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig.RANK_DAY;
import static com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig.RANK_MONTH;
import static com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig.RANK_WEEK;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class RankTypeListPresenter extends AppBasePresenter<RankTypeListContract.Repository, RankTypeListContract.View>
        implements RankTypeListContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public RankTypeListPresenter(RankTypeListContract.Repository repository, RankTypeListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Observable<List<UserInfoBean>> observable = null;
        int size = mRootView.getListDatas().size();
        if (size >= 100){
            // 大于100条就不再请求了
            mRootView.onNetResponseSuccess(null, isLoadMore);
            return;
        }
        if (maxId == 0){
            size = 0;
        }
        String type = mRootView.getRankType();
        switch (type){
            case RankTypeConfig.RANK_USER_FOLLOWER:
                observable = mRepository.getRankFollower(size);
                break;
            case RankTypeConfig.RANK_USER_RICHES:
                observable = mRepository.getRankRiches(size);
                break;
            case RankTypeConfig.RANK_USER_INCOME:
                observable = mRepository.getRankIncome(size);
                break;
            case RankTypeConfig.RANK_USER_CHECK_ID:
                observable = mRepository.getRankCheckIn(size);
                break;
            case RankTypeConfig.RANK_USER_EXPERT:
                observable = mRepository.getRankQuestionExpert(size);
                break;
            case RankTypeConfig.RANK_USER_QUESTION_LIKE:
                observable = mRepository.getRankQuestionLikes(size);
                break;
            case RankTypeConfig.RANK_QUESTION_DAY:
                observable = mRepository.getRankAnswer(RANK_DAY, size);
                break;
            case RankTypeConfig.RANK_QUESTION_WEEK:
                observable = mRepository.getRankAnswer(RANK_WEEK, size);
                break;
            case RankTypeConfig.RANK_QUESTION_MONTH:
                observable = mRepository.getRankAnswer(RANK_MONTH, size);
                break;
            case RankTypeConfig.RANK_DYNAMIC_DAY:
                observable = mRepository.getRankDynamic(RANK_DAY, size);
                break;
            case RankTypeConfig.RANK_DYNAMIC_WEEK:
                observable = mRepository.getRankDynamic(RANK_WEEK, size);
                break;
            case RankTypeConfig.RANK_DYNAMIC_MONTH:
                observable = mRepository.getRankDynamic(RANK_MONTH, size);
                break;
            case RankTypeConfig.RANK_INFORMATION_DAY:
                observable = mRepository.getRankInfo(RANK_DAY, size);
                break;
            case RankTypeConfig.RANK_INFORMATION_WEEK:
                observable = mRepository.getRankInfo(RANK_WEEK, size);
                break;
            case RankTypeConfig.RANK_INFORMATION_MONTH:
                observable = mRepository.getRankInfo(RANK_MONTH, size);
                break;
            default:
        }
        if (observable != null){
            Subscription subscription = observable
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {

                        @Override
                        protected void onSuccess(List<UserInfoBean> data) {
//                            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.
//                                    getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
//                            if (userInfoBean != null && maxId == 0){
//                                data.add(0, userInfoBean);
//                            }
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
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleFollowState(UserInfoBean userInfoBean) {
        userInfoBean.setFollower(!userInfoBean.getFollower());
        mUserInfoRepository.handleFollow(userInfoBean);
    }
}
