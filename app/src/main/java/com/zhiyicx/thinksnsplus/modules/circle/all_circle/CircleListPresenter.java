package com.zhiyicx.thinksnsplus.modules.circle.all_circle;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

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
    public CircleListPresenter(CircleListContract.Repository repository, CircleListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getCircleList(mRootView.getCategoryId(), maxId)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {

                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        return false;
    }
}
