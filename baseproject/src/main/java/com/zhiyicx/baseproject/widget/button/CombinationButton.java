package com.zhiyicx.baseproject.widget.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @author LiuChao
 * @describe 个人中心的组合控件，图片-文字-图片
 * @date 2017/1/7
 * @contactemail:450127106@qq.com
 */
public class CombinationButton extends FrameLayout {
    ImageView mCombinedButtonImgLeft;
    ImageView mCombinedButtonImgRight;

    TextView mCombinedButtonLeftText;
    TextView mCombinedButtonRightText;
    View mVLine;

    public CombinationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_combination_button, this);
        mCombinedButtonImgLeft = (ImageView) findViewById(R.id.iv_left_img);
        mCombinedButtonImgRight = (ImageView) findViewById(R.id.iv_right_img);
        mCombinedButtonLeftText = (TextView) findViewById(R.id.tv_left_text);
        mCombinedButtonRightText = (TextView) findViewById(R.id.tv_right_text);
        mVLine = findViewById(R.id.v_line);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.combinationBtn);
        Drawable leftImage = array.getDrawable(R.styleable.combinationBtn_leftImage);
        Drawable rightImage = array.getDrawable(R.styleable.combinationBtn_rightImage);
        String leftText = array.getString(R.styleable.combinationBtn_leftText);
        String rightText = array.getString(R.styleable.combinationBtn_rightText);
        int leftTextColor = array.getColor(R.styleable.combinationBtn_leftTextColor, -1);
        int rightTextColor = array.getColor(R.styleable.combinationBtn_rightTextColor, -1);
        boolean showLine = array.getBoolean(R.styleable.combinationBtn_showLine, true);
        int dividerLeftMargin = array.getDimensionPixelSize(R.styleable.combinationBtn_dividerLeftMargin, 0);
        int dividerRightMargin = array.getDimensionPixelSize(R.styleable.combinationBtn_dividerRightMargin, 0);
        array.recycle();
        if (!TextUtils.isEmpty(leftText)) {
            mCombinedButtonLeftText.setText(leftText);
        }
        if (leftTextColor != -1) {
            mCombinedButtonLeftText.setTextColor(leftTextColor);
        }
        if (rightTextColor != -1) {
            mCombinedButtonRightText.setTextColor(rightTextColor);
        }
        if (!TextUtils.isEmpty(rightText)) {
            mCombinedButtonRightText.setText(rightText);
        }
        mCombinedButtonImgLeft.setImageDrawable(leftImage);
        mCombinedButtonImgRight.setImageDrawable(rightImage);
        if (showLine) {
            mVLine.setVisibility(VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVLine.getLayoutParams();
            layoutParams.setMargins(dividerLeftMargin, 0, dividerRightMargin, 0);
        } else {
            mVLine.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置左边文字内容
     */
    public void setLeftText(String leftText) {
        mCombinedButtonLeftText.setText(leftText);
    }

    /**
     * 设置右边文字内容
     */
    public void setRightText(String rightText) {
        mCombinedButtonRightText.setText(rightText);
    }
}
