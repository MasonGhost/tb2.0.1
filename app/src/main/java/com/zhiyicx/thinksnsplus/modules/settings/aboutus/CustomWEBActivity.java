package com.zhiyicx.thinksnsplus.modules.settings.aboutus;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.modules.guide.GuideActivity;
import com.zhiyicx.thinksnsplus.modules.guide.GuideFragment_v2;
import com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter;

import java.util.HashMap;

/**
 * @Describe 关于我们等网页
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class CustomWEBActivity extends TSActivity<RegisterPresenter, CustomWEBFragment> {

    private static String flag = "";

    public static void startToWEBActivity(Context context, String... args) {
        startToWEBActivity(context, null, args);

    }

    public static void startToWEBActivity(Context context, HashMap<String, String> headers, String... args) {
        flag = "";
        Intent intent = new Intent(context, CustomWEBActivity.class);
        Bundle bundle = new Bundle();
        if (args.length > 0) {
            try {
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_URL, args[0]);
                bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_TITLE, args[1]);
                if (headers != null) {
                    bundle.putSerializable(CustomWEBFragment.BUNDLE_PARAMS_WEB_HEADERS, headers);
                }
                flag = args[2];
            } catch (Exception e) {
            }
            intent.putExtras(bundle);
        }
        context.startActivity(intent);

    }

    public static void startToOutWEBActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String urlRege = "^http://[\\s\\S]+";
        if (!url.matches(MarkdownConfig.NETSITE_FORMAT) && !url.matches(urlRege)) {
            url = url.replace(MarkdownConfig.SCHEME_ZHIYI, "");
            url = MarkdownConfig.SCHEME_HTTP + url;
        }
        intent.setData(Uri.parse(url));
        if (!DeviceUtils.hasPreferredApplication(context, intent)) {
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        }
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        //  x5 内核使用 网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置：Activity在onCreate时需要设置:（这个对宿主没什么影响，建议声明
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (GuideFragment_v2.ADVERT.equals(flag)) {
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
