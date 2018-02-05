package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import rx.Observable;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicListFragment extends TSListFragment<MusicContract.Presenter, MusicAlbumListBean>
        implements MusicContract.View {

    public static final String BUNDLE_MUSIC_ABLUM = "music_ablum";

    /**
     * 数量改变 event_bus 来的
     */
    private MusicAlbumListBean mMusicAlbumListBean;

    private PayPopWindow mPayMusicPopWindow;

    @Override
    public void onResume() {
        super.onResume();
        for (MusicAlbumListBean musicListBean : mListDatas) {
            WindowUtils.AblumHeadInfo ablumHeadInfo = WindowUtils.getAblumHeadInfo();
            if (ablumHeadInfo != null && ablumHeadInfo.getAblumId() == musicListBean.getId()) {
                musicListBean.setComment_count(ablumHeadInfo.getCommentCount());
                musicListBean.setTaste_count(ablumHeadInfo.getListenCount());
                musicListBean.setShare_count(ablumHeadInfo.getShareCount());
                musicListBean.setCollect_count(ablumHeadInfo.getLikeCount());
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mRvList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRvList.setPadding(20, 20, 0, 0);
        mRvList.addItemDecoration(new TGridDecoration(20, 20, true));
        mRvList.setBackgroundColor(0xffffffff);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        if (mListDatas.isEmpty()) {
            mRvList.setBackground(null);
        }
    }

    @Override
    protected boolean showNoMoreData() {
        return mListDatas.size() >= DEFAULT_ONE_PAGE_SHOW_MAX_SIZE;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.music_fm);
    }

    @Override
    protected CommonAdapter<MusicAlbumListBean> getAdapter() {
        final int width = (DeviceUtils.getScreenWidth(getActivity()) - 60) / 2;
        CommonAdapter<MusicAlbumListBean> comAdapter = new CommonAdapter<MusicAlbumListBean>
                (getActivity(), R.layout.item_music_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MusicAlbumListBean musicListBean, int
                    position) {
                ImageView imag = holder.getView(R.id.music_list_image);
                holder.setVisible(R.id.music_list_toll_flag, musicListBean.getPaid_node() == null
                       /* || !(musicListBean.getPaid_node() != null  && !musicListBean.getPaid_node().isPaid())*/ ? View.GONE : View.VISIBLE);

                Glide.with(getContext())
                        .load(ImageUtils.imagePathConvertV2(musicListBean.getStorage().getId(), width, width,
                                ImageZipConfig.IMAGE_70_ZIP))
                        .placeholder(R.drawable.shape_default_image)
                        .override(width, width)
                        .error(R.drawable.shape_default_image)
                        .into(imag);

                holder.setText(R.id.music_list_taste_count, "" + musicListBean.getTaste_count());
                holder.setText(R.id.music_list_title, musicListBean.getTitle());

            }
        };

        comAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                MusicAlbumListBean albumListBean = mListDatas.get(position);
                if (albumListBean.getPaid_node() != null && !albumListBean.getPaid_node().isPaid()) {
                    initMusicCenterPopWindow(position, albumListBean.getPaid_node().getAmount(),
                            albumListBean.getPaid_node().getNode(), R.string.buy_pay_music_ablum_desc);
                    return;
                }

                Intent intent = new Intent(getActivity(), MusicDetailActivity.class);
                Bundle bundle = new Bundle();

                bundle.putParcelable(BUNDLE_MUSIC_ABLUM, albumListBean);
                intent.putExtra(BUNDLE_MUSIC_ABLUM, bundle);
                WindowUtils.AblumHeadInfo ablumHeadInfo = new WindowUtils.AblumHeadInfo();
                ablumHeadInfo.setCommentCount(albumListBean.getComment_count());
                ablumHeadInfo.setShareCount(albumListBean.getShare_count());
                ablumHeadInfo.setListenCount(albumListBean.getTaste_count());
                ablumHeadInfo.setAblumId(albumListBean.getId());
                ablumHeadInfo.setLikeCount(albumListBean.getCollect_count());
                WindowUtils.setAblumHeadInfo(ablumHeadInfo);
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
        Observable.from(mListDatas).filter(albumListBean -> mMusicAlbumListBean.getId() == albumListBean.getId()).subscribe(albumListBean_same -> {
            albumListBean_same.setCollect_count(mMusicAlbumListBean.getCollect_count());
            albumListBean_same.setShare_count(mMusicAlbumListBean.getShare_count());
            albumListBean_same.setHas_collect(mMusicAlbumListBean.getHas_collect());
            albumListBean_same.setComment_count(mMusicAlbumListBean.getComment_count());
            albumListBean_same.setTaste_count(mMusicAlbumListBean.getTaste_count());
            mPresenter.updateOneMusic(albumListBean_same);
            mHeaderAndFooterWrapper.notifyDataSetChanged();
        });
    }


    private void initMusicCenterPopWindow(final int position, int amout,
                                          final int note, int strRes) {
        mPayMusicPopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes),amout,mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(getString(R.string.buy_pay_integration,amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPresenter.payNote(position, note);
                    mPayMusicPopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayMusicPopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayMusicPopWindow.show();

    }

    @Override
    public boolean isCollection() {
        return false;
    }
}
