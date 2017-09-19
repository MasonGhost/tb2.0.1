package com.zhiyicx.zhibolibrary.ui.fragment;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.adapter.AdapterViewPager;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreParentView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.ArrayList;
import java.util.List;


public class ScrollClearnFragment extends ZBLBaseFragment {
    private static final String ARG_PARAM1 = "currentView";
    private static final String ARG_PARAM2 = "publishCoreView";
    ViewPager vpContainer;
    private AdapterViewPager mAdapter;
    private List<ZBLBaseFragment> mFragmentList;
    PublishCoreView publishCoreView;
    public ScrollClearnFragment() {
        // Required empty public constructor
    }


    public static ScrollClearnFragment newInstance(int param1) {
        ScrollClearnFragment fragment = new ScrollClearnFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    protected View initView() {
       View rootview= UiUtils.inflate(R.layout.zb_fragment_scroll_clearn);
        vpContainer= (ViewPager) rootview.findViewById(R.id.vp_container);

        return rootview;
    }

    @Override
    protected void initData() {
        initViewPager();

    }
    //初始化viewpager
    private void initViewPager() {

        //设置缓存的个数
        vpContainer.setOffscreenPageLimit(2);
        mAdapter = new AdapterViewPager(getFragmentManager());
        mFragmentList = getFragments();
        mAdapter.bindData(mFragmentList);//将List设置给adapter
        vpContainer.setAdapter(mAdapter);
        vpContainer.setCurrentItem(1);
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ((PublishCoreFragment)mFragmentList.get(1)).hidekeyboard();
            }
        });

    }
    /**
     * 返回的分页
     *
     * @return
     */
    public List<ZBLBaseFragment> getFragments() {
        List<ZBLBaseFragment> fragmentList = new ArrayList<ZBLBaseFragment>();
        fragmentList.add(new EmptyFragment());
        fragmentList.add((((PublishCoreParentView)getActivity()).getPublishCoreview()));
        return fragmentList;
    }
}
