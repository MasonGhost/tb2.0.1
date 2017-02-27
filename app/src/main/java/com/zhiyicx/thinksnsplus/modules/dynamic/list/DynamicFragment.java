package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 动态列表
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements DynamicContract.View, DynamicListMenuView.OnItemClickListener, DynamicListBaseItem.OnImageClickListener, DynamicListBaseItem.OnUserInfoClickListener {
    private static final String BUNDLE_DYNAMIC_TYPE = "dynamic_type";
    public static final long ITEM_SPACING = 5L; // 单位dp
    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造

    private String mDynamicType = ApiConfig.DYNAMIC_TYPE_NEW;


    private List<DynamicBean> mDynamicBeens = new ArrayList<>();

    public static DynamicFragment newInstance(String dynamicType) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    @Override
    protected boolean getPullDownRefreshEnable() {
        return true;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        setAdapter(adapter, new DynamicListBaseItem(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        return adapter;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuClick(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }


    @Override
    protected void initData() {
        DaggerDynamicComponent // 在 super.initData();之前，因为initdata 会使用到 presenter
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicPresenterModule(new DynamicPresenterModule(this))
                .build().inject(this);
        mDynamicType = getArguments().getString(BUNDLE_DYNAMIC_TYPE);
        super.initData();
    }

    @Override
    public void setPresenter(DynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
        mRefreshlayout.setLoadingMore(false);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showToast(message);
    }

    @Override
    public void onImageClick(DynamicBean dynamicBean, int position) {
        showMessage(position + "");
    }

    @Override
    public void onUserInfoClick(DynamicBean dynamicBean) {
        showMessage(dynamicBean.getUserInfoBean().getName());
    }

    @Override
    public String getDynamicType() {
        return mDynamicType;
    }

    @Override
    public List<DynamicBean> getDatas() {
        return mDynamicBeens;
    }

    @Override
    public void refresh() {
        refreshData();
    }

    @Override
    public void refresh(int position) {
        LogUtils.d(TAG, "mDynamicBeens    position  = " + mDynamicBeens.toString());
        refreshData(position);
    }

    @Override
    public void onItemClick(ViewGroup parent, View v, int postion) {
        if (postion == 0) { // 点击喜欢
            ToastUtils.showToast("点击了喜欢");
        }
    }
}
