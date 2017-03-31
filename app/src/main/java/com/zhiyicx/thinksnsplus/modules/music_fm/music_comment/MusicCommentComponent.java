package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = MusicCommentPresenterModule.class)
public interface MusicCommentComponent extends InjectComponent<MusicCommentActivity>{
}
