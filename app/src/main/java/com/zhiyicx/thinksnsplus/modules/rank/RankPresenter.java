package com.zhiyicx.thinksnsplus.modules.rank;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.DigBean;

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
    public List<DigBean> requestCacheData(Long maxId, boolean isLoadMore, long userId, int pageType) {
        return null;
    }

    @Override
    public void followUser(int index, DigBean followFansBean) {

    }

    @Override
    public void cancleFollowUser(int index, DigBean followFansBean) {

    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DigBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DigBean> data) {
        return false;
    }
}

