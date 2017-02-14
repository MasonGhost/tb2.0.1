package com.zhiyicx.thinksnsplus.modules.music_fm;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */

public class MusicListFragment extends TSListFragment<MusicContract.Presenter, MusicListBean>
        implements
        MusicContract.View {
    private ImageLoader mImageLoader;
    private List<MusicListBean> mMusicListBeen = new ArrayList<>();

    @Override
    protected CommonAdapter<MusicListBean> getAdapter() {
        return new CommonAdapter<MusicListBean>(getActivity(), R.layout.item_music_list,
                mMusicListBeen) {
            @Override
            protected void convert(ViewHolder holder, MusicListBean musicListBean, int position) {

            }
        };
    }

    @Override
    protected boolean insertOrUpdateData(@NotNull List<MusicListBean> data) {
        return false;
    }


    @Override
    protected void initData() {
        super.initData();
        mRvList.setPadding(20, 20, 20, 0);
        mRvList.addItemDecoration(new GridDecoration(20, 20));
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    public void setPresenter(MusicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.music_fm);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }
}
