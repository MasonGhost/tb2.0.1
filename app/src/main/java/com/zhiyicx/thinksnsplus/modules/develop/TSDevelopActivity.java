package com.zhiyicx.thinksnsplus.modules.develop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.R;

import static com.zhiyicx.thinksnsplus.modules.develop.TSDevelopFragment.BUNDLE_CENTER_IMAGE;
import static com.zhiyicx.thinksnsplus.modules.develop.TSDevelopFragment.BUNDLE_TITLE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/24
 * @Contact master.jungle68@gmail.com
 */

public class TSDevelopActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return TSDevelopFragment.newInstance(getIntent().getStringExtra(BUNDLE_TITLE)
                , getIntent().getIntExtra(BUNDLE_CENTER_IMAGE, R.mipmap.pic_default_mall));
    }

    @Override
    protected void componentInject() {

    }

    public static void startDeveloperAcitvity(Context context, String title, int centerImage) {
        Intent intent = new Intent(context, TSDevelopActivity.class);
        intent.putExtra(BUNDLE_TITLE, title);
        intent.putExtra(BUNDLE_CENTER_IMAGE, centerImage);
        context.startActivity(intent);

    }
}
