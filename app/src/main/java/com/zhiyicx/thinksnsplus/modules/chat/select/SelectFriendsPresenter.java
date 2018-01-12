package com.zhiyicx.thinksnsplus.modules.chat.select;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsPresenter extends AppBasePresenter<SelectFriendsContract.Repository, SelectFriendsContract.View>
        implements SelectFriendsContract.Presenter{

    @Inject
    public SelectFriendsPresenter(SelectFriendsContract.Repository repository, SelectFriendsContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
