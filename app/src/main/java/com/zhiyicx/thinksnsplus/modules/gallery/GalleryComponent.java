package com.zhiyicx.thinksnsplus.modules.gallery;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/06/29/10:10
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = GalleryPresenterModule.class)
public interface GalleryComponent extends InjectComponent<GalleryPictureFragment>{
}
