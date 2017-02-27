package com.zhiyicx.baseproject.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.zhiyicx.baseproject.R;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/6/3.
 */
public class RefreshHeaderView extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {
    private TextView header_hint_text;
    private ImageView header_arrow;
    private ProgressBar header_progressbar;
  //  private TextView header_hint_time;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;
    private boolean rotated = false;
    private int mHeaderHeight;

    public RefreshHeaderView(Context context) {
        super(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.vw_header, this);
        header_hint_text = (TextView) findViewById(R.id.header_hint_text);
        header_arrow = (ImageView) findViewById(R.id.header_arrow);
        header_progressbar = (ProgressBar) findViewById(R.id.header_progressbar);
       // header_hint_time = (TextView) findViewById(R.id.header_hint_time);
       // header_hint_time.setText(getTime());
        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.button_login_height);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onRefresh() {
        header_hint_text.setText("刷新中");
        header_progressbar.setVisibility(VISIBLE);
        header_arrow.clearAnimation();
        header_arrow.setVisibility(GONE);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            header_arrow.setVisibility(VISIBLE);
            header_progressbar.setVisibility(GONE);

            if (y > mHeaderHeight) {
                header_hint_text.setText("下拉刷新");
                if (!rotated) {
                    header_arrow.clearAnimation();
                    header_arrow.startAnimation(mRotateUpAnim);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                header_hint_text.setText("下拉刷新");
                //header_arrow.clearAnimation();
               // header_arrow.startAnimation(mRotateDownAnim);
                rotated = false;
            }
        }
    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {
        rotated = false;
        header_hint_text.setText("刷新完成");
        header_arrow.clearAnimation();
        header_arrow.setVisibility(GONE);
        //header_progressbar.setVisibility(GONE);
    }

    @Override
    public void onReset() {
        rotated = false;
        header_arrow.clearAnimation();
        header_arrow.setVisibility(GONE);
        // header_progressbar.setVisibility(GONE);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
                .format(new Date());
    }
}
