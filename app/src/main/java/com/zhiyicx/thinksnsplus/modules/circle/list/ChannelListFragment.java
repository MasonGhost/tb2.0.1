package com.zhiyicx.thinksnsplus.modules.circle.list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.circle.list.ChannelListViewPagerFragment.PAGE_ALL_CHANNEL_LIST;

/**
 * @author LiuChao
 * @describe 页面数据加载逻辑：如果本地数据为空，则从网络加载数据，否则，本次加载上次网络请求的数据，本次的网络请求数据会保存到数据库中，供下次显示
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */

public class ChannelListFragment extends TSListFragment<ChannelListContract.Presenter, GroupInfoBean>
        implements ChannelListContract.View {

    @Inject
    ChannelListPresenter mChannelListPresenter;

    private int isFirst;// 记录请求次数，判断是不是第一次进入界面

    private int pageType = 0;// 上一个Fragment传递过来的页面类型

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ChannelListFragmentAdapter(getContext()
                , R.layout.item_channel_list, mListDatas, mPresenter);
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    public boolean handleTouristControl() {
        return mPresenter.handleTouristControl();
    }

    @Override
    protected void initView(View rootView) {
        DaggerChannelListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .channelListPresenterModule(new ChannelListPresenterModule(this))
                .build().inject(this);
        super.initView(rootView);
        //initAdvert();
    }

    private void initAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            return;
        }

        ChannelAdvertHeader advertHeader = new ChannelAdvertHeader(getActivity(), mPresenter.getAdvert());
        mHeaderAndFooterWrapper.addHeaderView(advertHeader.getRootView());
    }

    /**
     * 内容区域在 viewpager 中
     */
    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }


    @Override
    public void onNetResponseSuccess(List<GroupInfoBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        closeLoadingView();
        if (mListDatas.isEmpty() && isFirst == 0) {
            // 如果界面上没有显示数据，从网络获取后界面上仍然没有数据，就切换到所有频道的页面
            if (data == null || data.isEmpty()) {
                ChannelListViewPagerFragment channelListViewPagerFragment = (ChannelListViewPagerFragment) getParentFragment();
                if (channelListViewPagerFragment != null) {
                    channelListViewPagerFragment.setSelectPager(PAGE_ALL_CHANNEL_LIST);
                }
            }
        }
        isFirst = 1;
    }

    @Override
    public void onCacheResponseSuccess(@NotNull List<GroupInfoBean> data, boolean isLoadMore) {
        super.onCacheResponseSuccess(data, isLoadMore);
        if (mListDatas.isEmpty()) {
            // 数据库数据为空，还需要从网络请求数据，这时还不能够关闭loadingview
        } else {
            closeLoadingView();
        }
    }

    @Override
    protected boolean isRefreshEnable() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    @Override
    protected void initData() {
        pageType = getArguments().getInt(ChannelListViewPagerFragment.PAGE_TYPE);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    public static ChannelListFragment newInstance(Bundle bundle) {
        ChannelListFragment channelListFragment = new ChannelListFragment();
        channelListFragment.setArguments(bundle);
        return channelListFragment;
    }

    @Override
    public int getPageType() {
        return pageType;
    }

    @Override
    public void refreshSubscribState(int position) {
        refreshData(position);
    }

    @Override
    public void refreshSubscribState() {
        refreshData();
    }

    @Override
    public List<ChannelSubscripBean> getChannelListData() {
        return null;
    }

    @Override
    public List<GroupInfoBean> getGroupList() {
        return mListDatas;
    }


    @Override
    public void gotoAllChannel() {
        ChannelListViewPagerFragment channelListViewPagerFragment = (ChannelListViewPagerFragment) getParentFragment();
        if (channelListViewPagerFragment != null) {
            channelListViewPagerFragment.setSelectPager(PAGE_ALL_CHANNEL_LIST);
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_GROUP_JOIN)
    public void changeJoinState(GroupInfoBean groupInfoBean) {
        // 如果是自己关注的列表 则去掉该项
        boolean hasItem = false;
        for (int i = 0; i < mListDatas.size(); i++) {
            if (mListDatas.get(i).getId() == groupInfoBean.getId()) {
                if (pageType == ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST
                        && groupInfoBean.getIs_member() == 0) {
                    // 如果是自己关注的列表 则去掉该项
                    mListDatas.remove(i);
                } else {
                    mListDatas.set(i, groupInfoBean);
                }
                hasItem = true;
                break;
            } else {
                hasItem = false;
            }
        }
        if (pageType == ChannelListViewPagerFragment.PAGE_MY_SUBSCRIB_CHANNEL_LIST
                && groupInfoBean.getIs_member() == 1 && !hasItem) {
            mListDatas.add(groupInfoBean);
        }
        refreshData(mListDatas);
    }
}
