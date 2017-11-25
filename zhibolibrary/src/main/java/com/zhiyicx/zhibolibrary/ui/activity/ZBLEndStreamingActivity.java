package com.zhiyicx.zhibolibrary.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.azoft.carousellayoutmanager.ItemTransformation;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerEndStreamComponent;
import com.zhiyicx.zhibolibrary.di.module.EndStreamModule;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.presenter.EndStreamPresenter;
import com.zhiyicx.zhibolibrary.ui.adapter.DefaultAdapter;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.CarouselView;
import com.zhiyicx.zhibolibrary.ui.view.EndStreamView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import javax.inject.Inject;

/**
 * Created by zhiyicx on 2016/3/28.
 */
public class ZBLEndStreamingActivity extends ZBLBaseActivity implements EndStreamView, View.OnClickListener {
    protected LinearLayout mExceptionLL;
    protected TextView mPlayTV;
    protected TextView mFansTV;
    protected TextView mPostfixTV;
    protected TextView mStarTV;
    protected LinearLayout mStarLL;
    protected TextView mGoldTV;
    protected LinearLayout mGoldLL;
    protected CarouselView mCarouselView;
    protected RecyclerView mRecyclerView;
    protected Button mTopButton;
    protected ImageButton ibEndStreamWeixin;
    protected ImageButton ibEndStreamFriend;
    protected ImageButton ibEndStreamQq;
    protected ImageButton ibEndStreamSina;
    protected ImageButton ibEndStreamZone;
    protected Button btEndStreamGoHome;
    @Inject
    EndStreamPresenter mPresenter;


    private CarouselLayoutManager mLayoutManager;
    private ProgressDialog mLoading;
    private long mTime;

    private boolean isAudience;

