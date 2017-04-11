package com.zhiyicx.thinksnsplus.modules.rank;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/10
 * @Contact master.jungle68@gmail.com
 */

public class RankPresenter extends BasePresenter<RankContract.Repository, RankContract.View> implements RankContract.Presenter {


    @Inject
    public RankPresenter(RankContract.Repository repository, RankContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void requestNetData(Long maxId, boolean isLoadMore, long userId, int pageType) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, long userId, int pageType) {
        return null;
    }

    @Override
    public void followUser(int index, FollowFansBean followFansBean) {

    }

    @Override
    public void cancleFollowUser(int index, FollowFansBean followFansBean) {

    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data) {
        return false;
    }
}

