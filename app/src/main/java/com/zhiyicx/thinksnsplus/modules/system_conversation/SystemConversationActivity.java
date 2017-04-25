package com.zhiyicx.thinksnsplus.modules.system_conversation;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.chat.ChatPresenter;
import com.zhiyicx.thinksnsplus.modules.chat.ChatPresenterModule;

/**
 * @Describe 即时聊天 - 机器人 (意见反馈 + 系统公告)
 * @Author Jungle68
 * @Date 2017/4/26
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
