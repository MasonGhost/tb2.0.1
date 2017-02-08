package com.zhiyicx.thinksnsplus.modules.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class GalleryPhotoAdapter extends FragmentStatePagerAdapter {


    public List<String> fileList;

    public GalleryPhotoAdapter(FragmentManager fm, List<String> fileList) {
        super(fm);
        this.fileList = fileList;
    }

    @Override
    public int getCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public Fragment getItem(int position) {
        String url = fileList.get(position);
        ImageDetailFragment imageDetailFragment = ImageDetailFragment.newInstance(url);
        return imageDetailFragment;
    }

}
