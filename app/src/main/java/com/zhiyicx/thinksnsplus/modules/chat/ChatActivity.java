package com.zhiyicx.thinksnsplus.modules.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 聊天详情页
 * @date 2018/1/2
 * @contact email:648129313@qq.com
 */
public class ChatActivity extends TSActivity<ChatPresenter, ChatFragment> {

    @Override
    protected ChatFragment getFragment() {
        return ChatFragment.instance(getIntent().getExtras());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mContanierFragment.onNewIntent(intent.getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerChatComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .chatPresenterModule(new ChatPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startChatActivity(Context context,String to,int chatType){
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EaseConstant.EXTRA_USER_ID, to);
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
