package com.zhiyicx.thinksnsplus.modules.my_music.single_music;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;

/**
 * @Author Jliuer
 * @Date 2017/08/24/15:06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MySingleMusicListFragment extends TSListFragment {

    public static MySingleMusicListFragment getInstance() {
        MySingleMusicListFragment mySingleMusicListFragment=new MySingleMusicListFragment();
        return mySingleMusicListFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
