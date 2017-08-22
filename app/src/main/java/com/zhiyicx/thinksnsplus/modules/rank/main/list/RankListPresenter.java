package com.zhiyicx.thinksnsplus.modules.rank.main.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class RankListPresenter extends AppBasePresenter<RankListContract.Repository, RankListContract.View>
        implements RankListContract.Presenter{

    @Inject
    public RankListPresenter(RankListContract.Repository repository, RankListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<RankIndexBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<RankIndexBean> data, boolean isLoadMore) {
        return false;
    }
}
