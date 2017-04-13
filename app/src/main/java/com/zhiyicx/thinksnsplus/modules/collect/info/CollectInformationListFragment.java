package com.zhiyicx.thinksnsplus.modules.collect.info;

import android.os.Bundle;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;

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

}
