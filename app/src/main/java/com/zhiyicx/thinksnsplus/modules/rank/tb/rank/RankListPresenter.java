package com.zhiyicx.thinksnsplus.modules.rank.tb.rank;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:44
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListPresenter extends AppBasePresenter<RankListContract.View> implements RankListContract.Presenter {

    @Inject
    public RankListPresenter(RankListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RankData> data, boolean isLoadMore) {
        return false;
    }
}
