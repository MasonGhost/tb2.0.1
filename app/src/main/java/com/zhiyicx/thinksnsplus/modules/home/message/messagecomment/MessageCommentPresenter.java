package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessageCommentPresenter extends BasePresenter<MessageCommentContract.Repository, MessageCommentContract.View> implements MessageCommentContract.Presenter {

    @Inject
    public MessageCommentPresenter(MessageCommentContract.Repository repository, MessageCommentContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestData(int maxId, boolean isLoadMore) {

    }
}
