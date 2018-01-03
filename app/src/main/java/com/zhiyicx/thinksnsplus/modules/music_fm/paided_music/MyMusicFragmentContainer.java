package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.music_album.MyMusicAblumListFragment;
import com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.single_music.MySingleMusicListFragment;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyMusicFragmentContainer extends TSViewPagerFragment {

    public static MyMusicFragmentContainer getInstance() {
        MyMusicFragmentContainer myMusicFragmentContainer = new MyMusicFragmentContainer();
        return myMusicFragmentContainer;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.single_music), getString(R.string.music_album));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            Fragment singleMusic = MySingleMusicListFragment.getInstance();
            Fragment musicAlbum = MyMusicAblumListFragment.getInstance();
            mFragmentList = Arrays.asList(singleMusic, musicAlbum);
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {

    }


}
