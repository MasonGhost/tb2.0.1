package com.zhiyicx.zhibolibrary.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.Permission;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerHomeComponent;
import com.zhiyicx.zhibolibrary.di.module.HomeModule;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.presenter.HomePresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.AdapterViewPager;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.NoPullViewPager;
import com.zhiyicx.zhibolibrary.ui.view.HomeView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;

import java.util.List;

import javax.inject.Inject;


import rx.functions.Action1;

import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_CONTENT;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_TITLE;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_URL;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public class ZBLHomeActivity extends ZBLBaseActivity implements HomeView, ViewPager.OnPageChangeListener, View.OnClickListener {


    NoPullViewPager mViewPager;
    ImageView mLiveIV;
    ImageView mReplayIV;
    ImageView mMessageIV;

    ImageView mMyIV;
    TextView mLiveTV;

    TextView mReplayTV;

    TextView mMessageTV;

    TextView mMyTV;

    View tabHome;


    @Inject
    HomePresenter mPresenter;

    private List<ZBLBaseFragment> mFragmentList;
    private AdapterViewPager mAdapter;
    private ProgressDialog mLoading;


    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.zb_activity_home);

        findView();


    }

    private void findView() {
        mViewPager = (NoPullViewPager) findViewById(R.id.vp_home);

        mLiveIV = (ImageView) findViewById(R.id.iv_home_live);
        findViewById(R.id.bt_home_live).setOnClickListener(this);
        mReplayIV = (ImageView) findViewById(R.id.iv_home_replay);
        findViewById(R.id.bt_home_replay).setOnClickListener(this);
        mMessageIV = (ImageView) findViewById(R.id.iv_home_message);
        findViewById(R.id.bt_home_message).setOnClickListener(this);
        mMyIV = (ImageView) findViewById(R.id.iv_home_my);
        findViewById(R.id.bt_home_my).setOnClickListener(this);
        findViewById(R.id.bt_home_add).setOnClickListener(this);
        mLiveTV = (TextView) findViewById(R.id.tv_home_live);
        mReplayTV = (TextView) findViewById(R.id.tv_home_replay);
        mMessageTV = (TextView) findViewById(R.id.tv_home_message);
        mMyTV = (TextView) findViewById(R.id.tv_home_my);
        tabHome = findViewById(R.id.ll_home);
    }


    @Override
    protected void initData() {

        DaggerHomeComponent//注入
                .builder()
                .homeModule(new HomeModule(this))
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .build()
                .inject(this);
        initViewPager();
        initShareData();
        initFilterWord();
        initDialog();


    }

    /**
     * 铭感词过滤
     */
    private void initFilterWord() {
        if (ZhiboApplication.filter == null) {
            ZhiboApplication.initFilterWord();
        }
    }

    /**
     * 初始化分享数据
     */
    private void initShareData() {
        /**
         * 暂时使用本地
         */
        ShareContent shareContent = new ShareContent();
        ZhiboApplication.setShareContent(shareContent);
        shareContent.setTitle(SHARE_TITLE);
        shareContent.setContent(SHARE_CONTENT);
        shareContent.setUrl(ZBInitConfigManager.getZBCloundDomain() + SHARE_URL);
    }

    private void initDialog() {
        mLoading = new ProgressDialog(this);
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);

    }

    //初始化viewpager
    private void initViewPager() {
        //设置缓存的个数
        mViewPager.setOffscreenPageLimit(3);
        mAdapter = new AdapterViewPager(getSupportFragmentManager());
        mFragmentList = mPresenter.getFragments();
        mAdapter.bindData(mFragmentList);//将List设置给adapter
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);//设置滚动监听

    }

    @Override
    public void onClick(View v) {
        if (mFragmentList == null) {
            return;
        }
        if (v.getId() == R.id.bt_home_live) {//直播列表页面
            mFragmentList.get(0).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(0, false);
        } else if (v.getId() == R.id.bt_home_replay) {
            //回放页面
            mFragmentList.get(1).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(1, false);
        } else if (v.getId() == R.id.bt_home_add) {
//            mPresenter.initStream();
//初始化直播间
            // 添加相机权限设置
            mRxPermissions
                    .requestEach(Manifest.permission.CAMERA)
                    .subscribe(new Action1<Permission>() {
                        @Override
                        public void call(Permission permission) {
                            if (permission.granted) {
                                // 权限被允许
                                mPresenter.initStream();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 权限没有被彻底禁止
                            } else {
                                // 权限被彻底禁止
                                UiUtils.SnackbarText(getString(R.string.use_camea_permisson_tip));
                            }
                        }
                    });

        } else if (v.getId() == R.id.bt_home_message) {
//消息页面
            mFragmentList.get(2).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(2, false);
        } else if (v.getId() == R.id.bt_home_my) {
            startActivity(new Intent(UiUtils.getContext(), ZBLSearchActivity.class));//跳转到搜索页面
            overridePendingTransition(R.anim.vote_slide_in_from_left, R.anim.animate_null);//动画
        }


    }


    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.dismiss();
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void launchLiveRoom(Intent intent) {
        startActivity(intent);
        Anim.startActivityFromBottom(this);//进入动画
    }

    @Override
    public void launchEndStreamActivity(Intent intent) {
        startActivity(intent);
        Anim.startActivityFromBottom(this);
    }


    //    @Subscriber(tag = "launch_page", mode = ThreadMode.MAIN)
    @Override
    public void launchPage(int page) {
        mViewPager.setCurrentItem(page, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        hideChildFilter();
        changeNavigationButton(position);//改变导航栏按钮的状态
    }

    private void changeNavigationButton(int position) {
        mLiveIV.setImageResource(position == 0 ? R.mipmap.ic_bottom_live_blue : R.mipmap.ic_bottom_live_grey);
        mLiveTV.setTextColor(position == 0 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
        mReplayIV.setImageResource(position == 1 ? R.mipmap.ic_bottom_replay_blue : R.mipmap.ic_bottom_replay_grey);
        mReplayTV.setTextColor(position == 1 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
        mMessageIV.setImageResource(position == 2 ? R.mipmap.ic_bottom_chat_blue : R.mipmap.ic_bottom_chat_grey);
        mMessageTV.setTextColor(position == 2 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("white"));
//        mMyIV.setImageResource(position == 1 ? R.mipmap.ico_bottom_myself_blue : R.mipmap.ico_bottom_myself_grey);
//        mMyTV.setTextColor(position == 1 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("gray"));
    }

    private void hideChildFilter() {
        mFragmentList.get(0).setData(true);//隐藏筛选页面
        mFragmentList.get(1).setData(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
