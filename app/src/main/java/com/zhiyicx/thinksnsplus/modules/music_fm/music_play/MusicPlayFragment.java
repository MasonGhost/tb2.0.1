package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.LoopPagerRecyclerView;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.PagerRecyclerView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicPlayFragment extends TSFragment<MusicPlayContract.Presenter> implements
        MusicPlayContract.View {


    @BindView(R.id.fragment_music_paly_phonograph_point)
    ImageView mFragmentMusicPalyPhonographPoint;
    @BindView(R.id.fragment_music_paly_share)
    ImageView mFragmentMusicPalyShare;
    @BindView(R.id.fragment_music_paly_like)
    ImageView mFragmentMusicPalyLike;
    @BindView(R.id.fragment_music_paly_comment)
    ImageView mFragmentMusicPalyComment;
    @BindView(R.id.fragment_music_paly_lyrics)
    ImageView mFragmentMusicPalyLyrics;
    @BindView(R.id.fragment_music_paly_progress)
    SeekBar mFragmentMusicPalyProgress;
    @BindView(R.id.fragment_music_paly_order)
    ImageView mFragmentMusicPalyOrder;
    @BindView(R.id.fragment_music_paly_preview)
    ImageView mFragmentMusicPalyPreview;
    @BindView(R.id.fragment_music_paly_palyer)
    ImageView mFragmentMusicPalyPalyer;
    @BindView(R.id.fragment_music_paly_nextview)
    ImageView mFragmentMusicPalyNextview;
    @BindView(R.id.fragment_music_paly_list)
    ImageView mFragmentMusicPalyList;
    @BindView(R.id.fragment_music_paly_bg)
    LinearLayout mFragmentMusicPalyBg;
    @BindView(R.id.fragment_music_paly_rv)
    LoopPagerRecyclerView mFragmentMusicPalyRv;
    private ImageLoader mImageLoader;
    private CommonAdapter mAdapter;
    private List<String> mStringList = new ArrayList<>();

    private MusicPlayService mMusicPlayService;
    private Palette mPalette;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_paly;
    }

    @Override
    protected void initView(View rootView) {
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
        mStringList.add("");
//        bindServiceConnection();

        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(20000);
        rotateAnimation.setFillAfter(true);

        RotateAnimation animation = new RotateAnimation(0, -30, Animation.RELATIVE_TO_SELF, 1f,
                Animation
                        .RELATIVE_TO_SELF, 1f);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(500);
        animation.setFillAfter(false);

        mAdapter = new CommonAdapter<String>(getActivity(), R.layout.item_music_play, mStringList) {
            @Override
            protected void convert(ViewHolder holder, String o, final int position) {
                final ImageView imageView = holder.getView(R.id.fragment_music_paly_phonograph);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(position+"");
                        imageView.startAnimation(rotateAnimation);
                    }
                });
            }
        };

        mFragmentMusicPalyRv.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        mFragmentMusicPalyRv.setTriggerOffset(0.4f);
        mFragmentMusicPalyRv.setFlingFactor(3f);
        mFragmentMusicPalyRv.setAdapter(mAdapter);
        mFragmentMusicPalyRv.setHasFixedSize(true);

        mFragmentMusicPalyRv.addOnPageChangedListener(new PagerRecyclerView.OnPageChangedListener
                () {

            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {

            }
        });



//        mFragmentMusicPalyPhonographPoint.startAnimation(animation);
//        mFragmentMusicPalyPhonograph.startAnimation(rotateAnimation);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        Bitmap bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.npc).copy(Bitmap.Config.ARGB_8888, true);

//        mPalette = Palette.from(bitmap).generate();
//        StatusBarUtils.setStatusBarColor(getActivity(), mPalette.getVibrantColor(0xe3e3e3));
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(grayColorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap(bitmap, bitmap.getWidth
                (), bitmap.getHeight()));
        mFragmentMusicPalyBg.setBackgroundDrawable(drawable);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(MusicPlayContract.Presenter presenter) {
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

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @OnClick({R.id.fragment_music_paly_share, R.id.fragment_music_paly_like, R.id
            .fragment_music_paly_comment, R.id.fragment_music_paly_lyrics, R.id
            .fragment_music_paly_order, R.id.fragment_music_paly_preview, R.id
            .fragment_music_paly_palyer, R.id.fragment_music_paly_nextview, R.id
            .fragment_music_paly_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_music_paly_share:
                break;
            case R.id.fragment_music_paly_like:
                break;
            case R.id.fragment_music_paly_comment:
                break;
            case R.id.fragment_music_paly_lyrics:
                break;
            case R.id.fragment_music_paly_order:
                break;
            case R.id.fragment_music_paly_preview:
                break;
            case R.id.fragment_music_paly_palyer:
//                mMusicPlayService.playOrPause();
                break;
            case R.id.fragment_music_paly_nextview:
                break;
            case R.id.fragment_music_paly_list:
                break;
            default:
                break;
        }
    }

    //  在Activity中调用 bindService 保持与 Service 的通信
    private void bindServiceConnection() {
        Intent intent = new Intent(getActivity(), MusicPlayService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, getActivity().BIND_AUTO_CREATE);
    }

    //  回调onServiceConnected 函数，通过IBinder 获取 Service对象，实现Activity与 Service的绑定
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicPlayService = ((MusicPlayService.MyBinder) (service)).getService();
            //mMusicPlayService.mediaPlayer.getDuration()
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicPlayService = null;
        }
    };
}
