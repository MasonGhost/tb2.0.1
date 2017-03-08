package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideStokeTransform;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.MediaIDHelper;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.thinksnsplus.widget.NestedScrollLineayLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
public class MusicDetailFragment extends TSFragment<MusicDetailContract.Presenter> implements
        MusicDetailContract.View {

    @BindView(R.id.fragment_music_detail_head_iamge)
    ImageView mFragmentMusicDetailHeadIamge;
    @BindView(R.id.fragment_music_detail_name)
    TextView mFragmentMusicDetailName;
    @BindView(R.id.fragment_music_detail_dec)
    TextView mFragmentMusicDetailDec;
    @BindView(R.id.fragment_music_detail_share)
    TextView mFragmentMusicDetailShare;
    @BindView(R.id.fragment_music_detail_comment)
    TextView mFragmentMusicDetailComment;
    @BindView(R.id.fragment_music_detail_favorite)
    TextView mFragmentMusicDetailFavorite;
    @BindView(R.id.fragment_music_detail_head_info)
    RelativeLayout mFragmentMusicDetailHeadInfo;
    @BindView(R.id.rv_music_detail_list)
    RecyclerView mRvMusicDetailList;
    @BindView(R.id.fragment_music_detail_back)
    TextView mFragmentMusicDetailBack;
    @BindView(R.id.fragment_music_detail_scrollview)
    NestedScrollLineayLayout mFragmentMusicDetailScrollview;

    @BindView(R.id.fragment_music_detail_title)
    RelativeLayout mFragmentMusicDetailTitle;
    @BindView(R.id.fragment_music_detail_empty)
    View mFragmentMusicDetailEmpty;
    @BindView(R.id.fragment_music_detail_playvolume)
    TextView mFragmentMusicDetailPlayvolume;
    @BindView(R.id.fragment_music_detail_center_title)
    TextView mFragmentMusicDetailCenterTitle;
    @BindView(R.id.fragment_music_detail_center_sub_title)
    TextView mFragmentMusicDetailCenterSubTitle;


    private CommonAdapter mAdapter;
    private List<MediaBrowserCompat.MediaItem> mAdapterList = new ArrayList<>();
    private ImageLoader mImageLoader;
    private MediaMetadataCompat.Builder mBuilder;

    private static final String ARG_MEDIA_ID = "media_id";

    private String mMediaId;

    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;

    private MediaBrowserCompatProvider mCompatProvider;

    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                }

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                }
            };

    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    mAdapterList.addAll(children);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(@NonNull String id) {

                }
            };

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_detail;
    }

    @Override
    protected void initView(View rootView) {
        Bitmap bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.npc);
        final Palette palette = Palette.from(bitmap).generate();

        ViewGroup.LayoutParams titleParam;

        int titleHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleHeight = ConvertUtils.dp2px(getActivity(), 84);
        } else {
            mFragmentMusicDetailEmpty.setVisibility(View.GONE);
            titleHeight = ConvertUtils.dp2px(getActivity(), 64);
        }

        titleParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, titleHeight);
        mFragmentMusicDetailScrollview.setNotConsumeHeight(titleHeight);
        mFragmentMusicDetailTitle.setLayoutParams(titleParam);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mAdapter = getCommonAdapter();


        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap(bitmap, bitmap.getWidth
                (), bitmap.getHeight()));
        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);

        mImageLoader.loadImage(getActivity(), GlideImageConfig.builder()
                .transformation(new GlideStokeTransform(getActivity(), 15))
                .imagerView(mFragmentMusicDetailHeadIamge)
                .resourceId(R.mipmap.npc).build());

        mFragmentMusicDetailScrollview.setOnHeadFlingListener(new NestedScrollLineayLayout
                .OnHeadFlingListener() {

            @Override
            public void onHeadFling(int scrollY) {
                int distance = mFragmentMusicDetailScrollview.getTopViewHeight();
                int alpha = 255 * scrollY / distance;
                alpha = alpha > 255 ? 255 : alpha;
                mFragmentMusicDetailTitle.setBackgroundColor(palette.getLightVibrantColor
                        (0xdedede));
                if ((float) alpha / 255f > 0.7) {
                    mFragmentMusicDetailCenterTitle.setVisibility(View.VISIBLE);
                    mFragmentMusicDetailCenterSubTitle.setVisibility(View.VISIBLE);
                } else {
                    mFragmentMusicDetailCenterTitle.setVisibility(View.GONE);
                    mFragmentMusicDetailCenterSubTitle.setVisibility(View.GONE);
                }
                mFragmentMusicDetailTitle.getBackground().setAlpha(alpha);

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCompatProvider = (MediaBrowserCompatProvider) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mCompatProvider.getMediaBrowser().isConnected()) {
            onConnected();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCompatProvider.getMediaBrowser() != null && mCompatProvider.getMediaBrowser()
                .isConnected() && mMediaId != null) {
            mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
        }
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        if (controller != null) {
            controller.unregisterCallback(mMediaControllerCallback);
        }
    }

    @Override
    protected void initData() {
        mRvMusicDetailList.setAdapter(mAdapter);
        mRvMusicDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setPresenter(MusicDetailContract.Presenter presenter) {
        mPresenter = presenter;
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

    @OnClick({R.id.fragment_music_detail_share, R.id.fragment_music_detail_comment, R.id
            .fragment_music_detail_favorite, R.id.fragment_music_detail_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_detail_share:
                break;
            case R.id.fragment_music_detail_comment:
                break;
            case R.id.fragment_music_detail_favorite:
                break;
            case R.id.fragment_music_detail_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    public String getMediaId() {
        Bundle args = getArguments();
        if (args != null) {
            return args.getString(ARG_MEDIA_ID);
        }
        return null;
    }

    public void setMediaId(String mediaId) {
        Bundle args = new Bundle(1);
        args.putString(ARG_MEDIA_ID, mediaId);
        setArguments(args);
    }

    public void onConnected() {
        if (isDetached()) {
            return;
        }
        mMediaId = getMediaId();
        if (mMediaId == null) {
            mMediaId = mCompatProvider.getMediaBrowser().getRoot();
        }
        mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
        mCompatProvider.getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);
        MediaControllerCompat controller = getActivity()
                .getSupportMediaController();
        if (controller != null) {
            controller.registerCallback(mMediaControllerCallback);
        }
    }

    @NonNull
    private CommonAdapter<MusicAlbumListBean> getCommonAdapter() {
        mAdapter = new CommonAdapter<MediaBrowserCompat.MediaItem>(getActivity(), R.layout
                .item_music_detail_list,
                mAdapterList) {
            @Override
            protected void convert(ViewHolder holder, MediaBrowserCompat.MediaItem item, int
                    position) {
                Integer cachedState = (Integer) holder.itemView.getTag(R.id
                        .tag_mediaitem_state_cache);
                int state = getMediaItemState(item);
                if (cachedState == null || cachedState != state) {
                    holder.itemView.setTag(R.id.tag_mediaitem_state_cache, state);
                }

            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MediaBrowserCompat.MediaItem item = mAdapterList.get(position);
                if (item.isPlayable()) {

                    MediaControllerCompat controllerCompat = getActivity()
                            .getSupportMediaController();

                    controllerCompat.getTransportControls()
                            .playFromMediaId(item.getMediaId(), null);
                    mMediaId = item.getMediaId();
                } else if (item.isBrowsable()) {
                    mCompatProvider.getMediaBrowser().unsubscribe(item.getMediaId());
                    mCompatProvider.getMediaBrowser().subscribe(item.getMediaId(),
                            mSubscriptionCallback);
                } else {
                    startActivity(new Intent(getActivity(), MusicPlayActivity.class));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mAdapter;
    }

    public int getMediaItemState(MediaBrowserCompat.MediaItem mediaItem) {
        int state = STATE_NONE;
        if (mediaItem.isPlayable()) {
            state = STATE_PLAYABLE;
            if (MediaIDHelper.isMediaItemPlaying(getActivity(), mediaItem)) {
                state = getStateFromController();
            }
        }

        return state;
    }

    public int getStateFromController() {
        MediaControllerCompat controller = getActivity().getSupportMediaController();
        PlaybackStateCompat pbState = controller.getPlaybackState();
        if (pbState == null ||
                pbState.getState() == PlaybackStateCompat.STATE_ERROR) {
            return STATE_NONE;
        } else if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            return STATE_PLAYING;
        } else {
            return STATE_PAUSED;
        }
    }

    public interface MediaBrowserCompatProvider {
        MediaBrowserCompat getMediaBrowser();
    }
}
