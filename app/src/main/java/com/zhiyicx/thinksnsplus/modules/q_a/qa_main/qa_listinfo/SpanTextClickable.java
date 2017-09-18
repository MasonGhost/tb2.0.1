package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.graphics.BlurMaskFilter;
import android.text.Layout;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;

public class SpanTextClickable extends ClickableSpan {

    private long answer_id;

    private float radio;

    private int position;

    private Integer mSpanTextColor = SkinUtils.getColor(R
            .color.normal_for_assist_text);

    private SpanTextClickListener mSpanTextClickListener;

    public SpanTextClickable(long answer_id, float radio,int position) {
        this.radio = radio;
        this.answer_id = answer_id;
        this.position = position;
    }

    @Override
    public void onClick(View widget) {
        if (mSpanTextClickListener != null) {
            mSpanTextClickListener.onSpanClick(answer_id,position);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (mSpanTextColor != null) {
            ds.setColor(mSpanTextColor);
        }
        ds.setUnderlineText(false);    //去除超链接的下划线
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radio,
                BlurMaskFilter.Blur.NORMAL);
        ds.setMaskFilter(blurMaskFilter);

    }

    public interface SpanTextClickListener {
        void onSpanClick(long answer_id,int position);
    }

    public static void dealTextViewClickEvent(TextView textView) {
        textView.setOnTouchListener((v, event) -> {
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
        });
    }

    public void setSpanTextClickListener(SpanTextClickListener spanTextClickListener) {
        mSpanTextClickListener = spanTextClickListener;
    }
}


