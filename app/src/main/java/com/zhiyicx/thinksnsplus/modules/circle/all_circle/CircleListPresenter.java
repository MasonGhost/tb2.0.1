package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/11/21/16:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListPresenter extends AppBasePresenter<CircleListContract.Repository, CircleListContract.View>
        implements CircleListContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;

    @Inject
    public CircleListPresenter(CircleListContract.Repository repository, CircleListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getCircleList(mRootView.getCategoryId(), maxId)
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

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mCircleInfoGreenDao.getCircleListByCategory(mRootView.getCategoryId()), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        mCircleInfoGreenDao.saveMultiData(data);
        return isLoadMore;
    }
}
