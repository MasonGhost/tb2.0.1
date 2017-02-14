package com.zhiyicx.thinksnsplus.modules.follow_fans;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListPresenter extends BasePresenter implements FollowFansListContract.Presenter {
    @Override
    public void requestNetData(int maxId, boolean isLoadMore) {

    }

    @Override
    public List<FollowFansItemBean> requestCacheData(int maxId, boolean isLoadMore) {
        return null;
    }
}