    @SuppressLint("ActivityLayoutNameNotPrefixed")
    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_end_stream);
        mExceptionLL = (LinearLayout) findViewById(R.id.ll_end_stream_exception_prompt);
        mPlayTV = (TextView) findViewById(R.id.tv_end_play);
        mFansTV = (TextView) findViewById(R.id.tv_end_stream_fans);
        mPostfixTV = (TextView) findViewById(R.id.tv_end_stream_fans_postfix);
        mStarTV = (TextView) findViewById(R.id.tv_end_stream_star);
        mStarLL = (LinearLayout) findViewById(R.id.ll_end_stream_star);
        mGoldTV = (TextView) findViewById(R.id.tv_end_stream_gold);
        mGoldLL = (LinearLayout) findViewById(R.id.ll_end_stream_gold);
        mCarouselView = (CarouselView) findViewById(R.id.cv_end_stream_recommend);
        mRecyclerView = (RecyclerView) findViewById(R.id.end_stream_recyclerView);
        mTopButton = (Button) findViewById(R.id.bt_end_stream_my_room);
        mTopButton.setOnClickListener(ZBLEndStreamingActivity.this);
        ibEndStreamWeixin = (ImageButton) findViewById(R.id.ib_end_stream_weixin);
        ibEndStreamWeixin.setOnClickListener(ZBLEndStreamingActivity.this);
        ibEndStreamFriend = (ImageButton) findViewById(R.id.ib_end_stream_friend);
        ibEndStreamFriend.setOnClickListener(ZBLEndStreamingActivity.this);
        ibEndStreamQq = (ImageButton) findViewById(R.id.ib_end_stream_qq);
        ibEndStreamQq.setOnClickListener(ZBLEndStreamingActivity.this);
        ibEndStreamSina = (ImageButton) findViewById(R.id.ib_end_stream_sina);
        ibEndStreamSina.setOnClickListener(ZBLEndStreamingActivity.this);
        ibEndStreamZone = (ImageButton) findViewById(R.id.ib_end_stream_zone);
        ibEndStreamZone.setOnClickListener(ZBLEndStreamingActivity.this);
        btEndStreamGoHome = (Button) findViewById(R.id.bt_end_stream_go_home);
        btEndStreamGoHome.setOnClickListener(ZBLEndStreamingActivity.this);

    }

    @Override
    protected void initData() {
        DaggerEndStreamComponent//注入
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .endStreamModule(new EndStreamModule(this))
                .build()
                .inject(this);
        initDialog();
        initRecycleView();
        mPresenter.getIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.getIntent(intent);
    }

    private void initRecycleView() {
        mLayoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);
        mLayoutManager.setMaxVisibleItems(1);
        mLayoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener() {
            @Override
            public ItemTransformation transformChild(@NonNull View child, float itemPositionToCenterDiff, int orientation) {
                float scale = (float) (2 * (2 * -StrictMath.atan(Math.abs(itemPositionToCenterDiff * 0.5) + 1.0) / Math.PI + 1));
//                System.out.println("scale = " + scale);
                // because scaling will make view smaller in its center, then we should move this item to the top or bottom to make it visible
                final float translateY;
                final float translateX;
                if (CarouselLayoutManager.VERTICAL == orientation) {
                    final float translateYGeneral = child.getMeasuredHeight() * (1 - scale) / 2f;
                    translateY = Math.signum(itemPositionToCenterDiff) * translateYGeneral;
                    translateX = 0;
//                    System.out.println("VERTICAL -----translateY= " + translateY);
                }
                else {
                    final float translateXGeneral = child.getMeasuredWidth() * (1 - scale) / 2f;
                    translateX = Math.signum(itemPositionToCenterDiff) * translateXGeneral;
                    translateY = 0;
//                    System.out.println("VERTICAL -----translateX= " + translateX);
                }
                return new ItemTransformation(scale, scale, translateX, translateY);
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new CenterScrollListener());
    }


    private void initDialog() {
        mLoading = new ProgressDialog(this);
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
        killMyself();
    }


    @Override
    public void killMyself() {
        finish();
        exitAnimation();//退出动画
    }

    private void exitAnimation() {
        if (isAudience) {
            this.overridePendingTransition(R.anim.animate_null, R.anim.slide_out_from_right);//动画
        }
        else {
            Anim.startActivityFromTop(this);
        }
    }

    @Override
    public void setFans(String s) {
        mFansTV.setText(s);
    }

    @Override
    public void setStar(String s) {
        mStarTV.setText(s);
    }

    @Override
    public void setGold(String s) {
        mGoldTV.setText(s);
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
    public void setGoldVisible(Boolean isVisible) {
        mGoldLL.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setStarVisible(Boolean isVisible) {
        mStarLL.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRecycleViewVisible(boolean isVisible) {
        mRecyclerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initListener() {
        mLayoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {
            @Override
            public void onCenterItemChanged(int adapterPosition) {
                LogUtils.warnInfo(TAG, "..." + adapterPosition);
            }
        });
    }

    @Override
    public void setAdapter(DefaultAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.animate_null);

    }


    @Override
    public void isAudience(boolean isAudience) {
        this.isAudience = isAudience;
        if (isAudience) {//观众结束界面
            mPlayTV.setTextColor(UiUtils.getColor("title_blue"));
            mPostfixTV.setText("人看过");
            mTopButton.setText("+ 关注主播");
        }
        else {//主播结束页面
            mPlayTV.setPadding(0, 140, 0, 0);
            mTopButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        if (isFollow) {//已关注
            mTopButton.setText("已关注");
            mTopButton.setEnabled(false);
        }
        else {//未关注
            mTopButton.setText("+ 关注主播");
            mTopButton.setEnabled(true);
        }
    }

    @Override
    public void showExceptionPrompt(boolean isVisable) {
        mExceptionLL.setVisibility(isVisable ? View.VISIBLE : View.GONE);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_end_stream_weixin) {
            mPresenter.shareWechat(ZBLEndStreamingActivity.this);
        }
        else if (view.getId() == R.id.ib_end_stream_friend) {
            mPresenter.shareMoment(ZBLEndStreamingActivity.this);
        }
        else if (view.getId() == R.id.ib_end_stream_qq) {
            mPresenter.shareQQ(ZBLEndStreamingActivity.this);
        }
        else if (view.getId() == R.id.ib_end_stream_sina) {
            mPresenter.shareWeibo(ZBLEndStreamingActivity.this);
        }
        else if (view.getId() == R.id.ib_end_stream_zone) {
            mPresenter.shareZone(ZBLEndStreamingActivity.this);
        }
        else if (view.getId() == R.id.bt_end_stream_my_room) {
            if (isAudience) {//关注主播
                long currentTime = System.currentTimeMillis();//频繁请求提示
                if ((currentTime - mTime) < UserService.FOLLOW_SPACING_TIME) {
                    showMessage(UiUtils.getString("str_frequently_follow_prompt"));
                    return;
                }
                else {
                    mTime = System.currentTimeMillis();
                }
                mPresenter.follow();
            }
            else {//返回首页
//                    EventBus.getDefault().post(3, "launch_page");//返回用户主页
                killMyself();
            }
        }
        else if (view.getId() == R.id.bt_end_stream_go_home) {
//                EventBus.getDefault().post(0, "launch_page");
//            Intent to = new Intent(ZBLEndStreamingActivity.this, ZBLHomeActivity.class);
//            startActivity(to);
            killMyself();//退出动画
        }
    }
}
