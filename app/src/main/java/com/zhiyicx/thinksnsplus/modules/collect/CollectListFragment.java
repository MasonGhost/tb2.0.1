package com.zhiyicx.thinksnsplus.modules.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.collect.album.CollectAlbumListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.dynamic.CollectDynamicListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.group_posts.CollectGroupDynamicListFragment;
import com.zhiyicx.thinksnsplus.modules.collect.info.CollectInformationListFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;

import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.RECOMMEND_INFO;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectListFragment extends TSViewPagerFragment<CollectListPresenter> {
    @Override
    protected List<String> initTitles() {

        return Arrays.asList(
                getString(R.string.collect_dynamic)
                , getString(R.string.collect_info)
                , getString(R.string.collect_album)
                , getString(R.string.group_collect_dynamic)
        );
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.collect);
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment dynamicListFragment = CollectDynamicListFragment.newInstance();
            Fragment infoListFragment = CollectInformationListFragment.newInstance();
            Fragment albumListFragment = CollectAlbumListFragment.newInstance();
            Fragment groupDynamicListFragment = CollectGroupDynamicListFragment.newInstance(new Bundle());
            mFragmentList = Arrays.asList(
                    dynamicListFragment
                    , infoListFragment
                    , albumListFragment
                    , groupDynamicListFragment
            );
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    public static CollectListFragment newInstance(Bundle bundle) {
        CollectListFragment collectListFragment = new CollectListFragment();
        collectListFragment.setArguments(bundle);
        return collectListFragment;
    }
}
