package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class DigListPresenter extends AppBasePresenter<DigListContract.View> implements DigListContract.Presenter {
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    BaseDynamicRepository mBaseDynamicRepository;

    @Inject
    public DigListPresenter(DigListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDigListBean> data, boolean isLoadMore) {
        DynamicDetailBeanV2 dynamicBean = mRootView.getDynamicBean();
        dynamicBean.setDigUserInfoList(data);
        return mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean) >= 0;
    }

    @Override
    public void handleFollowUser(int position, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDataFollowState(position);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore, long feed_id) {
        Subscription subscribe = mBaseDynamicRepository.getDynamicDigListV2(feed_id, maxId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<DynamicDigListBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicDigListBean> data) {
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
        addSubscrebe(subscribe);

    }

    @Override
    public List<DynamicDigListBean> requestCacheData(Long maxId, boolean isLoadMore, DynamicDetailBeanV2 dynamicBean) {
        List<DynamicDigListBean> data = dynamicBean.getDigUserInfoList();
        mRootView.onCacheResponseSuccess(dynamicBean.getDigUserInfoList(), isLoadMore);
        return data;
    }
}
