package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

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
        MessageItemBean messageItemBean =  getIntent().getExtras().getParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN);
        if (messageItemBean ==null)
            throw new IllegalArgumentException("messageItemBean not be null ");
        return ChatFragment.newInstance(messageItemBean);
    }

}
