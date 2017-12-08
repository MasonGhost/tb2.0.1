package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MembersPresenter extends AppBasePresenter<MembersContract.Repository,MembersContract.View>
        implements MembersContract.Presenter {

    @Inject
    public MembersPresenter(MembersContract.Repository repository, MembersContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleMembers> data, boolean isLoadMore) {
        return false;
    }
}
