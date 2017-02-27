package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.zhiyicx.baseproject.R;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */
public class RefreshFooterView extends LinearLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    private ImageView loadingMore;
    private TextView loadingText;
    /**
     * 底部加载更多菊花drawable
     */
    protected AnimationDrawable mFooterAd;

    public RefreshFooterView(Context context) {
        super(context);
        init(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.vw_footer, this);
        loadingMore = (ImageView) findViewById(R.id.iv_loading_more);
        loadingText = (TextView) findViewById(R.id.tv_loading_text);
        loadingMore.setImageResource(R.drawable.frame_loading_grey);

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {

    }

    @Override
    public void onRelease() {
        mFooterAd = (AnimationDrawable) loadingMore.getDrawable();
        if (mFooterAd != null) {
            mFooterAd.start();
        }
    }

    @Override
    public void onComplete() {
        stopFooterAd();
    }

    @Override
    public void onReset() {
        stopFooterAd();
    }

    private void stopFooterAd() {
        if (mFooterAd != null) {
            mFooterAd.stop();
            mFooterAd = null;
        }
    }

}
