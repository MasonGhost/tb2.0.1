package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.imsdk.entity.Conversation;
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
        long userId = getIntent().getExtras().getLong(ChatFragment.BUNDLE_USERID);
        Conversation conversation = (Conversation) getIntent().getExtras().getSerializable(ChatFragment.BUNDLE_CONVERSATION);
        if (userId == 0)
            throw new IllegalArgumentException("userId is must");
        if (conversation ==null)
            throw new IllegalArgumentException("conversation not be null ");
        return ChatFragment.newInstance(4L,conversation);
    }

}
