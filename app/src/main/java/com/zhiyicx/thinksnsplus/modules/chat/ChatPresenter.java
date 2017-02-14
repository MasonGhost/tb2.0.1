package com.zhiyicx.thinksnsplus.modules.chat;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.imsdk.entity.Message;

import java.util.List;

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

    @Override
    public void getUserInfo(long user_id) {

    }

    @Override
    public List<Message> getHistoryMessages(int cid, long mid) {
        return mRepository.;
    }
}
