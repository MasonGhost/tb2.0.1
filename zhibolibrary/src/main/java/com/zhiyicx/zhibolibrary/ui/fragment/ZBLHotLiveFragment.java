package com.zhiyicx.zhibolibrary.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.zhibolibrary.R;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ZBLHotLiveFragment extends ZBLLiveItemFragment {

    @Override
    public String getOrder() {
        return TYPE_ONLINE;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setData();//防止RefreshLayout为空导致的白屏
    }

    @Override
    public int getLayout() {
        return R.layout.zb_fragment_live_item;
    }

}
