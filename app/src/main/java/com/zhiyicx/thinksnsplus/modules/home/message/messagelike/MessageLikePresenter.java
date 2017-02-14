package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class MessageLikePresenter extends BasePresenter<MessageLikeContract.Repository, MessageLikeContract.View> implements MessageLikeContract.Presenter {

    @Inject
    public MessageLikePresenter(MessageLikeContract.Repository repository, MessageLikeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List<MessageItemBean> requestCacheData(int maxId, boolean isLoadMore) {
        return null;
    }
}
