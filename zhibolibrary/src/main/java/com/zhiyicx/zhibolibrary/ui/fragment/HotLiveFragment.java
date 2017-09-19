package com.zhiyicx.zhibolibrary.ui.fragment;


import com.zhiyicx.zhibolibrary.R;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class HotLiveFragment extends LiveItemFragment {

    @Override
    public String getOrder() {
        return TYPE_ONLINE;
    }
    @Override
    public int getLayout() {
        return R.layout.zb_fragment_live_item;
    }

}
