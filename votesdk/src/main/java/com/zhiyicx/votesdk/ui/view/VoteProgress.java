package com.zhiyicx.votesdk.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.zhiyicx.votesdk.utils.UiUtils;


/**
 * Created by lei on 2016/8/10.
 * 显示投票进度的进度条
 */
public class VoteProgress extends ProgressBar {
    private Paint mPaint;
    private String descripion;

    public VoteProgress(Context context) {
        super(context);
        initText();
    }

    public VoteProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initText();
    }

    public VoteProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initText();

    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(descripion)) {
            Rect rect = new Rect();
            this.mPaint.getTextBounds(this.descripion, 0, this.descripion.length(), rect);
            int y = (getHeight() / 2) - rect.centerY();
            canvas.drawText(this.descripion, UiUtils.dip2px(getContext(), 5), y, this.mPaint);
        }

    }

    // 初始化，画笔
    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);// 设置抗锯齿;;;;
        this.mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(UiUtils.dip2px(getContext(), 10));
    }

    public void setProgressDes(String descripion) {
        this.descripion = descripion;
    }
}
