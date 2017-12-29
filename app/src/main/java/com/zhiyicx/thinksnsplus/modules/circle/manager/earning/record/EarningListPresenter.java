package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningContract;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/06/05/10:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class EarningListPresenter extends AppBasePresenter<EarningListContract.View> implements EarningListContract.Presenter {

    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    public EarningListPresenter(EarningListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
//        getCircleEarningList(long circleId, long start, long end,\long after, long limit, String type)
        Subscription subscribe = mBaseCircleRepository.getCircleEarningList(mRootView.getCircleId(), mRootView.getStartTime(), mRootView.getEndTime
                        (), maxId,
                (long) TSListFragment.DEFAULT_PAGE_SIZE, mRootView.getType())
                .subscribe(new BaseSubscribeForV2<List<CircleEarningListBean>>() {
                    @Override
                    protected void onSuccess(List<CircleEarningListBean> data) {
                        removeAction(data, mRootView.getType());
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
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleEarningListBean> data, boolean isLoadMore) {
        return true;
    }

    @Override
    public void selectBillByAction(int action) {
    }

    @Override
    public void selectAll() {
        requestCacheData(1L, false);
    }

    public void removeAction(List<CircleEarningListBean> list, String action) {

//        Iterator<CircleEarningListBean> rechargesIterator = list.iterator();
//        while (rechargesIterator.hasNext()) {
//            CircleEarningListBean data = rechargesIterator.next();
//            if (data.getAction() != action) {
//                rechargesIterator.remove();
//            }
//        }
    }
}
