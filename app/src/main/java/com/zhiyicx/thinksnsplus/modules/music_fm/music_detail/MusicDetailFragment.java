package com.zhiyicx.thinksnsplus.modules.music_fm.music_detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideStokeTransform;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicListBean;
import com.zhiyicx.thinksnsplus.widget.IconTextView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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
public class MusicDetailFragment extends TSFragment<MusicDetailContract.Presenter> implements
        MusicDetailContract.View {

    @BindView(R.id.fragment_music_detail_head_iamge)
    ImageView mFragmentMusicDetailHeadIamge;
    @BindView(R.id.fragment_music_detail_name)
    TextView mFragmentMusicDetailName;
    @BindView(R.id.fragment_music_detail_dec)
    TextView mFragmentMusicDetailDec;
    @BindView(R.id.fragment_music_detail_share)
    IconTextView mFragmentMusicDetailShare;
    @BindView(R.id.fragment_music_detail_comment)
    IconTextView mFragmentMusicDetailComment;
    @BindView(R.id.fragment_music_detail_favorite)
    IconTextView mFragmentMusicDetailFavorite;
    @BindView(R.id.fragment_music_detail_head_info)
    RelativeLayout mFragmentMusicDetailHeadInfo;
    @BindView(R.id.rv_music_detail_list)
    RecyclerView mRvMusicDetailList;
    @BindView(R.id.fragment_music_detail_back)
    TextView mFragmentMusicDetailBack;
    //    @BindView(R.id.fragment_music_detail_scrollview)
//    NestedScrollLineayLayout mFragmentMusicDetailScrollview;
    private CommonAdapter mAdapter;
    private List<MusicListBean> mMusicListBeen = new ArrayList<>();
    private ImageLoader mImageLoader;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_detail;
    }

    @Override
    protected void initView(View rootView) {
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mAdapter = getCommonAdapter();
//        mFragmentMusicDetailScrollview.setOnFlingistener(new NestedScrollLineayLayout
//                .OnFlingistener() {
//            @Override
//            public void onFling(int velocityY) {
//                LogUtils.e("滑动："+velocityY,velocityY);
//            }
//        });
    }

    @Override
    protected void initData() {
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        mMusicListBeen.add(new MusicListBean());
        Bitmap bitmap = BitmapFactory
                .decodeResource(getResources(), R.mipmap.npc);
        BitmapDrawable drawable = new BitmapDrawable(FastBlur.blurBitmap(bitmap, bitmap.getWidth
                (), bitmap.getHeight()));
        mFragmentMusicDetailHeadInfo.setBackgroundDrawable(drawable);

        mImageLoader.loadImage(getActivity(), GlideImageConfig.builder().transformation(new
                GlideStokeTransform(getActivity(), 20)).imagerView(mFragmentMusicDetailHeadIamge)
                .resourceId(R.mipmap.npc).build());
        HeaderAndFooterWrapper wrapper = new HeaderAndFooterWrapper(mAdapter);
        TextView textView = new IconTextView(getActivity());
        textView.setText("head");

        wrapper.addHeaderView(textView);
        mRvMusicDetailList.setAdapter(wrapper);
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

    @NonNull
    private CommonAdapter<MusicListBean> getCommonAdapter() {
        return new CommonAdapter<MusicListBean>(getActivity(), R.layout.item_music_detail_list,
                mMusicListBeen) {
            @Override
            protected void convert(ViewHolder holder, MusicListBean o, int position) {

            }
        };
    }

}
