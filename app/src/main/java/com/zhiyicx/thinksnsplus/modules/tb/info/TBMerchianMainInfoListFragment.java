package com.zhiyicx.thinksnsplus.modules.tb.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_USERID;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/22
 * @contact master.jungle68@gmail.com
 */
public class TBMerchianMainInfoListFragment extends InfoListFragment {

    public static InfoListFragment newInstance(long userId) {
        TBMerchianMainInfoListFragment fragment = new TBMerchianMainInfoListFragment();
        Bundle args = new Bundle();
        args.putLong(BUNDLE_USERID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        StatusBarUtils.transparencyBar(getActivity());
    }

    @Override
    protected void initAdvert() {

    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new TBMainInfoListItem(false) {
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
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }
}
