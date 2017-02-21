package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.chat.ChatContract;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicPresenter extends BasePresenter<DynamicContract.Repository, DynamicContract.View> implements DynamicContract.Presenter {
    @Inject
    ChatContract.Repository mChatRepository;
    @Inject
    AuthRepository mAuthRepository;
    private MessageItemBean mItemBeanComment;
    private MessageItemBean mItemBeanLike;

    @Inject
    public DynamicPresenter(DynamicContract.Repository repository, DynamicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicBean> requestCacheData(int minTime, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return false;
    }

}