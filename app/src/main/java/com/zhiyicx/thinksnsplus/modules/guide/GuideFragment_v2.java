package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhiyicx.thinksnsplus.widget.TCountTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

public class GuideFragment_v2 extends TSFragment<GuideContract.Presenter> implements
        GuideContract.View,
        OnBannerListener, ViewPager.OnPageChangeListener, TCountTimer.OnTimeListener {

    @BindView(R.id.guide_banner)
    Banner mGuideBanner;
    @BindView(R.id.guide_text)
    TextView mGuideText;

    TCountTimer mTimer;
    int mPosition;

    boolean isFinish;
    boolean isClick;
    boolean isFirst = true;

    public static final String ADVERT = "advert";

    private List<RealAdvertListBean> mBootAdverts;


    /**
     * Activity 手动调用处理
     *
     * @param intent
     */
    public void onNewIntent(Intent intent) {
        isClick = false;
        isFirst = false;
        if (isFinish || mPosition != 0) {
            mPresenter.checkLogin();
        }
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
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
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide_v2;
    }

    @Override
    protected void initView(View rootView) {
        RxView.clicks(mGuideText).throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> mPresenter.checkLogin());

        if (com.zhiyicx.common.BuildConfig.USE_ADVERT) {
            mBootAdverts = mPresenter.getBootAdvert();
            if (mBootAdverts != null) {
                List<String> urls = new ArrayList<>();
                for (RealAdvertListBean realAdvertListBean : mBootAdverts) {
                    urls.add(realAdvertListBean.getAdvertFormat().getImage().getImage());
                }
                int time = mBootAdverts.get(0).getAdvertFormat().getImage().getDuration() * 1000;
                time = time > 0 ? time : 5000;
                mGuideText.setVisibility(View.VISIBLE);
                mTimer = TCountTimer.builder()
                        .buildBtn(mGuideText)
                        .buildTimeCount(time)
                        .buildCanUseListener(urls.size() <= 1)// 单张图片
                        .buildOnTimeListener(this)
                        .buildCanUseOntick(false)
                        .build();
                mGuideBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
                mGuideBanner.setImageLoader(new BannerImageLoaderUtil());
                mGuideBanner.setImages(urls);
                mGuideBanner.isDownStopAutoPlay(false);
                mGuideBanner.setViewPagerIsScroll(false);
                mGuideBanner.setDelayTime(time);
                mGuideBanner.setOnBannerListener(this);
                mGuideBanner.setOnPageChangeListener(this);
            }
        }
    }

    @Override
    protected void initData() {
        mPresenter.initConfig();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            return;
        }
        if (com.zhiyicx.common.BuildConfig.USE_ADVERT && mBootAdverts != null) {
            initAdvert();
        } else {
            mPresenter.checkLogin();
        }
    }


    @Override
    public void startActivity(Class aClass) {
        repleaseAdvert();
        startActivity(new Intent(getActivity(), aClass));
        getActivity().finish();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        }
    }

    private void repleaseAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mTimer == null) {
            return;
        }
        mGuideBanner.setOnPageChangeListener(null);
        mGuideBanner.stopAutoPlay();
        mTimer.replease();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mGuideBanner == null) {
            return;
        }
        mPosition = mGuideBanner.getCurrentItem();
        if (mPosition == mGuideBanner.getItemCount() - 1) {
            mGuideBanner.stopAutoPlay();
        }

        if (mPosition > 0) {
            mTimer.replease();
            int time = mBootAdverts.get(position - 1).getAdvertFormat().getImage().getDuration() * 1000;
            time = time > 0 ? time : position * 2000;
            mGuideBanner.setDelayTime(time);
            mTimer = mTimer.newBuilder()
                    .buildTimeCount(time)
                    .buildCanUseOntick(true)
                    .buildDurText(getString(R.string.skip))
                    .buildCanUseListener(mPosition == mGuideBanner.getItemCount() - 1)
                    .buildOnTimeListener(this)
                    .build();
            mTimer.start();
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
        isFinish = true;
        if (isClick) {
            return;
        }
        mPresenter.checkLogin();
    }

    @Override
    public void OnBannerClick(int position) {

        isClick = true;
        if (isFinish) {
            return;
        }
        CustomWEBActivity.startToWEBActivity(getActivity(), mBootAdverts.get(position)
                        .getAdvertFormat().getImage().getLink(),
                mBootAdverts.get(position).getTitle(), ADVERT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.checkLogin();
    }

    @Override
    public void initAdvert() {
        mGuideBanner.start();
        mTimer.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mGuideBanner != null) {
            mGuideBanner.releaseBanner();
        }
    }
}
