package com.zhiyicx.thinksnsplus.modules.rank.main.container;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexFragment extends TSViewPagerFragment{

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected List<String> initTitles() {
        List<String> list = new ArrayList<>();
        list.add(getString(R.string.rank_user));
        list.add(getString(R.string.rank_qa));
        list.add(getString(R.string.rank_dynamic));
        list.add(getString(R.string.rank_info));
        return list;
    }

    @Override
    protected List<Fragment> initFragments() {
        return null;
    }

    @Override
    protected void initData() {

    }
}
