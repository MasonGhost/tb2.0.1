package com.zhiyicx.thinksnsplus.modules.circle.mine;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseChannelRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
/**
 * @Describe 我的圈子列表
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MyGroupPresenter extends AppBasePresenter< MyGroupContract.View> implements MyGroupContract.Presenter {

    @Inject
    GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;
    @Inject
    BaseChannelRepository mBaseChannelRepository;

    @Inject
    public MyGroupPresenter(MyGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseChannelRepository.getGroupList(1, maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<GroupInfoBean>>() {
                    @Override
                    protected void onSuccess(List<GroupInfoBean> data) {
                        if (!data.isEmpty()) {
                            for (GroupInfoBean groupInfoBean : data) {
                                groupInfoBean.setIs_member(1);
                            }
                        }
                        mGroupInfoBeanGreenDao.saveMultiData(data);
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
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        List<GroupInfoBean> list = mGroupInfoBeanGreenDao.getUserJoinedGroup();
        mRootView.onCacheResponseSuccess(list,isLoadMore);
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
        mBaseChannelRepository.handleGroupJoin(groupInfoBean);
        mRootView.updateGroupJoinState(position, groupInfoBean);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB)
    public void updateList(GroupInfoBean groupInfoBean) {
        if (groupInfoBean != null) {
            int position = -1;
            for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                if (groupInfoBean.getId() == mRootView.getListDatas().get(i).getId()) {
                    position = i;
                    break;
                }
            }
            // 取消关注，看列表是否有 有就移除
            if (groupInfoBean.getIs_member() == 0 && position != -1){
                mRootView.getListDatas().remove(position);
            }
            // 加关注 看列表是否有 没有才添加
            if (groupInfoBean.getIs_member() == 1 && position == -1){
                mRootView.getListDatas().add(groupInfoBean);
            }
            mRootView.refreshData();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
