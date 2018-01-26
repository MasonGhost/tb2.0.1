package com.zhiyicx.thinksnsplus.modules.rank.main.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.RankIndexBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseRankRepository;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig;

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
public class RankListPresenter extends AppBasePresenter< RankListContract.View>
        implements RankListContract.Presenter {

    @Inject
    RankIndexBeanGreenDaoImpl mRankIndexBeanGreenDao;
    
    @Inject
    BaseRankRepository mBaseRankRepository;

    @Inject
    public RankListPresenter(RankListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        String category = mRootView.getCategory();
        if (category.equals(mContext.getString(R.string.rank_user))) {
            requestNetDataUser(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_qa))) {
            requestNetDataQuestion(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_dynamic))) {
            requestNetDataDynamic(isLoadMore);
        } else if (category.equals(mContext.getString(R.string.rank_info))) {
            requestNetDataInfo(isLoadMore);
        }
    }

    private void requestNetDataUser(boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseRankRepository.getRankFollower(0)
                , mBaseRankRepository.getRankRiches(0)
                , mBaseRankRepository.getRankIncome(0)
                , mBaseRankRepository.getRankCheckIn(0)
                , mBaseRankRepository.getRankQuestionExpert(0)
                , mBaseRankRepository.getRankQuestionLikes(0),
                (userInfoFollower, userInfoRiches, userInfoIncome, userInfoCheckIn, userInfoExpert, userInfoQuestionLike) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_user_type_all), RankTypeConfig.RANK_USER_FOLLOWER, userInfoFollower);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_riches), RankTypeConfig.RANK_USER_RICHES, userInfoRiches);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_income), RankTypeConfig.RANK_USER_INCOME, userInfoIncome);
                    SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig
                            .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
                    // 如果已经签到了，则不再展示签到
                    if (systemConfigBean != null && systemConfigBean.isCheckin()) {
                        dealResultList(list, mContext.getString(R.string.rank_user_type_sign_in), RankTypeConfig.RANK_USER_CHECK_ID, userInfoCheckIn);
                    }
                    dealResultList(list, mContext.getString(R.string.rank_user_type_expert), RankTypeConfig.RANK_USER_EXPERT, userInfoExpert);
                    dealResultList(list, mContext.getString(R.string.rank_user_type_qa), RankTypeConfig.RANK_USER_QUESTION_LIKE,
                            userInfoQuestionLike);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
                        mRankIndexBeanGreenDao.saveMultiData(data);
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

    private void requestNetDataQuestion(boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseRankRepository.getRankAnswer("day", 0)
                , mBaseRankRepository.getRankAnswer("week", 0)
                , mBaseRankRepository.getRankAnswer("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_day), RankTypeConfig.RANK_QUESTION_DAY, userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_week), RankTypeConfig.RANK_QUESTION_WEEK, userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_qa_type_month), RankTypeConfig.RANK_QUESTION_MONTH, userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
                        mRankIndexBeanGreenDao.saveMultiData(data);
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

    private void requestNetDataDynamic(boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseRankRepository.getRankDynamic("day", 0)
                , mBaseRankRepository.getRankDynamic("week", 0)
                , mBaseRankRepository.getRankDynamic("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_day), RankTypeConfig.RANK_DYNAMIC_DAY, userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_week), RankTypeConfig.RANK_DYNAMIC_WEEK, userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_dynamic_type_month), RankTypeConfig.RANK_DYNAMIC_MONTH, userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
                        mRankIndexBeanGreenDao.saveMultiData(data);
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

    private void requestNetDataInfo(boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseRankRepository.getRankInfo("day", 0)
                , mBaseRankRepository.getRankInfo("week", 0)
                , mBaseRankRepository.getRankInfo("month", 0),
                (userInfoDay, userInfoWeek, userInfoMonth) -> {
                    List<RankIndexBean> list = new ArrayList<RankIndexBean>();
                    dealResultList(list, mContext.getString(R.string.rank_info_type_day), RankTypeConfig.RANK_INFORMATION_DAY, userInfoDay);
                    dealResultList(list, mContext.getString(R.string.rank_info_type_week), RankTypeConfig.RANK_INFORMATION_WEEK, userInfoWeek);
                    dealResultList(list, mContext.getString(R.string.rank_info_type_month), RankTypeConfig.RANK_INFORMATION_MONTH, userInfoMonth);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<RankIndexBean>>() {

                    @Override
                    protected void onSuccess(List<RankIndexBean> data) {
                        mRankIndexBeanGreenDao.saveMultiData(data);
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

    private void dealResultList(List<RankIndexBean> list, String subCategory, String type, List<UserInfoBean> listUser) {
        RankIndexBean rankIndexBean = new RankIndexBean();
        rankIndexBean.setCategory(mRootView.getCategory());
        rankIndexBean.setSubCategory(subCategory);
        rankIndexBean.setType(type);
        if (!listUser.isEmpty()) {
            rankIndexBean.setUserInfoList(listUser);
            list.add(rankIndexBean);
        }
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        List<RankIndexBean> list = mRankIndexBeanGreenDao.getIndexRankList(mRootView.getCategory());
        mRootView.onCacheResponseSuccess(list,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RankIndexBean> data, boolean isLoadMore) {
        return false;
    }
}
