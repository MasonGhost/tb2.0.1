package com.zhiyicx.zhibolibrary.ui.fragment;

import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyicx on 2016/3/22.
 */
public class ZBLLiveListFragment extends ZBLLiveFragment {

    @Override
    protected View initView() {
        View view = UiUtils.inflate(R.layout.zb_fragment_live);
        mViewPager = UiUtils.findViewByName(view, "vp_live");
        return view;
    }

    @Override
    protected List<ZBLBaseFragment> getFragements() {
        List<ZBLBaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ZBLSubscribeLiveFragment());
        fragmentList.add(new ZBLHotLiveFragment());
        fragmentList.add(new ZBLNewLiveFragment());
        return fragmentList;
    }

    @Override
    public void setData(Object data) {
            if (data instanceof Boolean){
                if (isShow){//如果筛选页面打开则关闭
                    hideFilterNotAni();
                }
        }
    }

    @Nullable
    @Subscriber(tag = "set_filter_satus_live", mode = ThreadMode.MAIN)
    @Override
    public void setFilterSatus(boolean isFilter) {
        mFilterTV.setImageResource(isFilter ? R.mipmap.ic_top_screening_select
                : R.mipmap.ic_top_screening_white);
    }
}
