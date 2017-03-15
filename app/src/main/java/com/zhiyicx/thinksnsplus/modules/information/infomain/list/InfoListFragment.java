package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoBannerBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.data.source.repository.InfoMainRepository;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListFragment extends TSListFragment<InfoMainContract.InfoListPresenter,
        InfoListBean> implements InfoMainContract.InfoListView {
    private static final String BUNDLE_INFO_TYPE = "info_type";
    private List<BaseListBean> mInfoList = new ArrayList<>();

    public static InfoListFragment newInstance(String params) {
        InfoListFragment fragment = new InfoListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        mInfoList.add(new InfoBannerBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        mInfoList.add(new InfoListBean());
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mInfoList);
        adapter.addItemViewDelegate(new InfoBannerItem());
        adapter.addItemViewDelegate(new InfoListItem() {
            @Override
            public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                                final int position) {

                final TextView title = holder.getView(R.id.item_info_title);

                if (AppApplication.sOverRead.contains(position + "")) {
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!AppApplication.sOverRead.contains(position + "")) {
                            AppApplication.sOverRead.add(position + "");
                        }
                        title.setTextColor(getResources()
                                .getColor(R.color.normal_for_assist_text));
                        startActivity(new Intent(getActivity(), InfoDetailsActivity.class));
                    }
                });
            }
        });
        return adapter;
    }

    @Override
    protected void initData() {
        DaggerInfoListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoListPresenterModule(new InfoListPresenterModule(this))
                .build().inject(this);


        super.initData();
    }

    @Override
    public void setInfoList(InfoListBean infoList) {

    }

    @Override
    public void setPresenter(InfoMainContract.InfoListPresenter presenter) {
        mPresenter = presenter;
        mPresenter.getInfoList("1",1,15,1);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    public void onRefresh() {
        mRefreshlayout.setRefreshing(false);
    }

    @Override
    protected List requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
//        super.requestNetData(maxId, isLoadMore);
    }
}
