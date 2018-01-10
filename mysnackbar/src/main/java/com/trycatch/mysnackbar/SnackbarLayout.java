package com.trycatch.mysnackbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/1/10
 * @Contact master.jungle68@gmail.com
 */

public class SnackbarLayout extends LinearLayout {
    private TextView mMessageView;
    private TextView mActionView;

    private int mMaxWidth;
    private int mMaxInlineActionWidth;


    private TSnackbar.OnLayoutChangeListener mOnLayoutChangeListener;
    private TSnackbar.OnAttachStateChangeListener mOnAttachStateChangeListener;

    public SnackbarLayout(Context context) {
        this(context, null);
    }

    public SnackbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
        mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
        mMaxInlineActionWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
        if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
            ViewCompat.setElevation(this, a.getDimensionPixelSize(
                    R.styleable.SnackbarLayout_elevation, 5));
        }
        a.recycle();
        setClickable(true);

        // Now inflate our content. We need to do this manually rather than using an <include>
        // in the layout since older versions of the Android do not inflate includes with
        // the correct Context.
        LayoutInflater.from(context).inflate(R.layout.view_tsnackbar_layout_include, this);
        ViewCompat.setAccessibilityLiveRegion(this,
                ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
        ViewCompat.setImportantForAccessibility(this,
                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
//            if (this.isHardwareAccelerated()) {
//                findViewById(R.id.v_custom_shadow).setVisibility(GONE);
//            }else {
//                findViewById(R.id.v_custom_shadow).setVisibility(VISIBLE);
//            }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMessageView = (TextView) findViewById(R.id.snackbar_text);
        mActionView = (TextView) findViewById(R.id.snackbar_action);
    }

    TextView getMessageView() {
        return mMessageView;
    }

    TextView getActionView() {
        return mActionView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
          /*  if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            final int multiLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical_2lines);
            final int singleLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical);
            final boolean isMultiLine = mMessageView.getLayout().getLineCount() > 1;
            boolean remeasure = false;
            if (isMultiLine && mMaxInlineActionWidth > 0
                    && mActionView.getMeasuredWidth() > mMaxInlineActionWidth) {
                if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                        multiLineVPadding - singleLineVPadding)) {
                    remeasure = true;
                }
            } else {
                final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
                if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true;
                }
            }
            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }*/
    }

    void animateChildrenIn(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 0f);
        ViewCompat.animate(mMessageView).alpha(1f).setDuration(duration)
                .setStartDelay(delay).start();
        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 0f);
            ViewCompat.animate(mActionView).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }

    void animateChildrenOut(int delay, int duration) {
        ViewCompat.setAlpha(mMessageView, 1f);
        ViewCompat.animate(mMessageView).alpha(0f).setDuration(duration)
                .setStartDelay(delay).start();
        if (mActionView.getVisibility() == VISIBLE) {
            ViewCompat.setAlpha(mActionView, 1f);
            ViewCompat.animate(mActionView).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && mOnLayoutChangeListener != null) {
            mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener.onViewAttachedToWindow(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnAttachStateChangeListener != null) {
            mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
        }
    }

    void setOnLayoutChangeListener(TSnackbar.OnLayoutChangeListener onLayoutChangeListener) {
        mOnLayoutChangeListener = onLayoutChangeListener;
    }

    void setOnAttachStateChangeListener(TSnackbar.OnAttachStateChangeListener listener) {
        mOnAttachStateChangeListener = listener;
    }

    private boolean updateViewsWithinLayout(final int orientation,
                                            final int messagePadTop, final int messagePadBottom) {
        boolean changed = false;
        if (orientation != getOrientation()) {
            setOrientation(orientation);
            changed = true;
        }
        if (mMessageView.getPaddingTop() != messagePadTop
                || mMessageView.getPaddingBottom() != messagePadBottom) {
            updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
            changed = true;
        }
        return changed;
    }

    private void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
        if (ViewCompat.isPaddingRelative(view)) {
            ViewCompat.setPaddingRelative(view,
                    ViewCompat.getPaddingStart(view), topPadding,
                    ViewCompat.getPaddingEnd(view), bottomPadding);
        } else {
            view.setPadding(view.getPaddingLeft(), topPadding,
                    view.getPaddingRight(), bottomPadding);
        }
    }
}