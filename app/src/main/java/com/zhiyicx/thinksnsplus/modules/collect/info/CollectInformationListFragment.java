package com.zhiyicx.thinksnsplus.modules.collect.info;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectInformationListFragment extends InfoListFragment {
    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected Long getMaxId(@NotNull List<BaseListBean> data) {
        return (long) mListDatas.size();
    }

    public static CollectInformationListFragment newInstance() {
        CollectInformationListFragment fragment = new CollectInformationListFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_INFO_TYPE, ApiConfig.INFO_TYPE_COLLECTIONS);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void handleCollectInfo(InfoListDataBean info) {
        boolean isCollect = info.getHas_collect();
        if (isCollect) {
            mListDatas.add(info);
        } else {
            mListDatas.remove(info);
        }
        refreshData();
    }
}
