<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

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
public class MessageLikePresenter extends BasePresenter<MessageLikeContract.Repository, MessageLikeContract.View> implements MessageLikeContract.Presenter {

    @Inject
    public MessageLikePresenter(MessageLikeContract.Repository repository, MessageLikeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestData(int maxId, boolean isLoadMore) {

    }
}
=======
package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

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
public class MessageLikePresenter extends BasePresenter<MessageLikeContract.Repository, MessageLikeContract.View> implements MessageLikeContract.Presenter {

    @Inject
    public MessageLikePresenter(MessageLikeContract.Repository repository, MessageLikeContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestData(int maxId, boolean isLoadMore) {

    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
