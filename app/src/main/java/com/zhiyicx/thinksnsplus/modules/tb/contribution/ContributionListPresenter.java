package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/03/01/14:53
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ContributionListPresenter extends AppBasePresenter<ContributionListContract.View> implements ContributionListContract.Presenter {

    @Inject
    public ContributionListPresenter(ContributionListContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ContributionData> data, boolean isLoadMore) {
        return false;
    }
}
