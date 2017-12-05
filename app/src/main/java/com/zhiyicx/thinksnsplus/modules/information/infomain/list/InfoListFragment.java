package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicListAdvert;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerHeader;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.RECOMMEND_INFO;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListFragment extends TSListFragment<InfoMainContract.InfoListPresenter,
        BaseListBean> implements InfoMainContract.InfoListView, InfoBannerHeader.InfoBannerHeadlerClickEvent {

    private String mInfoType = RECOMMEND_INFO;

    private List<RealAdvertListBean> mListAdvert;
    private List<RealAdvertListBean> mHeaderAdvert;

    private InfoBannerHeader mInfoBannerHeader;

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
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        try {// 添加广告
            if (!data.isEmpty()) {
                RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
                DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
                long max_id = data.get(data.size() - 1).getMaxId();
                data.add(DynamicListAdvert.advert2Info(advert, max_id));
            }
        } catch (Exception e) {
        }
        super.onNetResponseSuccess(data, isLoadMore);
        if (mInfoBannerHeader == null) {
            return;
        }
        if (!isLoadMore && data.isEmpty()) {
            mInfoBannerHeader.getInfoBannerHeader().setVisibility(View.GONE);
        } else {
            mInfoBannerHeader.getInfoBannerHeader().setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected boolean showEmptyViewWithNoData() {
        return true;
    }

    @Override
    public void onCacheResponseSuccess(List<BaseListBean> data, boolean isLoadMore) {
        try {// 添加广告
            if (!data.isEmpty()) {
                RealAdvertListBean realAdvertListBean = mListAdvert.get(getPage() - 1);
                DynamicListAdvert advert = realAdvertListBean.getAdvertFormat().getAnalog();
                long max_id = data.get(data.size() - 1).getMaxId();
                data.add(DynamicListAdvert.advert2Info(advert, max_id));
            }
        } catch (Exception e) {
        }
        super.onCacheResponseSuccess(data, isLoadMore);
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new InfoListItem(false) {
            @Override
            public void itemClick(int position, ImageView imageView, TextView title, InfoListDataBean realData) {
                if (TouristConfig.INFO_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    if (!AppApplication.sOverRead.contains(realData.getId())) {
                        AppApplication.sOverRead.add(realData.getId().intValue());
                    }
                    FileUtils.saveBitmapToFile(getActivity(), ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                            , imageView.getDrawable(), R.mipmap.icon), "info_share");
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                    Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_INFO, realData);
                    bundle.putString(BUNDLE_INFO_TYPE, mInfoType);
                    intent.putExtra(BUNDLE_INFO, bundle);
                    startActivity(intent);
                }
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
        initAdvert();
        super.initData();
    }

    private void initAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            return;
        }
        if (!mInfoType.equals("-1")) {// 只有推荐才加载广告
            return;
        }
        List<String> advertTitle = new ArrayList<>();
        List<String> advertUrls = new ArrayList<>();
        List<String> advertLinks = new ArrayList<>();
        List<RealAdvertListBean> advertList = mPresenter.getBannerAdvert();
        mListAdvert = mPresenter.getListAdvert();
        mHeaderAdvert = mPresenter.getBannerAdvert();
        for (RealAdvertListBean advert : advertList) {
            advertTitle.add(advert.getTitle());
            advertUrls.add(advert.getAdvertFormat().getImage().getImage());
            advertLinks.add(advert.getAdvertFormat().getImage().getLink());
            if (advert.getType().equals("html")) {
                showStickyHtmlMessage((String) advert.getData());
            }
        }

        mInfoBannerHeader = new InfoBannerHeader(getActivity());
        mInfoBannerHeader.setHeadlerClickEvent(this);
        InfoBannerHeader.InfoBannerHeaderInfo headerInfo = mInfoBannerHeader.new
                InfoBannerHeaderInfo();
        headerInfo.setTitles(advertTitle);
        headerInfo.setLinks(advertLinks);
        headerInfo.setUrls(advertUrls);
        headerInfo.setDelay(4000);
        headerInfo.setOnBannerListener(position -> {

        });
        mInfoBannerHeader.setHeadInfo(headerInfo);
        mHeaderAndFooterWrapper.addHeaderView(mInfoBannerHeader.getInfoBannerHeader());
    }

    @Override
    public void headClick(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.autoRefresh();
    }

    @Override
    public String getInfoType() {
        return mInfoType;
    }

    @Override
    public int isRecommend() {
        return mInfoType.equals(RECOMMEND_INFO) ? 1 : 0;
    }


    @Override
    public void setPresenter(InfoMainContract.InfoListPresenter presenter) {
        mPresenter = presenter;
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

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_INFO_LIST_COLLECT)
    public void handleCollectInfo(InfoListDataBean info) {
        LogUtils.d("handleCollectInfo");
//        onCacheResponseSuccess(requestCacheData(mMaxId, false), false);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_INFO_LIST_DELETE_UPDATE)
    public void handleDeleteInfo(InfoListDataBean info) {
        LogUtils.d("handleDeleteInfo");
        mListDatas.remove(mListDatas.indexOf(info));
        refreshData();
    }

    public void setInfoType(String infoType) {
        mInfoType = infoType;
    }
}
