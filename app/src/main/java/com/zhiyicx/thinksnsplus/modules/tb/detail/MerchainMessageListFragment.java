package com.zhiyicx.thinksnsplus.modules.tb.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.tbmerchianmessage.MerchianMassageBean;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionListPresenterModule;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.DaggerContributionListComponent;
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
