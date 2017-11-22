package com.zhiyicx.zhibolibrary.ui.fragment;


import com.zhiyicx.zhibolibrary.R;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ZBLNewLiveFragment extends ZBLLiveItemFragment {

    @Override
    public String getOrder() {
        return TYPE_AIRTIME;
    }

    @Override
    public boolean isNeedShowUserInfo() {
        return isNeedShowUserInfo;
    }

    @Override
    public int getLayout() {
        return R.layout.zb_fragment_live_item;
    }

}
