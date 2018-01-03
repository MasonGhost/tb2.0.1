package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupDynamicListBeanGreenDaoimpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseChannelRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class GroupDigListPresenter extends AppBasePresenter< GroupDigListContract.View>
        implements GroupDigListContract.Presenter{

    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    BaseChannelRepository mBaseChannelRepository;
    @Inject
    GroupDynamicListBeanGreenDaoimpl mGroupDynamicListBeanGreenDaoimpl;

    @Inject
    public GroupDigListPresenter( GroupDigListContract.View rootView) {
        super( rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mBaseChannelRepository.getGroupDynamicDigList(mRootView.getDynamicBean().getGroup_id(), mRootView.getDynamicBean()
                .getId(), maxId)
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
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDigListBean> data, boolean isLoadMore) {
        GroupDynamicListBean dynamicBean = mRootView.getDynamicBean();
        dynamicBean.setMGroupDynamicLikeListBeanList(data);
        return mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(dynamicBean) >= 0;
    }

    @Override
    public void handleFollowUser(int position, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        // 本地数据库和ui进行刷新
        mRootView.upDataFollowState(position);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore, long feed_id) {

    }

    @Override
    public List<DynamicDigListBean> requestCacheData(Long maxId, boolean isLoadMore, GroupDynamicListBean dynamicBean) {
        return dynamicBean.getMGroupDynamicLikeListBeanList();
    }
}
