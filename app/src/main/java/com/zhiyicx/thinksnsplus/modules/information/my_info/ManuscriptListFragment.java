package com.zhiyicx.thinksnsplus.modules.information.my_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.news.PublishInfoActivityV2;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoFragment.INFO_REFUSE;

/**
 * @Author Jliuer
 * @Date 2017/08/23/11:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ManuscriptListFragment extends TSListFragment<ManuscriptListContract.Presenter, InfoListDataBean>
        implements ManuscriptListContract.View {

    public static final String MY_INFO_TYPE = "MY_INFO_TYPE";
    public static final String MY_INFO_TYPE_DONE = "0";
    public static final String MY_INFO_TYPE_ING = "1";
    public static final String MY_INFO_TYPE_ERROR = "3";

    @Inject
    ManuscriptListPresenter mManuscriptListPresenter;

    public static ManuscriptListFragment getInstance(String type) {
        ManuscriptListFragment manuscriptListFragment = new ManuscriptListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(MY_INFO_TYPE, type);
        manuscriptListFragment.setArguments(bundle);
        return manuscriptListFragment;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected void initData() {
        DaggerManuscriptListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .manuscripListPresenterModule(new ManuscripListPresenterModule(this))
                .build().inject(this);
        super.initData();
    }

    @Override
    public String getMyInfoType() {
        return getArguments().getString(MY_INFO_TYPE, MY_INFO_TYPE);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new InfoListItem(!getMyInfoType().endsWith(MY_INFO_TYPE_DONE)) {
            @Override
            public void itemClick(int position, ImageView imageView, TextView title, InfoListDataBean realData) {
                if (TouristConfig.INFO_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    if (!AppApplication.sOverRead.contains(realData.getId())) {
                        AppApplication.sOverRead.add(realData.getId().intValue());
                    }

                    if (getMyInfoType().equals(MY_INFO_TYPE_ERROR)) {
                        InfoPublishBean infoPublishBean = new InfoPublishBean();
                        infoPublishBean.setNews_id(realData.getId().intValue());
                        infoPublishBean.setSubject(realData.getSubject());
                        infoPublishBean.setTitle(realData.getTitle());
                        infoPublishBean.setAuthor(realData.getAuthor());
                        infoPublishBean.setCategoryId(realData.getCategory().getId());
                        infoPublishBean.setContent(realData.getContent());
                        infoPublishBean.setCategoryName(realData.getCategory().getName());
                        infoPublishBean.setCover(realData.getImage() == null ? -1 : realData.getImage().getId());
                        infoPublishBean.setRefuse(true);
                        infoPublishBean.setTags(realData.getTags());
                        Intent intent = new Intent(getActivity(), PublishInfoActivityV2.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(INFO_REFUSE, infoPublishBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        return;
                    }
                    FileUtils.saveBitmapToFile(getActivity(), ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                            , imageView.getDrawable(), R.mipmap.icon), "info_share");
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                    Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_INFO, realData);
                    intent.putExtra(BUNDLE_INFO, bundle);
                    startActivity(intent);
                }
            }
        });
        return adapter;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }
}
