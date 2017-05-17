package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBFragment;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhiyicx.thinksnsplus.widget.TCountTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GuideFragment_v2 extends TSFragment<GuideContract.Presenter> implements GuideContract.View,
        OnBannerListener, ViewPager.OnPageChangeListener, TCountTimer.OnTimeListener {
    public static final int DEFAULT_DELAY_TIME = 1000;

    @BindView(R.id.guide_banner)
    Banner mGuideBanner;
    @BindView(R.id.guide_text)
    TextView mGuideText;

    TCountTimer mTimer;
    int mPosition;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide_v2;
    }

    @Override
    protected void initView(View rootView) {
        initAdvert();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }


    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.timer(DEFAULT_DELAY_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
//                        mPresenter.checkLogin();
                    }
                });
    }

    @Override
    public void startActivity(Class aClass) {
        startActivity(new Intent(getActivity(), aClass));
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @OnClick(R.id.guide_text)
    public void onViewClicked() {
        mPresenter.checkLogin();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mGuideBanner == null)
            return;
        mPosition = mGuideBanner.getCurrentItem();
        if (mPosition == 3) {
            mGuideBanner.stopAutoPlay();
        }
        if (mPosition > 0) {
            mTimer.replease();
            mTimer.newBuilder()
                    .buildCanUseOntick(true)
                    .buildDurText("跳过")
                    .buildCanUseListener(mPosition == 3)
                    .buildOnTimeListener(this)
                    .build()
                    .start();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTick() {

    }

    @Override
    public void onFinish() {
        mPresenter.checkLogin();
    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent(getActivity(), CustomWEBActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_URL, "http://www.baidu.com");
        bundle.putString(CustomWEBFragment.BUNDLE_PARAMS_WEB_TITLE, "lalalla");
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.checkLogin();
    }

    private void initAdvert() {

        mTimer = TCountTimer.builder()
                .buildBtn(mGuideText)
                .buildCanUseOntick(false)
                .build();

        mGuideBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        mGuideBanner.setImageLoader(new BannerImageLoaderUtil());
        List<String> urls = new ArrayList<>();
        urls.add("");
        urls.add("");
        urls.add("");
        urls.add("");
        mGuideBanner.setImages(urls);
        mGuideBanner.setViewPagerIsScroll(false);
        mGuideBanner.setDelayTime(5000);
        mGuideBanner.setOnBannerListener(this);
        mGuideBanner.setOnPageChangeListener(this);
        mGuideBanner.start();
        mTimer.start();
    }
}
