package com.zhiyicx.thinksnsplus.modules.circle.main;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainPresenter extends AppBasePresenter<CircleMainContract.Repository, CircleMainContract.View> implements CircleMainContract.Presenter {

    @Inject
    public CircleMainPresenter(CircleMainContract.Repository repository, CircleMainContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupInfoBean> data, boolean isLoadMore) {
        return false;
    }
}
