package com.zhiyicx.thinksnsplus.modules.my_music.music_album;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MyMusicAblumListFragment extends TSListFragment {

    public static MyMusicAblumListFragment getInstance() {
        MyMusicAblumListFragment myMusicAblumListFragment = new MyMusicAblumListFragment();
        return myMusicAblumListFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
