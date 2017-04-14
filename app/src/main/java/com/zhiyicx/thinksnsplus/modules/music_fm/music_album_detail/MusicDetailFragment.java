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
import android.support.v4.media.session.MediaSessionCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideStokeTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentHeader;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.MediaIDHelper;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_helper.WindowUtils;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_play.MusicPlayActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;
import com.zhiyicx.thinksnsplus.widget.NestedScrollLineayLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_MUSIC_LIKE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.bak_paly.PlaybackManager.MUSIC_ACTION;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list.MusicListFragment.BUNDLE_MUSIC_ABLUM;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;

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
    @BindView(R.id.nestedscroll_target)
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
    @BindView(R.id.fragment_music_detail_center_title)
    TextView mFragmentMusicDetailCenterTitle;
    @BindView(R.id.fragment_music_detail_center_sub_title)
    TextView mFragmentMusicDetailCenterSubTitle;
    @BindView(R.id.fragment_music_detail_playvolume)
    IconTextView mFragmentMusicDetailPlayvolume;
    @BindView(R.id.fragment_music_detail_share)
    IconTextView mFragmentMusicDetailShare;
    @BindView(R.id.fragment_music_detail_comment)
    IconTextView mFragmentMusicDetailComment;
    @BindView(R.id.fragment_music_detail_favorite)
    IconTextView mFragmentMusicDetailFavorite;
    @BindView(R.id.fragment_music_detail_music_count)
    TextView fragmentMusicDetailMusicCount;

    private CommonAdapter mAdapter;
    private List<MediaBrowserCompat.MediaItem> mAdapterList = new ArrayList<>();
    private ImageLoader mImageLoader;

    private static final String ARG_MEDIA_ID = "media_id";
    public static final String MUSIC_INFO = "music_info";
    private String mCurrentMediaId = "-1";
    private Bitmap mBgBitmap;

    private String mMediaId;
    private Palette mPalette;

    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;

    private MediaBrowserCompatProvider mCompatProvider;
    private MusicAlbumListBean mMusicAlbumListBean;
    private MusicAlbumDetailsBean mAlbumDetailsBean;

    /**
     * 音乐切换回掉
     */
    private final MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                    super.onMetadataChanged(metadata);
                    if (metadata == null) {
                        return;
                    }
                    mAdapter.notifyDataSetChanged();
                    mCurrentMediaId = metadata.getDescription().getMediaId();
                    mPresenter.getMusicDetails(mCurrentMediaId);
                }

                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    super.onPlaybackStateChanged(state);
                }
            };

    /**
     * 音乐数据变更回掉
     */
    private final MediaBrowserCompat.SubscriptionCallback mSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    MediaSessionCompat.QueueItem mCurrentMusic = AppApplication.getmQueueManager().getCurrentMusic();
                    if (mCurrentMusic != null) {
                        mCurrentMediaId = MediaIDHelper.extractMusicIDFromMediaID(mCurrentMusic.getDescription().getMediaId());
                    }
                    fragmentMusicDetailMusicCount.setText(String.format("(共%d首)", children.size()));
                    mAdapter.dataChange(children);
                }

                @Override
                public void onError(@NonNull String id) {

                }
            };

    public static MusicDetailFragment newInstance(Bundle param) {
        MusicDetailFragment fragment = new MusicDetailFragment();
        fragment.setArguments(param);
        return fragment;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_detail;
    }

    @Override
    protected void initView(View rootView) {
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
    }

    @Override
    protected void initData() {
        mMusicAlbumListBean = getArguments().getParcelable(BUNDLE_MUSIC_ABLUM);
        mAlbumDetailsBean = mPresenter.getCacheAblumDetail(mMusicAlbumListBean.getId());
        initHeadInfo(mMusicAlbumListBean);
        mPresenter.getMusicAblum(mMusicAlbumListBean.getId() + "");
        mAdapter = getCommonAdapter();
        mRvMusicDetailList.setAdapter(mAdapter);
        mRvMusicDetailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        initTitle();
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
    public void setPresenter(MusicDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setMusicAblum(MusicAlbumDetailsBean musicAblum) {
        mAlbumDetailsBean = musicAblum;
        mFragmentMusicDetailCenterTitle.setText(mAlbumDetailsBean.getTitle());
        mFragmentMusicDetailCenterSubTitle.setText(mAlbumDetailsBean.getIntro());
        mFragmentMusicDetailDec.setText(mAlbumDetailsBean.getIntro());

        mMusicAlbumListBean.setIs_collection(mAlbumDetailsBean.getIs_collection());

        if (mAlbumDetailsBean.getIs_collection() != 0) {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.detail_ico_collect);
        }
        if (mCompatProvider.getMediaBrowser().isConnected()) {
            onConnected();
        }
    }

    @Override
    public void setCollect(boolean isCollected) {
        if (isCollected) {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.detail_ico_collect);
        } else {
            mFragmentMusicDetailFavorite.setIconRes(R.mipmap.music_ico_collect);
        }
        mFragmentMusicDetailFavorite.setText(mAlbumDetailsBean.getCollect_count() + "");
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
        return mMediaId;
    }

    public void setMediaId(String mediaId) {
        mMediaId = mediaId;
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
            if (mAlbumDetailsBean != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_ACTION, mAlbumDetailsBean);
                controller.getTransportControls().sendCustomAction(MUSIC_ACTION, bundle);

                mCompatProvider.getMediaBrowser().unsubscribe(mMediaId);
                mCompatProvider.getMediaBrowser().subscribe(mMediaId, mSubscriptionCallback);
            }
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
                TextView musicName = holder.getView(R.id.item_music_name);
                TextView authorName = holder.getView(R.id.item_music_author);
                musicName.setText(item.getDescription().getTitle());
                authorName.setText("-" + item.getDescription().getSubtitle());

                Integer cachedState = (Integer) holder.itemView.getTag(R.id
                        .tag_mediaitem_state_cache);
                int state = getMediaItemState(item);
                if (cachedState == null || cachedState != state) {
                    holder.itemView.setTag(R.id.tag_mediaitem_state_cache, state);
                }
                if (mCurrentMediaId.equals(MediaIDHelper.extractMusicIDFromMediaID(item.getMediaId
                        ()))) {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_theme));
                    authorName.setTextColor(getResources().getColor(R.color.important_for_theme));
                } else {
                    musicName.setTextColor(getResources().getColor(R.color.important_for_content));
                    authorName.setTextColor(getResources().getColor(R.color
                            .normal_for_assist_text));
                }
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                MediaBrowserCompat.MediaItem item = mAdapterList.get(position);

                Intent intent = new Intent(getActivity(), MusicPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_INFO, mAlbumDetailsBean);
                intent.putExtra(MUSIC_INFO, bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                MediaControllerCompat controller = getActivity().getSupportMediaController();
                MediaMetadataCompat metadata = controller.getMetadata();
                if (metadata != null) {
                    intent.putExtra(MusicDetailActivity.EXTRA_CURRENT_MEDIA_DESCRIPTION,
                            metadata.getDescription());
                }
                startActivity(intent);
                MediaSessionCompat.QueueItem mCurrentMusic = AppApplication.getmQueueManager().getCurrentMusic();
                if (mCurrentMusic != null) {
                    mMediaId = mCurrentMusic.getDescription().getMediaId();
                }

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

    @Override
    public MusicAlbumDetailsBean getCurrentAblum() {
        return mAlbumDetailsBean;
    }

    @Override
    public MusicAlbumListBean getmMusicAlbumListBean() {
        return mMusicAlbumListBean;
    }

    private void initTitle() {
        mBgBitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.icon_256);
        mPalette = Palette.from(mBgBitmap).generate();
        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap
                (mBgBitmap, mBgBitmap.getWidth(), mBgBitmap.getHeight()));
        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);

        mFragmentMusicDetailScrollview.setOnHeadFlingListener(new NestedScrollLineayLayout
                .OnHeadFlingListener() {
            @Override
            public void onHeadFling(int scrollY) {
                int distance = mFragmentMusicDetailScrollview.getTopViewHeight();
                int alpha = 255 * scrollY / distance;
                alpha = alpha > 255 ? 255 : alpha;
                mFragmentMusicDetailTitle.setBackgroundColor(mPalette.getDarkVibrantColor
                        (0xffdedede));
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

    private void initHeadInfo(MusicAlbumListBean albumListBean) {
        mFragmentMusicDetailName.setText(albumListBean.getTitle());
        mFragmentMusicDetailDec.setText(albumListBean.getIntro());
        mFragmentMusicDetailShare.setText(albumListBean.getShare_count() + "");
        mFragmentMusicDetailComment.setText(albumListBean.getComment_count() + "");
        mFragmentMusicDetailFavorite.setText(albumListBean.getCollect_count() + "");
        mFragmentMusicDetailPlayvolume.setText(albumListBean.getTaste_count() + "");

        Glide.with(getContext())
                .load(ImageUtils.imagePathConvert(albumListBean.getStorage().getId() + "",
                        ImageZipConfig.IMAGE_70_ZIP))
                .asBitmap()
                .transform(new GlideStokeTransform(getActivity(), 20))
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        mBgBitmap = resource.copy(Bitmap.Config.RGB_565, false);
                        mFragmentMusicDetailHeadIamge.setImageBitmap(resource);
                        mPalette = Palette.from(mBgBitmap).generate();
                        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap
                                (mBgBitmap, mBgBitmap.getWidth(), mBgBitmap.getHeight()));
                        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);
                    }
                });
    }

    public interface MediaBrowserCompatProvider {
        MediaBrowserCompat getMediaBrowser();
    }

    public class MusicAblumHeadInfo {
        private int iamgeId;
    }

    @OnClick({R.id.fragment_music_detail_playvolume, R.id.fragment_music_detail_share, R.id
            .fragment_music_detail_comment, R.id.fragment_music_detail_favorite,
            R.id.fragment_music_detail_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_detail_playvolume:
                break;
            case R.id.fragment_music_detail_share:
                mPresenter.shareMusicAlbum();
                break;
            case R.id.fragment_music_detail_comment:
                Intent intent = new Intent(getActivity(), MusicCommentActivity.class);
                Bundle musicBundle = new Bundle();
                MusicCommentHeader.HeaderInfo headerInfo = new MusicCommentHeader.HeaderInfo();
                headerInfo.setCommentCount(mMusicAlbumListBean.getComment_count());
                headerInfo.setId(mMusicAlbumListBean.getId());
                headerInfo.setTitle(mMusicAlbumListBean.getTitle());
                headerInfo.setLitenerCount(mMusicAlbumListBean.getTaste_count() + "");
                headerInfo.setImageUrl(ImageUtils.imagePathConvert(mMusicAlbumListBean.getStorage().getId() + "",
                        ImageZipConfig.IMAGE_70_ZIP));
                musicBundle.putSerializable(CURRENT_COMMENT, headerInfo);
                intent.putExtra(CURRENT_COMMENT, musicBundle);
                startActivity(intent);
                break;
            case R.id.fragment_music_detail_favorite:
                mPresenter.handleCollect(mAlbumDetailsBean.getIs_collection() == 0, mAlbumDetailsBean.getId() + "");
                break;
            case R.id.fragment_music_detail_back:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    @Subscriber(tag = EVENT_MUSIC_LIKE, mode = ThreadMode.MAIN)
    public void onLikeCountUpdate(final MusicAlbumDetailsBean.MusicsBean e_albumListBean) {

        Observable.from(mAlbumDetailsBean.getMusics()).filter(new Func1<MusicAlbumDetailsBean.MusicsBean, Boolean>() {
            @Override
            public Boolean call(MusicAlbumDetailsBean.MusicsBean musicsBean) {
                return e_albumListBean.getId() == musicsBean.getId();
            }
        }).subscribe(new Action1<MusicAlbumDetailsBean.MusicsBean>() {
            @Override
            public void call(MusicAlbumDetailsBean.MusicsBean musicsBean) {
                musicsBean.getMusic_info().setIsdiggmusic(e_albumListBean.getMusic_info().getIsdiggmusic());
                musicsBean.getMusic_info().setComment_count(e_albumListBean.getMusic_info().getComment_count());
            }
        });
        LogUtils.d("EVENT_MUSIC_LIKE");
    }
}
