package com.zhiyicx.common.utils;

import android.graphics.BlurMaskFilter;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Describe for TextView click spannable  and Blur effect
 * @Author Jungle68
 * @Date 2015/5/12
 * @Contact master.jungle68@gmail.com
 */
public class TextViewUtils {

    private OnSpanTextClickListener mSpanTextClickListener;
    private OnTextSpanComplete mOnTextSpanComplete;

    private TextView mTextView;//显示富文本的控件
    private String mOriMsg;//全部文本信息

    private boolean canNotRead = true;// 未付费状态

    private int mStartPos;

    private int mEndPos;

    private int mAlpha;

    private int mDynamicPosition;// 动态位置

    private int mNote;// 付费节点

    private long mAmount;// 付费金额

    private Integer mSpanTextColor;
    private boolean mCanRead;

    private int mMaxLineNums = 3;

    public static TextViewUtils newInstance(TextView textView, String oriMsg) {
        return new TextViewUtils(textView, oriMsg);
    }

    private TextViewUtils(TextView textView, String oriMsg) {
        this.mTextView = textView;
        mTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.mOriMsg = oriMsg;
    }

    public TextViewUtils spanTextColor(int spanTextColor) {
        mSpanTextColor = spanTextColor;
        return this;
    }

    public TextViewUtils onSpanTextClickListener(OnSpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
        return this;
    }

    public TextViewUtils onTextSpanComplete(OnTextSpanComplete onTextSpanComplete) {
        mOnTextSpanComplete = onTextSpanComplete;
        return this;
    }

    public TextViewUtils position(int startPos, int endPos) {
        mStartPos = startPos;
        mEndPos = endPos;
        return this;
    }

    public TextViewUtils oriMsg(String oriMsg) {
        this.mOriMsg = oriMsg;
        return this;
    }

    public TextViewUtils alpha(int alpha) {
        mAlpha = alpha;
        return this;
    }

    public TextViewUtils amount(int amount) {
        mAmount = amount;
        return this;
    }

    public TextViewUtils note(int note) {
        mNote = note;
        return this;
    }

    public TextViewUtils dataPosition(int dynamicPosition) {
        mDynamicPosition = dynamicPosition;
        return this;
    }

    public TextViewUtils maxLines(int maxlines) {
        mMaxLineNums = maxlines;
        return this;
    }

    /**
     * 设置文字
     *
     * @param canRead 是否可见
     * @return
     */
    public TextViewUtils disPlayText(boolean canRead) {
        mCanRead = canRead;
        return this;
    }

    public TextViewUtils build() {
        if (mTextView == null) {
            throw new IllegalArgumentException("textView not be null");
        }
        handleTextDisplay();
        return this;
    }

    private void handleTextDisplay() {
        mTextView.setVisibility(View.INVISIBLE);
        if (!mCanRead) {
            mTextView.setText(getSpannableString(mOriMsg));
            //mTextView.setMovementMethod(LinkMovementMethod.getInstance());// 已经交给上级分发处理
            // dealTextViewClickEvent
            mTextView.post(new Runnable() {
                @Override
                public void run() {
                    if (mTextView.getLineCount() > mMaxLineNums) {
                        int endOfLastLine = mTextView.getLayout().getLineEnd(mMaxLineNums - 1);
                        String result = mTextView.getText().subSequence(0, endOfLastLine) + "...";
                        mTextView.setText(getSpannableString(result));
                        mTextView.setVisibility(View.VISIBLE);
                        if (mOnTextSpanComplete != null) {
                            mOnTextSpanComplete.onComplete();
                        }
                    } else {
                        mTextView.setText(getSpannableString(mTextView.getText()));
                        mTextView.setVisibility(View.VISIBLE);
                        if (mOnTextSpanComplete != null) {
                            mOnTextSpanComplete.onComplete();
                        }
                    }
                }
            });
            dealTextViewClickEvent(mTextView);
        } else {
            mTextView.setText(mOriMsg);
            if (mOnTextSpanComplete != null) {
                mOnTextSpanComplete.onComplete();
            }
        }
    }

    class SpanTextClickable extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            if (mSpanTextClickListener != null) {
                mSpanTextClickListener.setSpanText(mDynamicPosition, mNote, mAmount, mTextView,
                        canNotRead);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            if (mSpanTextColor != null) {
                ds.setColor(mSpanTextColor);
            }
            ds.setAlpha(mAlpha > 0 ? mAlpha : 0xff);
            ds.setUnderlineText(false);    //去除超链接的下划线
            if (canNotRead) {
                BlurMaskFilter blurMaskFilter = new BlurMaskFilter(mTextView.getTextSize() / 3,
                        BlurMaskFilter.Blur.NORMAL);
                ds.setMaskFilter(blurMaskFilter);
            } else {
                ds.setMaskFilter(null);
            }
        }
    }

    private SpannableString getSpannableString(CharSequence temp) {
        SpannableString spanableInfo = SpannableString.valueOf(temp);
        if (mEndPos > temp.length()) {
            mEndPos = temp.length();
        }
        try {
            spanableInfo.setSpan(new SpanTextClickable(), mStartPos,
                    mEndPos, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            spanableInfo.setSpan(new SpanTextClickable(), 0, temp.length(), Spanned
                    .SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanableInfo;
    }

    // clickSpan 的点击事件分发处理
    private void dealTextViewClickEvent(TextView textView) {
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CharSequence text = ((TextView) v).getText();
                Spannable stext = Spannable.Factory.getInstance().newSpannable(text);
                TextView widget = (TextView) v;
                int action = event.getAction();

                if (action == MotionEvent.ACTION_UP ||
                        action == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= widget.getTotalPaddingLeft();
                    y -= widget.getTotalPaddingTop();

                    x += widget.getScrollX();
                    y += widget.getScrollY();

                    Layout layout = widget.getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = stext.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            link[0].onClick(widget);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static int getStringLenght(String src) {
        int count = 0;
        char[] chars = src.toCharArray();
        for (char c : chars) {
            if (c < 128) {// 英文ascii码值都是128以下
                count += 1;
            }
        }
        return src.length() - count;
    }

    /**
     * @param src
     * @return 字母个数
     */
    public static int getLetterLenght(CharSequence src) {
        int count = 0;
        char[] chars = src.toString().toCharArray();
        for (char c : chars) {
            if (c < 128) {// 英文ascii码值都是128以下完成
                count += 1;
            }
        }
        return count;
    }

    public interface OnSpanTextClickListener {
        void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead);
    }

    public interface OnTextSpanComplete {
        void onComplete();
    }
}

