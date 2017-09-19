package com.zhiyicx.zhibolibrary.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerHomeComponent;
import com.zhiyicx.zhibolibrary.di.module.HomeModule;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.ShareContent;
import com.zhiyicx.zhibolibrary.presenter.HomePresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.AdapterViewPager;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.NoPullViewPager;
import com.zhiyicx.zhibolibrary.ui.view.HomeView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBInitConfigManager;
import com.zhiyicx.zhibosdk.manage.listener.OnVertifyTokenCallbackListener;
import com.zhiyicx.zhibosdk.model.entity.ZBApiConfig;

import java.util.List;

import javax.inject.Inject;

import cn.sharesdk.framework.ShareSDK;

import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_CONTENT;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_TITLE;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.SHARE_URL;

/**
 * Created by zhiyicx on 2016/3/16.
 */
public class HomeActivity extends ZBLBaseActivity implements HomeView, ViewPager.OnPageChangeListener, View.OnClickListener {


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
        vertifyTickt();
        initDialog();


    }

    private void vertifyTickt() {
        ZBInitConfigManager.vertifyToken(getApplicationContext(),         //配置更新
                ZhiboApplication.getUserInfo().ticket, new OnVertifyTokenCallbackListener() {
                    @Override
                    public void onSuccess(ZBApiConfig zbApiConfig) {
                        /**
                         * 获取配置信息
                         */
                        ZBLApi.sZBApiConfig = zbApiConfig;
                        initViewPager();
                        initShareData();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        String message = UiUtils.getString(R.string.app_init_faile);
                        UiUtils.makeText(message);
                        goLogin();
                    }

                    @Override
                    public void onFial(String code, String message) {
                        if (message != null) UiUtils.makeText(message);
                        goLogin();
                    }
                });

        initFilterWord();
    }

    /**
     * 跳转登录界面
     */
    private void goLogin() {
        // TODO: 16/10/10 重新登录
//        ZBLBaseActivity.killAll();
//        Intent intent = new Intent(UiUtils.getContext(), LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        UiUtils.getContext().startActivity(intent);
    }

    /**
     * 铭感词过滤
     */
    private void initFilterWord() {
        if (ZhiboApplication.filter == null) {
            ((ZhiboApplication) getApplication()).initFilterWord();
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
        shareContent.title = SHARE_TITLE;
        shareContent.content = SHARE_CONTENT;
        shareContent.url = ZBInitConfigManager.getZBCloundDomain() + SHARE_URL;
    }

    private void initDialog() {
        mLoading = new ProgressDialog(this);
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);

    }

    //初始化viewpager
    private void initViewPager() {
        //设置缓存的个数
        mViewPager.setOffscreenPageLimit(4);
        mAdapter = new AdapterViewPager(getSupportFragmentManager());
        mFragmentList = mPresenter.getFragments();
        mAdapter.bindData(mFragmentList);//将List设置给adapter
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);//设置滚动监听

    }

    @Override
    public void onClick(View v) {
        if (mFragmentList == null) return;
        if (v.getId() == R.id.bt_home_live) {//直播列表页面
            mFragmentList.get(0).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(0, false);
        }
        else if (v.getId() == R.id.bt_home_replay) {
            //回放页面
            mFragmentList.get(1).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(1, false);
        }
        else if (v.getId() == R.id.bt_home_add) {
//初始化直播间
            mPresenter.initStream();
        }
        else if (v.getId() == R.id.bt_home_message) {
//消息页面
            mFragmentList.get(2).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(2, false);
        }
        else if (v.getId() == R.id.bt_home_my) {
//我的页面
            mFragmentList.get(1).setData();//加载当前页，并跳转当当前页
            mViewPager.setCurrentItem(1, false);
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
        ShareSDK.stopSDK(this);
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
        mLiveTV.setTextColor(position == 0 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("gray"));
        mReplayIV.setImageResource(position == 1 ? R.mipmap.ic_bottom_replay_blue : R.mipmap.ic_bottom_replay_grey);
        mReplayTV.setTextColor(position == 1 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("gray"));
        mMessageIV.setImageResource(position == 2 ? R.mipmap.ic_bottom_chat_blue : R.mipmap.ic_bottom_chat_grey);
        mMessageTV.setTextColor(position == 2 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("gray"));
        mMyIV.setImageResource(position == 1 ? R.mipmap.ico_bottom_myself_blue : R.mipmap.ico_bottom_myself_grey);
        mMyTV.setTextColor(position == 1 ? UiUtils.getColor("color_blue_button") : UiUtils.getColor("gray"));
    }

    private void hideChildFilter() {
        mFragmentList.get(0).setData(true);//隐藏筛选页面
        mFragmentList.get(1).setData(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        ZBLBaseActivity.killAll();


    }
}
