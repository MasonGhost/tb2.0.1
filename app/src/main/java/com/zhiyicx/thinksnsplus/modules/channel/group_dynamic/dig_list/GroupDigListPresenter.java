package com.zhiyicx.thinksnsplus.modules.channel.group_dynamic.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicLikeListBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class GroupDigListPresenter extends AppBasePresenter<GroupDigListContract.Repository, GroupDigListContract.View>
        implements GroupDigListContract.Presenter{

    @Inject
    public GroupDigListPresenter(GroupDigListContract.Repository repository, GroupDigListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<FollowFansBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleFollowUser(int position, FollowFansBean followFansBean) {

    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore, long feed_id) {

    }

    @Override
    public List<FollowFansBean> requestCacheData(Long maxId, boolean isLoadMore, GroupDynamicLikeListBean dynamicBean) {
        return null;
    }
}
