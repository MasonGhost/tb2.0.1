package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleJoinedBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListPresenter extends AppBasePresenter<CircleListContract.View>
        implements CircleListContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;
    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    public CircleListPresenter(CircleListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Observable<List<CircleInfo>> observable;
        if (mRootView.getCategoryId() < 0) {
            observable = mBaseCircleRepository.getRecommendCircle(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue(), null);
        } else {
            observable = mBaseCircleRepository.getCircleList(mRootView.getCategoryId(), maxId);
        }
        Subscription subscription = observable
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
        addSubscrebe(subscription);
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {

        if (handleTouristControl()) {
            return;
        }
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;
        boolean isPaid = CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode());

        Observable<BaseJsonV2<Object>> observable;
        if (isPaid) {
            observable = handleWalletBlance(circleInfo.getMoney())
                    .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                            .string.pay_alert_ing)))
                    .flatMap(o -> mBaseCircleRepository.dealCircleJoinOrExit(circleInfo));
        } else {
            observable = mBaseCircleRepository.dealCircleJoinOrExit(circleInfo)
                    .doOnSubscribe(() -> {
                                mRootView.dismissSnackBar();
                                mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing));
                            }
                    );

        }
        Subscription subscription = observable
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        if (isJoined) {
                            // 退圈操作
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
                        if (isBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mCircleInfoGreenDao.getCircleListByCategory(mRootView.getCategoryId()), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        mCircleInfoGreenDao.saveMultiData(data);
        return isLoadMore;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_CIRCLE)
    public void updateCircle(CircleInfo circleInfo) {
        int index = -1;
        for (CircleInfo circle : mRootView.getListDatas()) {
            if (circle.equals(circleInfo)) {
                index = mRootView.getListDatas().indexOf(circle);
            }
        }
        if (index != -1) {
            mRootView.getListDatas().set(index, circleInfo);
        }
        mRootView.refreshData(index);
        LogUtils.d(EventBusTagConfig.EVENT_UPDATE_CIRCLE);
    }
}
