package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListRecycleItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements DynamicContract.View, DynamicListBaseItem.OnImageClickListener, DynamicListBaseItem.OnUserInfoClickListener {
    public static final long ITEM_SPACING = 5L; // 单位dp
    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造

    private List<DynamicBean> mDynamicBeens = new ArrayList<>();

    public static DynamicFragment newInstance() {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
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
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        DynamicListRecycleItem dynamicListBaseItem = new DynamicListRecycleItem(getContext());
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
        DynamicListItemForFiveImage dynamicListItemForFiveImage = new DynamicListItemForFiveImage(getContext());
        dynamicListItemForFiveImage.setOnImageClickListener(this);
        dynamicListItemForFiveImage.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListItemForFiveImage);
        DynamicListItemForSixImage dynamicListItemForSixImage = new DynamicListItemForSixImage(getContext());
        dynamicListItemForSixImage.setOnImageClickListener(this);
        dynamicListItemForSixImage.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListItemForSixImage);
        DynamicListItemForSevenImage dynamicListItemForSevenImage = new DynamicListItemForSevenImage(getContext());
        dynamicListItemForSevenImage.setOnImageClickListener(this);
        dynamicListItemForSevenImage.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListItemForSevenImage);
        return adapter;
    }


    @Override
    protected void initData() {
        DaggerDynamicComponent // 在 super.initData();之前，因为initdata 会使用到 presenter
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicPresenterModule(new DynamicPresenterModule(this))
                .build().inject(this);
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
        mRefreshlayout.endRefreshing();
        mRefreshlayout.endLoadingMore();
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
}
