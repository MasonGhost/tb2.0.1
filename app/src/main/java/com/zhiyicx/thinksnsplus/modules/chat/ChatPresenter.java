package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends BasePresenter<ChatContract.Repository, ChatContract.View> implements ChatContract.Presenter {

    @Inject
    public ChatPresenter(ChatContract.Repository repository, ChatContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }
}
