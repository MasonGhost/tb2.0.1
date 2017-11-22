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
public class ZBLReplayFragment extends ZBLLiveFragment {


    @Override
    protected View initView() {
        View view = UiUtils.inflate(R.layout.zb_fragment_replay);
        mViewPager = UiUtils.findViewByName(view, "vp_replay");
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        mSubscribeTV.setText("我的");
        mHotTV.setText("关注");
        mNewTV.setText("全部");//隐藏第三个Tab,只有两个tab
    }

    @Override
    public void setData() {
        mFragmentList.get(0).setData();
    }

    @Override
    public void setData(Object data) {
        if (data instanceof Boolean) {
            if (isShow) {//如果筛选页面打开则关闭
                hideFilterNotAni();
            }
        }
    }

    @Override
    protected List<ZBLBaseFragment> getFragements() {
        List<ZBLBaseFragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ZBLReplayOwnFragment());
        fragmentList.add(new ZBLReplayFollowFragment());
        fragmentList.add(new ZBLReplayAllFragment());
        return fragmentList;
    }

    @Nullable
    @Subscriber(tag = "set_filter_satus_replay", mode = ThreadMode.MAIN)
    @Override
    public void setFilterSatus(boolean isFilter) {
        mFilterTV.setImageResource(isFilter ? R.mipmap.ic_top_screening_select
                : R.mipmap.ic_top_screening_white);
    }
}
