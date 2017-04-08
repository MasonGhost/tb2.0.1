package com.zhiyicx.thinksnsplus.modules.channel;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/8
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class ChannelListPresenter extends BasePresenter<ChannelListContract.Repository, ChannelListContract.View>
        implements ChannelListContract.Presenter {
    @Inject
    public ChannelListPresenter(ChannelListContract.Repository repository, ChannelListContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<ChannelSubscripBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ChannelSubscripBean> data) {
        return false;
    }
}
