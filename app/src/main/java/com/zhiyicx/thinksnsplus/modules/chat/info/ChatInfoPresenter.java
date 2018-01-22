package com.zhiyicx.thinksnsplus.modules.chat.info;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoPresenter extends AppBasePresenter<ChatInfoContract.Repository, ChatInfoContract.View>
        implements ChatInfoContract.Presenter{

    @Inject
    public ChatInfoPresenter(ChatInfoContract.Repository repository, ChatInfoContract.View rootView) {
        super(repository, rootView);
    }
}
