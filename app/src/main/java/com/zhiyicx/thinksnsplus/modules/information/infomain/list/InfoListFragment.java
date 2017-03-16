package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoBannerBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
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
        BaseListBean> implements InfoMainContract.InfoListView {
    public static final String BUNDLE_INFO_TYPE = "info_type";
    private List<BaseListBean> mInfoList = new ArrayList<>();
    private String mInfoType = "1";
    private ImageLoader mImageLoader;

    public static InfoListFragment newInstance(String params) {
        InfoListFragment fragment = new InfoListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    InfoListPresenter mInfoListPresenter;

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mInfoList);
        adapter.addItemViewDelegate(new InfoBannerItem());
        adapter.addItemViewDelegate(new InfoListItem() {
            @Override
            public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                                final int position) {
                InfoListBean.ListBean realData = (InfoListBean.ListBean) baseListBean;
                final TextView title = holder.getView(R.id.item_info_title);
                ImageView imageView = holder.getView(R.id.item_info_imag);
                if (AppApplication.sOverRead.contains(position + "")) {
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                }
                title.setText(realData.getTitle());
                String url = String.format(ApiConfig.IMAGE_PATH, realData.getStorage().getId(), 50);
                mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                        .url(url)
                        .imagerView(imageView)
                        .build());
                holder.setText(R.id.item_info_timeform, TimeUtils.getTimeFriendlyNormal(realData
                        .getUpdated_at()));

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
        mInfoType = getArguments().getString(BUNDLE_INFO_TYPE, "1");
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        super.initData();
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public String getInfoType() {
        return mInfoType;
    }


    @Override
    public void setPresenter(InfoMainContract.InfoListPresenter presenter) {
        mPresenter = presenter;
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
    protected List requestCacheData(Long maxId, boolean isLoadMore) {
        return null;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        super.requestNetData(maxId, isLoadMore);
    }

    @Override
    protected Long getMaxId(@NotNull List<BaseListBean> data) {
        InfoListBean.ListBean needData=(InfoListBean.ListBean)data.get(data.size() - 1);
        return (long) needData.getId();
    }
}
