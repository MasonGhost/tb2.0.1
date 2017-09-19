package com.zhiyicx.zhibolibrary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.zhibolibrary.R;


/**
 * Created by zhiyicx on 2016/3/22.
 */
public class SubscribeLiveFragment extends LiveItemFragment  {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setData();//第一页初始化
    }


    @Override
    public String getOrder() {
        return TYPE_FOLLOW;
    }
    @Override
    public int getLayout() {
        return R.layout.zb_fragment_live_item;
    }

}
