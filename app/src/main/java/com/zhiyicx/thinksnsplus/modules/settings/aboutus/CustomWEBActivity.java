package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;
import com.zhiyicx.thinksnsplus.modules.guide.GuideFragment_v2;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

/**
 * @Describe 关于我们等网页
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class CustomWEBActivity extends TSActivity<RegisterPresenter, CustomWEBFragment> {

    private static String flag = "";

    public static void startToWEBActivity(Context context, String... args) {
        flag = "";
        Intent intent = new Intent(context, CustomWEBActivity.class);
        Bundle bundle = new Bundle();
        if (args.length > 0) {
            try {
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_URL, args[0]);
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_TITLE, args[1]);
                flag = args[2];
            } catch (Exception e) {
            }
            intent.putExtras(bundle);
        }
        context.startActivity(intent);

    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (flag.equals(GuideFragment_v2.ADVERT)) {
            finish();
            startActivity(new Intent(this, GuideActivity.class));
        }
    }

    @Override
    protected CustomWEBFragment getFragment() {
        return CustomWEBFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    /**
     * 覆盖系统的回退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mContanierFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
