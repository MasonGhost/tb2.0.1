package com.zhiyicx.thinksnsplus.modules.music_fm.paided_music.single_music;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = SingleMusicPresenterModule.class)
public interface SingleMusicLIstComponent extends InjectComponent<MySingleMusicListFragment>{
}
