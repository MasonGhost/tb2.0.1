package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/10/17/16:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class)
public interface PictureTollComponent extends InjectComponent<PictureTollFragment> {
}
