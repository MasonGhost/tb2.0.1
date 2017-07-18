package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe 系统消息
 * @Author Jungle68
 * @Date 2017/5/25
 * @Contact master.jungle68@gmail.com
 */
public class SystemConversationActivity extends TSActivity<SystemConversationPresenter, SystemConversationFragment> {


    @Override
    protected void componentInject() {
        DaggerSystemConversationComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .systemConversationPresenterModule(new SystemConversationPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected SystemConversationFragment getFragment() {
        return SystemConversationFragment.newInstance();
    }
}
