package com.zhiyicx.thinksnsplus.modules.gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.adapter.PhotoPagerAdapter;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class GalleryFragment extends TSFragment {
    @BindView(R.id.vp_photos)
    ViewPager mVpPhotos;
    private PhotoPagerAdapter mPagerAdapter;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), new ArrayList<String>());
        mVpPhotos.setAdapter(mPagerAdapter);
    }

    @Override
    protected void initData() {
        mPagerAdapter.getPhotos().addAll(Arrays.asList(
                "http://cdn.duitang.com/uploads/item/201507/29/20150729112737_waACF.thumb.700_0.jpeg",
                "http://pic1.5442.com/2015/0604/07/05.jpg",
                "http://t-1.tuzhan.com/0f50dee8f485/c-2/l/2016/02/23/18/341ec1822a524c9eb30f5b615a1f8b3a.jpg",
                "http://pic1.5442.com/2015/0916/06/08.jpg",
                "http://pic1.win4000.com/wallpaper/a/565bdbdac0dc4.jpg",
                "http://pic1.5442.com/2015/0715/05/09.jpg"
        ));
        mPagerAdapter.notifyDataSetChanged();
    }

    public static GalleryFragment initFragment(Bundle bundle) {
        GalleryFragment galleryFragment = new GalleryFragment();
        galleryFragment.setArguments(bundle);
        return galleryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
