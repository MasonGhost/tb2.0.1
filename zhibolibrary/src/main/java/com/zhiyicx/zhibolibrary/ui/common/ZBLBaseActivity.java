package com.zhiyicx.zhibolibrary.ui.common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhy.autolayout.AutoLayoutActivity;

import org.simple.eventbus.EventBus;

import java.util.LinkedList;

import butterknife.ButterKnife;

public abstract class ZBLBaseActivity extends AutoLayoutActivity {
    public static LinkedList<Activity> mActivityList;
    protected final String TAG = this.getClass().getSimpleName();
    private BroadcastReceiver mBroadcastReceiver;
    public static final String ACTION_RECEIVER_ACTIVITY = "com.zhibo.activity";

    public Toolbar mToolbar;
    TextView mTitle;

    public RxPermissions mRxPermissions;

    @Override
    protected void onResume() {
        super.onResume();
        registReceiver();//注册广播
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregistReceriver();
    }

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivityList == null) {
            mActivityList = new LinkedList<Activity>();
        }
        synchronized (mActivityList) {

            mActivityList.add(this);
        }
        EventBus.getDefault().register(this);//注册到事件主线
//        FullScreencall();//去掉navigationbar
        if (usePermisson()) { //是否需要权限验证，需要防止 initview 之前，防止 rxbinding初始化空
            mRxPermissions = new RxPermissions(this);
            mRxPermissions.setLogging(true);
        }
        initView();
        ButterKnife.bind(this);//绑定到butterknife

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);

        initToolBar();
        initData();
    }

    /**
     * 是否需要权限
     * @return
     */
    protected boolean usePermisson() {
        return false;
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    /**
     * 初始化toolbar
     */
    protected void initToolBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }


    /**
     * 退出所有activity
     */
    public static void killAll() {
        LinkedList<Activity> copy;
        synchronized (mActivityList) {
            copy = new LinkedList<Activity>(mActivityList);
        }
        for (Activity baseActivity : copy) {
            baseActivity.finish();
        }
//		android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static LinkedList<Activity> getActivityList() {
        return mActivityList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivityList) {
            mActivityList.remove(this);
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Anim.exit(this);
    }

    /**
     * 注册广播
     */
    public void registReceiver() {
        try {
            mBroadcastReceiver = new ActivityReceriver();
            IntentFilter filter = new IntentFilter(ACTION_RECEIVER_ACTIVITY);
            registerReceiver(mBroadcastReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解除注册广播
     */
    public void unregistReceriver() {
        if (mBroadcastReceiver == null) return;
        try {
            unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 用于处理当前activity需要
     */
    class ActivityReceriver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getStringExtra("type")) {
                    case "startActivity"://启动activity
                        Intent content = intent.getExtras().getParcelable("content");
                        startActivity(content);
                        Anim.in(ZBLBaseActivity.this);
                        break;
                    case "showSnackbar"://显示snackbar
                        String text = intent.getStringExtra("content");
                        boolean isLong = intent.getBooleanExtra("long", false);
                        View view = ZBLBaseActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
                        Snackbar.make(view, text, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
                        break;
                    default:
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }
}
