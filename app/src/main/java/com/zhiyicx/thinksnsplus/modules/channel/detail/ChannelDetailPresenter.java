package com.zhiyicx.thinksnsplus.modules.channel.detail;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class ChannelDetailPresenter extends BasePresenter<ChannelDetailContract.Repository, ChannelDetailContract.View>
        implements ChannelDetailContract.Presenter {
    @Inject
    public ChannelDetailPresenter(ChannelDetailContract.Repository repository, ChannelDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return false;
    }
}
