package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListFragment extends TSListFragment<MusicContract.Presenter, MusicAlbumListBean>
        implements MusicContract.View {

    private ImageLoader mImageLoader;
    public static final String BUNDLE_MUSIC_ABLUM = "music_ablum";
    /**
     * 数量改变 event_bus 来的
     */
    private MusicAlbumListBean mMusicAlbumListBean;

    @Override
    protected void initData() {
        super.initData();
        mRvList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvList.setPadding(20, 20, 0, 0);
        mRvList.addItemDecoration(new TGridDecoration(20, 20, true));
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();

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

    @Override
    protected CommonAdapter<MusicAlbumListBean> getAdapter() {
        CommonAdapter<MusicAlbumListBean> comAdapter = new CommonAdapter<MusicAlbumListBean>
                (getActivity(), R.layout.item_music_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumListBean musicListBean, int
                    position) {
                ImageView imag = holder.getView(R.id.music_list_image);
                mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                        .placeholder(R.drawable.shape_default_image)
                        .errorPic(R.drawable.shape_default_image)
                        .imagerView(imag)
                        .url(ImageUtils.imagePathConvert(musicListBean.getStorage().getId() + "",
                                ImageZipConfig.IMAGE_70_ZIP))
                        .build());

                holder.setText(R.id.music_list_taste_count, "" + musicListBean.getCollect_count());
                holder.setText(R.id.music_list_title, musicListBean.getTitle());

            }
        };

        comAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_MUSIC_ABLUM, mListDatas.get(position));
                intent.putExtra(BUNDLE_MUSIC_ABLUM, bundle);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
        return comAdapter;
    }

    @Override
    protected Long getMaxId(@NotNull List<MusicAlbumListBean> data) {
        return (long) data.get(data.size() - 1).getId();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_ABLUM_COLLECT, mode = ThreadMode.MAIN)
    public void onCollectCountUpdate(MusicAlbumListBean e_albumListBean) {
        mMusicAlbumListBean = e_albumListBean;
        Observable.from(mListDatas).filter(new Func1<MusicAlbumListBean, Boolean>() {
            @Override
            public Boolean call(MusicAlbumListBean albumListBean) {
                return mMusicAlbumListBean.getId() == albumListBean.getId();
            }
        }).subscribe(new Action1<MusicAlbumListBean>() {
            @Override
            public void call(MusicAlbumListBean albumListBean_same) {
                albumListBean_same.setCollect_count(mMusicAlbumListBean.getCollect_count());
                albumListBean_same.setShare_count(mMusicAlbumListBean.getShare_count());
                albumListBean_same.setIs_collection(mMusicAlbumListBean.getIs_collection());
                albumListBean_same.setComment_count(mMusicAlbumListBean.getComment_count());
                albumListBean_same.setTaste_count(mMusicAlbumListBean.getTaste_count());
                mPresenter.updateOneMusic(albumListBean_same);
                mHeaderAndFooterWrapper.notifyDataSetChanged();
            }
        });
        LogUtils.d("EVENT_ABLUM_COLLECT");
    }
}
