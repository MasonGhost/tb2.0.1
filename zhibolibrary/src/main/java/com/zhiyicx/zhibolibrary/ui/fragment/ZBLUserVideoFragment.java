package com.zhiyicx.zhibolibrary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhiyicx.zhibolibrary.R;


/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ZBLUserVideoFragment extends ZBLLiveItemFragment {
    public static final String BUNDLE_ISFROMUSERHOME = "isfrom_userhome";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setData();//刷新列表
    }

    @Override
    public String getOrder() {
        return TYPE_VIDEO;
    }

    @Override
    public int getLayout() {

//        if (getArguments().getBoolean(BUNDLE_ISFROMUSERHOME))
            return R.layout.zb_fragment_video_item;
//        else
//            return R.layout.zb_fragment_live_item;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof Boolean) {//判断是否开启刷新,又applayout控制
            mRefreshLayout.setPullDownRefreshEnable((Boolean) data);//让下拉刷新不可用
            return;
        }
        setUsid((String) data);//设置uid;
    }
}
