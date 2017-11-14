package com.zhiyicx.thinksnsplus.modules.collect.group_posts;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailPresenter;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailPresenterModule;
import com.zhiyicx.thinksnsplus.modules.circle.detail.adapter.GroupDynamicListBaseItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/07/24/10:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CollectGroupDynamicListFragment extends ChannelDetailFragment {

    @Inject
    ChannelDetailPresenter mChannelDetailPresenter;

    @Override
    protected boolean isNeedRefreshAnimation() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected boolean isRefreshEnable() {
        return true;
    }

    @Override
    protected boolean setHeadShow() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoadingAnimation() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mLlToolbarContainerParent.setVisibility(View.GONE);
        mBtnSendDynamic.setVisibility(View.GONE);
    }

    @Override
    protected Long getMaxId(@NotNull List<GroupDynamicListBean> data) {
        return (long)mListDatas.size();
    }

    @Override
    protected void onEmptyViewClick() {
        super.onEmptyViewClick();
    }

    @Override
    protected void initData() {
        DaggerCollectGroupDynamicPresenterComonent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .shareModule(new ShareModule(getActivity()))
                .channelDetailPresenterModule(new ChannelDetailPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected void setAdapter(MultiItemTypeAdapter adapter, GroupDynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
        dynamicListBaseItem.setShowCommentList(false)
                .setShowReSendBtn(false)
                .setShowToolMenu(false);
    }

    public static CollectGroupDynamicListFragment newInstance(Bundle bundle) {
        CollectGroupDynamicListFragment channelDetailFragment = new CollectGroupDynamicListFragment();
        channelDetailFragment.setArguments(bundle);
        return channelDetailFragment;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_GROUP_COLLECTION)
    public void deleteCollection(Bundle bundle) {
        if (bundle != null){
            GroupDynamicListBean dynamicBean = bundle.getParcelable(EventBusTagConfig.EVENT_UPDATE_GROUP_COLLECTION);
            if (dynamicBean != null){
                for (GroupDynamicListBean dynamicBeanItem : mListDatas){
                    if (dynamicBeanItem.getId().equals(dynamicBean.getId())){
                        mListDatas.remove(dynamicBeanItem);
                        break;
                    }
                }
                if (mListDatas.isEmpty()) {// 添加暂未图
                    mListDatas.add(new GroupDynamicListBean());
                }
                refreshData();
            }
        }
    }


}
