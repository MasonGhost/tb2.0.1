package com.zhiyicx.thinksnsplus.modules.chat.v2;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 聊天详情页
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */

public class ChatActivityV2 extends TSActivity<ChatPresenterV2, ChatFragmentV2> {

    public final static String BUNDLE_CHAT_DATA = "bundle_chat_data";

    @Override
    protected ChatFragmentV2 getFragment() {
        return ChatFragmentV2.instance(getIntent().getBundleExtra(BUNDLE_CHAT_DATA));
    }

    @Override
    protected void componentInject() {
        DaggerChatComponentV2.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatPresenterModuleV2(new ChatPresenterModuleV2(mContanierFragment))
                .build().inject(this);
    }
}
