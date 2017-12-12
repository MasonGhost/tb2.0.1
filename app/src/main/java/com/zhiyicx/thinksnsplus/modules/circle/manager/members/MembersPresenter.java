package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
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
public class MembersPresenter extends AppBasePresenter<MembersContract.Repository, MembersContract.View>
        implements MembersContract.Presenter {

    public static final String TYPE_ALL = "all";

    @Inject
    public MembersPresenter(MembersContract.Repository repository, MembersContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mRepository.getCircleMemberList(mRootView.getCIrcleId(), maxId.intValue(), TSListFragment.DEFAULT_ONE_PAGE_SIZE, TYPE_ALL)
                .subscribe(new BaseSubscribeForV2<List<CircleMembers>>() {
                    @Override
                    protected void onSuccess(List<CircleMembers> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleMembers> data, boolean isLoadMore) {
        return false;
    }
}
