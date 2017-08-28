package com.zhiyicx.thinksnsplus.modules.channel.mine;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyGroupPresenter extends AppBasePresenter<MyGroupContract.Repository, MyGroupContract.View> implements MyGroupContract.Presenter{

    @Inject
    GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;

    @Inject
    public MyGroupPresenter(MyGroupContract.Repository repository, MyGroupContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getGroupList(1, maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<GroupInfoBean>>() {
                    @Override
                    protected void onSuccess(List<GroupInfoBean> data) {
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

    @Override
    public List<GroupInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleGroupJoin(int position, GroupInfoBean groupInfoBean) {
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        if (isJoined) {
            // 已经订阅，变为未订阅
            groupInfoBean.setMembers_count(groupInfoBean.getMembers_count() - 1);// 订阅数-1
        } else {
            // 未订阅，变为已订阅
            groupInfoBean.setMembers_count(groupInfoBean.getMembers_count() + 1);// 订阅数+1
        }
        // 更改数据源，切换订阅状态
        groupInfoBean.setIs_member(isJoined ? 0 : 1);
        mGroupInfoBeanGreenDao.updateSingleData(groupInfoBean);
        mRepository.handleGroupJoin(groupInfoBean);
        mRootView.updateGroupJoinState(position, groupInfoBean);
    }
}
