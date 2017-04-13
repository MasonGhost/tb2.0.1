package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.information.infomain.container
        .InfoContainerFragment.RECOMMEND_INFO;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListFragment extends TSListFragment<InfoMainContract.InfoListPresenter,
        BaseListBean> implements InfoMainContract.InfoListView {
    public static final String BUNDLE_INFO_TYPE = "info_type";
    public static final String BUNDLE_INFO = "info";
    private String mInfoType = RECOMMEND_INFO;

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
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new InfoBannerItem());
        adapter.addItemViewDelegate(new InfoListItem() {
            @Override
            public void itemClick(int position, TextView title, InfoListBean.ListBean realData) {
                if (!AppApplication.sOverRead.contains(position + "")) {
                    AppApplication.sOverRead.add(position + "");
                }
                title.setTextColor(getResources()
                        .getColor(R.color.normal_for_assist_text));
                Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_INFO, realData);
                bundle.putString(BUNDLE_INFO_TYPE, mInfoType);
                intent.putExtra(BUNDLE_INFO, bundle);
                startActivity(intent);
            }
        });

        return adapter;
    }

    @Override
    protected void initData() {
        DaggerInfoListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoListPresenterModule(new InfoListPresenterModule(this))
                .build()
                .inject(this);
        mInfoType = getArguments().getString(BUNDLE_INFO_TYPE, RECOMMEND_INFO);
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
        showMessageNotSticky(message);
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
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected Long getMaxId(@NotNull List<BaseListBean> data) {
        InfoListBean.ListBean needData = (InfoListBean.ListBean) data.get(data.size() - 1);
        return (long) needData.getId();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_INFO_LIST_COLLECT)
    public void handleCollectInfo(InfoListBean.ListBean info) {
        LogUtils.d("handleCollectInfo");
        onCacheResponseSuccess(requestCacheData(mMaxId, false), false);
    }

    public void setInfoType(String infoType) {
        mInfoType = infoType;
    }
}
