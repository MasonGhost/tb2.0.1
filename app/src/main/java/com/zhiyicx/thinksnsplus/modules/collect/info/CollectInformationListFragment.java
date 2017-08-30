package com.zhiyicx.thinksnsplus.modules.collect.info;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;

import java.util.Collections;
import java.util.Comparator;

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
        // 存在这样的动态
        if (isCollect) {// 取消收藏
            mListDatas.remove(info);
        } else {
            mListDatas.add(info);
            // 按资讯id大小进行逆序排列，防止上啦加载重复
            Collections.sort(mListDatas, new Comparator<BaseListBean>() {
                @Override
                public int compare(BaseListBean o1, BaseListBean o2) {
                    InfoListDataBean infoListDataBean1 = (InfoListDataBean) o1;
                    InfoListDataBean infoListDataBean2 = (InfoListDataBean) o2;
                    return infoListDataBean2.getId() > infoListDataBean1.getId() ? 1 : -1;
                }
            });
        }
        refreshData();
    }
}
