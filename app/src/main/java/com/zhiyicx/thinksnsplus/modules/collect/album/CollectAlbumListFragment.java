package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectAlbumListFragment extends MusicListFragment {
    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }
}
