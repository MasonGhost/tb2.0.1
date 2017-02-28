package com.zhiyicx.thinksnsplus.modules.music_fm.music_album;


import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
    protected void initData() {
        super.initData();
        mRvList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvList.setPadding(20, 20, 20, 0);
        mRvList.addItemDecoration(new GridDecoration(20, 20));
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(getActivity(), MusicDetailActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
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
    public void setMediaBrowserCompat(MediaBrowserCompat mediaBrowserCompat) {

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

    @Override
    protected int setListBackColor() {
        return R.color.white;
    }

    @Override
    public void onRefresh() {
        mRefreshlayout.setRefreshing(false);
    }
}
