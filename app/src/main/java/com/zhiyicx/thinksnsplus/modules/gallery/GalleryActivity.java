package com.zhiyicx.thinksnsplus.modules.gallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @author LiuChao
 * @describe 图片浏览器（画廊），用于网络图片的预览
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */
public class GalleryActivity extends TSActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected Fragment getFragment() {
        return GalleryFragment.initFragment(null);
    }
}
