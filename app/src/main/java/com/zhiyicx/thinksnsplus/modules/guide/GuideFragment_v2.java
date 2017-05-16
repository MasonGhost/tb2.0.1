package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhiyicx.thinksnsplus.widget.TCountTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class GuideFragment_v2 extends TSFragment<GuideContract.Presenter> implements GuideContract.View {
    public static final int DEFAULT_DELAY_TIME = 1000;

    @BindView(R.id.guide_banner)
    Banner mGuideBanner;
    @BindView(R.id.guide_text)
    TextView mGuideText;

    TCountTimer mTimer;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide_v2;
    }

    @Override
    protected void initView(View rootView) {
        mTimer = new TCountTimer(mGuideText);
        mTimer.start();
        mGuideBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mGuideBanner.setImageLoader(new BannerImageLoaderUtil());
        List<String> urls = new ArrayList<>(4);
        urls.add("");
        urls.add("");
        urls.add("");
        urls.add("");
        mGuideBanner.setImages(urls);
        mGuideBanner.setDelayTime(5000);

        mGuideBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = mGuideBanner.toRealPosition(position);
                if (position > 0) {
                    mTimer.setBtnText("跳过");
                    mGuideText.setClickable(true);
                }
                mTimer.start();
                if (position == 3) {
                    mPresenter.checkLogin();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mGuideBanner.start();
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
        mGuideBanner.stopAutoPlay();
        startActivity(new Intent(getActivity(), aClass));
        getActivity().finish();
        getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @OnClick(R.id.guide_text)
    public void onViewClicked() {
        mPresenter.checkLogin();
    }
}
