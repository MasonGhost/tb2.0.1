package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.os.Handler;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    public void requestNetData(Long maxId, boolean isLoadMore) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mRootView.hideLoading();
            }
        }, 2000);
    }

    @Override
    public List<MessageItemBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBean> data, boolean isLoadMore) {
        return false;
    }
}
