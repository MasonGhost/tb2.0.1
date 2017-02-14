package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class FollowFansListPresenter extends BasePresenter<FollowFansListContract.Repository, FollowFansListContract.View> implements FollowFansListContract.Presenter {
    @Inject
    public FollowFansListPresenter(FollowFansListContract.Repository repository, FollowFansListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansItemBean> requestCacheData(int maxId, boolean isLoadMore) {
        return null;
    }
}
