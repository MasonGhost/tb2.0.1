package com.zhiyicx.thinksnsplus.modules.my_music.single_music;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/08/24/17:04
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SingleMusicPresenter extends AppBasePresenter<SingleMusicContract.Repository,SingleMusicContract.View>
        implements SingleMusicContract.Presenter {

    @Inject
    public SingleMusicPresenter(SingleMusicContract.Repository repository, SingleMusicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<MusicDetaisBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicDetaisBean> data, boolean isLoadMore) {
        return false;
    }
}
