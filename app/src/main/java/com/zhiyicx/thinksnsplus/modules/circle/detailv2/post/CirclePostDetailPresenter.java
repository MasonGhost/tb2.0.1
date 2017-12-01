package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/12/01/16:36
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostDetailPresenter extends AppBasePresenter<CirclePostDetailContract.Repository,CirclePostDetailContract.View>
        implements CirclePostDetailContract.Presenter {

    @Inject
    public CirclePostDetailPresenter(CirclePostDetailContract.Repository repository, CirclePostDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CirclePostCommentBean> data, boolean isLoadMore) {
        return false;
    }
}
