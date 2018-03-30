package com.zhiyicx.thinksnsplus.modules.tb.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListPresenterModule;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.DaggerContributionListComponent;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchMechanismUserActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 机构消息详情列表页
 * @Author Jungle68
 * @Date 2018/3/23
 * @Contact master.jungle68@gmail.com
 */
public class MerchainMessageListFragment extends TSListFragment<MerchainMessageListContract.Presenter, MerchianMassageBean.DataBean>
        implements MerchainMessageListContract.View {
    private static final String BUNDLE_DATA_USER = "merchain_user";
    @Inject
    MerchainMessageListPresenter mContributionListPresenter;

    private UserInfoBean mUserInfoBean;

    private boolean mIsNeedScrollToBottom;

    public static MerchainMessageListFragment newInstance(UserInfoBean userInfoBean) {
        MerchainMessageListFragment contributionListFragment = new MerchainMessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_DATA_USER, userInfoBean);
        contributionListFragment.setArguments(bundle);
        return contributionListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMerchainMessageListComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .merchianMessageListPresenterModule(new MerchianMessageListPresenterModule(this))
                .build().inject(this);
        mUserInfoBean = (UserInfoBean) getArguments().getSerializable(BUNDLE_DATA_USER);
    }

    @Override
    protected void initData() {
        super.initData();
        setCenterText(mUserInfoBean.getName());
        mRvList.setPadding(0, 0, 0, 50);
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    public void scroollToBottom() {
        if (mListDatas.size() > 0) {
            mRvList.scrollToPosition(mListDatas.size() - 1);
        }
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.topbar_more_white;
    }

    @Override
    protected void setRightClick() {
        PersonalCenterFragment.startToPersonalCenter(getContext(), mUserInfoBean);
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    /**
     * 处理服务器或者缓存中拿到的数据
     *
     * @param data       返回的数据
     * @param isLoadMore 是否是加载更多
     */
    @Override
    protected void handleReceiveData(List<MerchianMassageBean.DataBean> data, boolean isLoadMore, boolean isFromCache) {

        mIsNeedScrollToBottom = mListDatas.isEmpty();
        // 刷新
        if (!isLoadMore) {

            mTvNoMoredataText.setVisibility(View.GONE);
            if (isLoadingMoreEnable()) {
                mRefreshlayout.setEnableLoadmore(true);
            }
            mListDatas.clear();
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, false);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                mMaxId = getMaxId(data);
                refreshData();

            } else {
                layzLoadEmptyView();
                mEmptyView.setErrorImag(setEmptView());
                refreshData();
            }
        } else { // 加载更多
            if (data != null && data.size() != 0) {
                if (!isFromCache) {
                    // 更新缓存
                    mPresenter.insertOrUpdateData(data, true);
                }
                // 内存处理数据
                mListDatas.addAll(data);
                try {
                    refreshRangeData(mListDatas.size() - data.size() - 1, data.size());
                } catch (Exception e) {
                    refreshData();
                }
                mMaxId = getMaxId(data);
            }
        }
        // 数据加载后，所有的数据数量小于一页，说明没有更多数据了，就不要上拉加载了(除开缓存)
        if (!isFromCache && (data == null || data.size() < getPagesize())) {
            mRefreshlayout.setEnableLoadmore(false);
            // mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE 当前数量大于一页显示数量时，显示加载更多
            if (showNoMoreData()) {
                mTvNoMoredataText.setVisibility(View.VISIBLE);
            }
        }
        if (mIsNeedScrollToBottom) {
            scroollToBottom();
            mIsNeedScrollToBottom = false;
        }
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        MerchainMessageListItemDynamic merchainMessageListItemDynamic = new MerchainMessageListItemDynamic(mUserInfoBean);
        MerchainMessageListItemNews merchainMessageListItemNews = new MerchainMessageListItemNews(mUserInfoBean);
        adapter.addItemViewDelegate(merchainMessageListItemNews);
        adapter.addItemViewDelegate(merchainMessageListItemDynamic);

        return adapter;
    }

    @Override
    public Integer getOriginId() {
        return mUserInfoBean.getUser_id().intValue();
    }
}
