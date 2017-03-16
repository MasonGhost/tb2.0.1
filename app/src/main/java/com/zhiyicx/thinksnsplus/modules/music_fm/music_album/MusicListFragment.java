package com.zhiyicx.thinksnsplus.modules.music_fm.music_album;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
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
public class MusicListFragment extends TSListFragment<MusicContract.Presenter, MusicAlbumListBean>
        implements MusicContract.View {

    private ImageLoader mImageLoader;
    public static final String BUNDLE_MUSIC_ABLUM="music_ablum";

    private List<MusicAlbumListBean> mMusicListBeen = new ArrayList<>();

    @Override
    protected void initData() {
        super.initData();
        mRvList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvList.setPadding(20, 20, 0, 0);
        mRvList.addItemDecoration(new TGridDecoration(20, 20, true));
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent=new Intent(getActivity(), MusicDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable(BUNDLE_MUSIC_ABLUM,mMusicListBeen.get(position));
                intent.putExtra(BUNDLE_MUSIC_ABLUM,bundle);
                startActivity(intent);
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
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
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

    @Override
    protected int setListBackColor() {
        return R.color.white;
    }

    @Override
    protected CommonAdapter<MusicAlbumListBean> getAdapter() {
        return new CommonAdapter<MusicAlbumListBean>(getActivity(), R.layout.item_music_list,
                mMusicListBeen) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumListBean musicListBean, int
                    position) {
                ImageView imag = holder.getView(R.id.music_list_image);
                String url = String.format(ApiConfig.IMAGE_PATH,
                        musicListBean.getStorage().getId(), 50);
                mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                        .imagerView(imag)
                        .url(url)
                        .build());
                holder.setText(R.id.music_list_taste_count,""+musicListBean.getTaste_count());
                holder.setText(R.id.music_list_title,musicListBean.getTitle());
            }
        };
    }

    @Override
    protected Long getMaxId(@NotNull List<MusicAlbumListBean> data) {
        return (long)data.get(data.size() - 1).getId();
    }
}
