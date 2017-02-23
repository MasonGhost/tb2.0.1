package com.zhiyicx.thinksnsplus.modules.chat;

import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */
public class ChatActivity extends TSActivity<ChatPresenter, ChatFragment> {


    @Override
    protected void componentInject() {
        DaggerChatComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatPresenterModule(new ChatPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected ChatFragment getFragment() {
        String userId = getIntent().getExtras().getString(ChatFragment.BUNDLE_USERID);
        if(TextUtils.isEmpty(userId))
            throw new IllegalArgumentException("userId not be null");
        return ChatFragment.newInstance("9527");
    }

}
