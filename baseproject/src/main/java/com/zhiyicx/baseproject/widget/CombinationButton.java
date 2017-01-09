package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

/**
 * @author LiuChao
 * @describe 个人中心的组合控件，图片-文字-图片
 * @date 2017/1/7
 * @contact email:450127106@qq.com
 */
public class CombinationButton extends LinearLayout {
    ImageView combined_button_img_left, combined_button_img_right;
    TextView combined_button_text;

    public CombinationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_combination_button, this);
        combined_button_img_left = (ImageView) findViewById(R.id.iv_left_img);
        combined_button_img_right = (ImageView) findViewById(R.id.iv_right_img);
        combined_button_text = (TextView) findViewById(R.id.tv_left_text);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.combinationBtn);
        Drawable left = array.getDrawable(R.styleable.combinationBtn_left);
        Drawable right = array.getDrawable(R.styleable.combinationBtn_right);
        String text = array.getString(R.styleable.combinationBtn_text);
        array.recycle();
        combined_button_text.setText(text);
        combined_button_img_left.setImageDrawable(left);
        combined_button_img_right.setImageDrawable(right);
    }

    /**
     * 设置文字内容
     */
    public void setText(String text) {
        combined_button_text.setText(text);
    }

}
