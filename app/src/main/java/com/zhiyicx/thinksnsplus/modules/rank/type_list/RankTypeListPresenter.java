package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

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
        String type = mRootView.getRankType();
        // 全站粉丝
        if (type.equals(mContext.getString(R.string.rank_user_type_all))){
            observable = mRepository.getRankFollower(size);
        }
        // 财富
        if (type.equals(mContext.getString(R.string.rank_user_type_riches))){
            observable = mRepository.getRankRiches(size);
        }
        // 收入
        if (type.equals(mContext.getString(R.string.rank_user_type_income))){
            observable = mRepository.getRankIncome(size);
        }
        // 连续签到
        if (type.equals(mContext.getString(R.string.rank_user_type_sign_in))){
            observable = mRepository.getRankCheckIn(size);
        }
        // 专家
        if (type.equals(mContext.getString(R.string.rank_user_type_expert))){
            observable = mRepository.getRankQuestionExpert(size);
        }
        // 问答达人
        if (type.equals(mContext.getString(R.string.rank_user_type_qa))){
            observable = mRepository.getRankQuestionLikes(size);
        }
        /*问答*/
        // 今日解答
        if (type.equals(mContext.getString(R.string.rank_qa_type_day))){
            observable = mRepository.getRankAnswer("day", size);
        }
        // 本周解答
        if (type.equals(mContext.getString(R.string.rank_qa_type_week))){
            observable = mRepository.getRankAnswer("week", size);
        }
        // 本月解答
        if (type.equals(mContext.getString(R.string.rank_qa_type_month))){
            observable = mRepository.getRankAnswer("month", size);
        }
        /*动态*/
        if (type.equals(mContext.getString(R.string.rank_dynamic_type_day))){
            observable = mRepository.getRankDynamic("day", size);
        }
        if (type.equals(mContext.getString(R.string.rank_dynamic_type_week))){
            observable = mRepository.getRankDynamic("week", size);
        }
        if (type.equals(mContext.getString(R.string.rank_dynamic_type_month))){
            observable = mRepository.getRankDynamic("month", size);
        }
       /*资讯*/
        if (type.equals(mContext.getString(R.string.rank_info_type_day))){
            observable = mRepository.getRankInfo("day", size);
        }
        if (type.equals(mContext.getString(R.string.rank_info_type_week))){
            observable = mRepository.getRankInfo("week", size);
        }
        if (type.equals(mContext.getString(R.string.rank_info_type_month))){
            observable = mRepository.getRankInfo("month", size);
        }
        if (observable != null){
            Subscription subscription = observable
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {

                        @Override
                        protected void onSuccess(List<UserInfoBean> data) {
                            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.
                                    getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
                            if (userInfoBean != null && maxId == 0){
                                data.add(0, userInfoBean);
                            }
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
    public List<UserInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleFollowState(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);
        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
    }
}
