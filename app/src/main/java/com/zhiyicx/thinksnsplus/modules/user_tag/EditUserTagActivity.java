package com.zhiyicx.thinksnsplus.modules.user_tag;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.settings.DaggerSettingsComponent;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsFragment;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsPresenter;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsPresenterModule;

/**
 * @Describe 用户标签
 * @Author Jungle68
 * @Date 2017/7/29
 * @Contact master.jungle68@gmail.com
 */

public class EditUserTagActivity extends TSActivity<EditUserTagPresenter, EditUserTagFragment> {

    @Override
    protected void componentInject() {
        DaggerEditUserTagComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .editUserTagPresenterModule(new EditUserTagPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected EditUserTagFragment getFragment() {
        return EditUserTagFragment.newInstance();
    }



}
