package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoActivity extends TSActivity<ChatInfoPresenter, ChatInfoFragment>{

    @Override
    protected ChatInfoFragment getFragment() {
        return new ChatInfoFragment().instance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerChatInfoComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatInfoPresenterModule(new ChatInfoPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
