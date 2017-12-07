package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainPresenter extends AppBasePresenter<CircleMainContract.Repository, CircleMainContract.View>
        implements CircleMainContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;

    @Inject
    public CircleMainPresenter(CircleMainContract.Repository repository, CircleMainContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        Subscription subscription = Observable.zip(mRepository.getCircleCount(),
                mRepository.getMyJoinedCircle(CircleMainFragment.DATALIMIT, 0),
                mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0),
                (integerBaseJsonV2, myJoinedCircle, recommendCircle) -> {

                    mRootView.updateCircleCount(integerBaseJsonV2.getData());
                    CircleInfo moreJoined = new CircleInfo();
                    moreJoined.setName(mContext.getString(R.string.joined_group));
                    moreJoined.setSummary(mContext.getString(R.string.more_group));
                    moreJoined.setId(BaseCircleItem.MYJOINEDCIRCLE);
                    CircleInfo changeCircle = new CircleInfo();
                    changeCircle.setName(mContext.getString(R.string.recommend_group));
                    changeCircle.setSummary(mContext.getString(R.string.exchange_group));
                    changeCircle.setId(BaseCircleItem.RECOMMENDCIRCLE);
                    myJoinedCircle.add(0, moreJoined);
                    myJoinedCircle.add(changeCircle);
                    mRootView.setJoinedCircles(new ArrayList<>(myJoinedCircle));
                    myJoinedCircle.addAll(recommendCircle);
                    return myJoinedCircle;
                })
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });

        addSubscrebe(subscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void getRecommendCircle() {
        Subscription subscription = mRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        List<CircleInfo> subs = new ArrayList<>(mRootView.getJoinedCircles());
                        subs.addAll(data);
                        mRootView.getListDatas().clear();
                        mRootView.getListDatas().addAll(subs);
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        if (circleInfo.getUser_id() == AppApplication.getMyUserIdWithdefault()) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.exit_circle));
            return;
        }

        mRepository.dealCircleJoinOrExit(circleInfo);
        boolean isJoined = circleInfo.getJoined() != null;
        if (isJoined) {
            circleInfo.setJoined(null);
            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
        } else {
            circleInfo.setJoined(new CircleInfo.JoinedBean());
            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);
        }
        // 更改数据源，切换订阅状态
        mCircleInfoGreenDao.updateSingleData(circleInfo);
        mRootView.refreshData(position);
    }
}
