package com.zhiyicx.thinksnsplus.modules.collect.album;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicPresenterModule;

import dagger.Component;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = CollectAlbumPresenterModule.class)
public interface CollectAlbumListPresenterComponent extends InjectComponent<CollectAlbumListFragment> {
}
