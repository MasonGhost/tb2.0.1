package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DigListPresenter extends AppBasePresenter<DigListContract.Repository, DigListContract.View> implements DigListContract.Presenter {
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public DigListPresenter(DigListContract.Repository repository, DigListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public List<FollowFansBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data, boolean isLoadMore) {
        DynamicDetailBeanV2 dynamicBean = mRootView.getDynamicBean();
        dynamicBean.setDigUserInfoList(data);
        return mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean) >= 0;
    }

    @Override
    public void handleFollowUser(int position, FollowFansBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDataFollowState(position);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore, long feed_id) {
        mRepository.getDynamicDigListV2(feed_id, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<FollowFansBean>>() {
                    @Override
                    protected void onSuccess(List<FollowFansBean> data) {
                        LogUtils.i("digList_netData" + data.toString());
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, DynamicDetailBeanV2 dynamicBean) {
        return dynamicBean.getDigUserInfoList();
    }
}
