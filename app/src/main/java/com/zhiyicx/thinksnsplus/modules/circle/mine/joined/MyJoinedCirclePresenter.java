package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.circle.all_circle.CircleListContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class MyJoinedCirclePresenter extends AppBasePresenter<MyJoinedCircleContract.Repository, MyJoinedCircleContract.View>
        implements CircleListContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;

    @Inject
    public MyJoinedCirclePresenter(MyJoinedCircleContract.Repository repository, MyJoinedCircleContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mRepository.getMyJoinedCircle(TSListFragment.DEFAULT_PAGE_SIZE, maxId.intValue())
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
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage("圈子还在审核啊");
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;
        if (isJoined) {
            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
        } else {
            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);
        }
        // 更改数据源，切换订阅状态
        circleInfo.setJoined(new CircleInfo.JoinedBean());
        mCircleInfoGreenDao.updateSingleData(circleInfo);
        mRepository.dealCircleJoinOrExit(circleInfo);
        mRootView.refreshData(position);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        mCircleInfoGreenDao.saveMultiData(data);
        return isLoadMore;
    }
}
