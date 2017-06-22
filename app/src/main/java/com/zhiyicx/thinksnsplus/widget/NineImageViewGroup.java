package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/14/10:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class NineImageViewGroup extends ViewGroup {

    private int grap = 5;

    private int maxWidth, minWidth;

    private int childCount;

    private int position;

    private FilterImageView mView;

    private int[] ids = new int[]{R.id.siv_0_, R.id.siv_1_, R.id.siv_2_, R.id.siv_3_, R.id.siv_4_,
            R.id.siv_5_, R.id.siv_6_, R.id.siv_7_, R.id.siv_8_};

    private OnNineImageClickListener mOnNineImageClickListener;

    public NineImageViewGroup(Context context) {
        super(context);
    }

    public NineImageViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NineImageViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, measureHeight(heightMeasureSpec, childCount));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(childCount);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private int measureHeight(int heightMeasureSpec, int childCount) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = getMeasureHeight(childCount);
        return MeasureSpec.makeMeasureSpec(size, mode);
    }

    private int getMeasureHeight(int childCount) {
        int result = 0;
        switch (childCount) {
            case 1:
                result = minWidth * 4 / 3;
                break;
            case 2:
            case 3:
                result = minWidth;
                break;
            case 4:
                result = minWidth * 2 + grap;
                break;
            case 5:
                result = maxWidth + grap + getHorizontalSpace() / 2;
                break;
            case 7:
                result = maxWidth * 2 + grap;
                break;
            case 8:
                result = maxWidth + (minWidth + grap) * 2;
                break;
            case 6:
            case 9:
                result = minWidth * 3 + grap * 2;
                break;
            default:
                break;

        }
        return result;
    }

    private void layoutChild(int childCount) {
        switch (childCount) {
            case 1:
                layout1Child();
                break;
            case 2:
            case 3:
                layout2_3Child();
                break;
            case 4:
                layout4Child();
                break;
            case 5:
                layout5Child();
                break;
            case 6:
                layout6Child();
                break;
            case 7:
                layout7Child();
                break;
            case 8:
                layout8Child();
                break;
            case 9:
                layout9Child();
                break;
            default:
                break;

        }
    }

    private void layout1Child() {
        View view = getChildAt(0);
        view.layout(getPaddingLeft(), getPaddingTop(), minWidth + getPaddingLeft(), minWidth +
                getPaddingTop());
    }

    private void layout2_3Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(offset1X, offset1Y, minWidth + offset1X, minWidth + offset1Y);
            offset1X = (minWidth + grap) + offset1X;
        }
    }

    private void layout4Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int offset2X = 0;
        int offset2Y = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 2) {
                view.layout(offset1X, offset1Y, minWidth + offset1X, minWidth + offset1Y);
                offset1X = (minWidth + grap) + offset1X;
                offset2Y = minWidth + grap + offset1Y;
            } else if (i < 4) {
                view.layout(offset2X, offset2Y, minWidth + offset2X, minWidth + offset2Y);
                offset2X += minWidth + grap;
            }
        }
    }

    private void layout5Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int offset2X = getPaddingLeft();
        int offset2Y = getPaddingTop();

        int midWidth = (getHorizontalSpace() - grap) / 2;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 1) {
                view.layout(offset1X, offset1Y, maxWidth + offset1X, maxWidth + offset1Y);
                offset1X = (maxWidth + grap) + offset1X;
                offset1Y = maxWidth + grap + offset1Y;
            } else if (i < 3) {
                view.layout(offset1X, offset2Y, minWidth + offset1X, minWidth + offset2Y);
                offset2Y = offset2Y + minWidth + grap;
            } else if (i < 5) {
                view.layout(offset2X, offset1Y, midWidth + offset2X, midWidth + offset1Y);
                offset2X = offset2X + midWidth + grap;
            }
        }
    }

    private void layout6Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int offset2X = getPaddingLeft();
        int offset2Y = getPaddingTop();

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 1) {
                view.layout(offset1X, offset1Y, maxWidth + offset1X, maxWidth + offset1Y);
                offset1X = (maxWidth + grap) + offset1X;
                offset1Y = maxWidth + grap + offset1Y;
            } else if (i < 3) {
                view.layout(offset1X, offset2Y, minWidth + offset1X, minWidth + offset2Y);
                offset2Y = offset2Y + minWidth + grap;
            } else if (i < 6) {
                view.layout(offset2X, offset1Y, minWidth + offset2X, minWidth + offset1Y);
                offset2X = offset2X + minWidth + grap;
            }
        }
    }

    private void layout7Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int offset2X = getPaddingLeft();
        int offset2Y = getPaddingTop();

        int offset3X = getPaddingLeft();
        int offset3Y = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 1) {
                view.layout(offset1X, offset1Y, maxWidth + offset1X, maxWidth + offset1Y);
                offset1X = maxWidth + grap;
                offset2X = maxWidth + grap;
            } else if (i < 3) {
                view.layout(offset1X, offset1Y, minWidth + offset1X, minWidth + offset1Y);
                offset1X = offset1X + minWidth + grap;
                offset2Y = offset3X + minWidth + grap;
            } else if (i < 4) {
                view.layout(offset2X, offset2Y, maxWidth + offset2X, maxWidth + offset2Y);
                offset3Y = offset1Y + maxWidth + grap;
            } else if (i < 5) {
                view.layout(offset3X, offset3Y, maxWidth + offset3X, maxWidth + offset3Y);
                offset1Y = offset2Y + maxWidth + grap;
            } else if (i < 7) {
                view.layout(offset2X, offset1Y, minWidth + offset2X, minWidth + offset1Y);
                offset2X = offset2X + minWidth + grap;
            }
        }
    }

    private void layout8Child() {
        int offset3X = 0;

        int offset2X = 0;
        int offset2Y = 0;

        int offset1X = offset2X = offset3X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int totalW = getHorizontalSpace();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 3) {
                int width = minWidth = (totalW - 2 * grap) / 3;
                view.layout(offset1X, getPaddingTop(), width + offset1X, width + getPaddingTop());
                offset1X = (width + grap) + offset1X;
                offset1Y = width + grap + getPaddingTop();
            } else if (i < 5) {
                int width = maxWidth = (totalW - grap) / 2;
                view.layout(offset2X, offset1Y, width + offset2X, width + offset1Y);
                offset2X += width + grap;
                offset2Y = offset1Y + width + grap;
            } else if (i < 8) {
                int width = (totalW - 2 * grap) / 3;
                view.layout(offset3X, offset2Y, width + offset3X, width + offset2Y);
                offset3X += width + grap;
            }
        }
    }

    private void layout9Child() {
        int offset1X = getPaddingLeft();
        int offset1Y = getPaddingTop();

        int offset2X = getPaddingLeft();
        int offset2Y = getPaddingTop();

        int offset3X = getPaddingLeft();
        int offset3Y = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (i < 3) {
                view.layout(offset1X, offset1Y, minWidth + offset1X, minWidth + offset1Y);
                offset1X = minWidth + grap + offset1X;
                offset2Y = offset1Y + minWidth + grap;
            } else if (i < 6) {
                view.layout(offset2X, offset2Y, minWidth + offset2X, minWidth + offset2Y);
                offset2X = offset2X + minWidth + grap;
                offset3Y = offset2Y + grap + minWidth;
            } else if (i < 9) {
                view.layout(offset3X, offset3Y, minWidth + offset3X, minWidth + offset3Y);
                offset3X = offset3X + minWidth + grap;
            }
        }
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    private int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getChildMeasuredWidth(View view) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                view.getLayoutParams();
        return view.getMeasuredWidth() + params.leftMargin
                + params.rightMargin;
    }

    private int getChildMeasuredHeight(View view) {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                view.getLayoutParams();
        return view.getMeasuredHeight() + params.topMargin
                + params.bottomMargin;
    }

    public int getGrap() {
        return grap;
    }

    public void setGrap(int grap) {
        this.grap = grap;
    }

    @Override
    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(final List<DynamicDetailBeanV2.ImagesBean> images) {
        this.childCount = images.size();
        removeAllViews();
        for (int i = 0; i < childCount; i++) {
            FilterImageView imageView = new SquareImageView(getContext());
            if (childCount == 1) {
                imageView = new FilterImageView(getContext());
            }
            mView = imageView;
            imageView.setId(ids[i]);
            RxView.clicks(imageView)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                    .filter(new Func1<Void, Boolean>() {
                        @Override
                        public Boolean call(Void aVoid) {
                            return mOnNineImageClickListener != null;
                        }
                    })
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            mOnNineImageClickListener.onImageClick(mView, images, position);
                        }
                    });

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.mipmap.icon_256);
            addView(imageView, i);
            position = i;
        }
        int totalW = getHorizontalSpace();

        switch (childCount) {
            case 1:
                maxWidth = minWidth = totalW;
                break;
            case 3:
                maxWidth = minWidth = (totalW - 2 * grap) / 3;
                break;
            case 2:
            case 4:
                maxWidth = minWidth = (totalW - grap) / 2;
                break;
            case 5:
            case 6:
                minWidth = (totalW - 2 * grap) / 3;
                maxWidth = minWidth * 2 + grap;
                break;
            case 7:
                maxWidth = (totalW - grap) / 2;
                minWidth = (maxWidth - grap) / 2;
                break;
            case 8:
                minWidth = (totalW - 2 * grap) / 3;
                maxWidth = (totalW - grap) / 2;
                break;
            case 9:
                maxWidth = minWidth = (totalW - 2 * grap) / 3;
                break;
            default:
                break;
        }

    }

    public interface OnNineImageClickListener {
        void onImageClick(FilterImageView imageView, List<DynamicDetailBeanV2.ImagesBean>
                imageBeanList, int position);
    }
}
