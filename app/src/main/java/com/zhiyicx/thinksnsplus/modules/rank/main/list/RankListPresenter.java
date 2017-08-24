package com.zhiyicx.thinksnsplus.modules.rank.main.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class RankListPresenter extends AppBasePresenter<RankListContract.Repository, RankListContract.View>
        implements RankListContract.Presenter {

    @Inject
    public RankListPresenter(RankListContract.Repository repository, RankListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        String category = mRootView.getCategory();
        if (category.equals(mContext.getString(R.string.rank_user))){
            requestNetDataUser(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_qa))){
            requestNetDataQuestion(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_dynamic))){
            requestNetDataDynamic(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_info))){
            requestNetDataInfo(isLoadMore);
        }
    }

    private void requestNetDataUser(boolean isLoadMore){
        Subscription subscription = Observable.zip(mRepository.getRankFollower(0)
                , mRepository.getRankRiches(0)
                , mRepository.getRankIncome(0)
                , mRepository.getRankCheckIn(0)
                , mRepository.getRankQuestionExpert(0)
                , mRepository.getRankQuestionLikes(0),
                (userInfoFollower, userInfoRiches, userInfoIncome, userInfoCheckIn, userInfoExpert, userInfoQuestionLike) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_user_type_all), userInfoFollower);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_riches), userInfoRiches);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_income), userInfoIncome);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_sign_in), userInfoCheckIn);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_expert), userInfoExpert);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_qa), userInfoQuestionLike);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
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

    private void requestNetDataQuestion(boolean isLoadMore){
        Subscription subscription = Observable.zip(mRepository.getRankAnswer("day", 0)
                , mRepository.getRankAnswer("week", 0)
                , mRepository.getRankAnswer("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_day), userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_week), userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_month), userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
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

    private void requestNetDataDynamic(boolean isLoadMore){
        Subscription subscription = Observable.zip(mRepository.getRankDynamic("day", 0)
                , mRepository.getRankDynamic("week", 0)
                , mRepository.getRankDynamic("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_day), userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_week), userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_month), userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
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

    private void requestNetDataInfo(boolean isLoadMore){
        Subscription subscription = Observable.zip(mRepository.getRankInfo("day", 0)
                , mRepository.getRankInfo("week", 0)
                , mRepository.getRankInfo("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_info_type_day), userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_info_type_week), userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_info_type_month), userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
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

    private void dealResultList(List<RankIndexBean> list, String subCategory, List<UserInfoBean> listUser) {
        RankIndexBean rankIndexBean = new RankIndexBean();
        rankIndexBean.setCategory(mRootView.getCategory());
        rankIndexBean.setSubCategory(subCategory);
        if (!listUser.isEmpty()){
            rankIndexBean.setUserInfoList(listUser);
            list.add(rankIndexBean);
        }
    }

    @Override
    public List<RankIndexBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RankIndexBean> data, boolean isLoadMore) {
        return false;
    }
}
