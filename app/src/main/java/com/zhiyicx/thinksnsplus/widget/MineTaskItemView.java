package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;


/**
 * @Describe 我的页面，展示积分的自定义 item
 * @Author Jungle68
 * @Date 2018/2/27
 * @Contact master.jungle68@gmail.com
 */
public class MineTaskItemView extends FrameLayout {

    private TextView mTvPoint;
    private TextView mTvPointSymbol;
    private TextView mTvTitle;
    private TextView mTvDes;
    private TextView mTvButton;
    private ProgressBar mProgressBar;

    public MineTaskItemView(@NonNull Context context) {
        this(context, null);
    }

    public MineTaskItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MineTaskItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_mine_task_item, this);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        mTvPointSymbol = (TextView) findViewById(R.id.tv_point_symbol);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvDes = (TextView) findViewById(R.id.tv_des);
        mTvButton = (TextView) findViewById(R.id.tv_button);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_task);
    }

    /**
     * 设置积分
     */
    public void setPoint(String pontStr) {
        mTvPoint.setText(pontStr);
    }

    public void setPointSymbol(boolean isAdd) {
        mTvPointSymbol.setText(isAdd ? "+" : "-");
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setDes(String des) {
        mTvDes.setText(des);
    }

    public void setButtonText(String buttonText) {
        mTvButton.setText(buttonText);
    }

    public void setProgressVisiable(boolean visiable) {
        mProgressBar.setVisibility(visiable ? VISIBLE : GONE);
    }

    public void setTvButtonBackground(int resId) {
        mTvButton.setBackgroundResource(resId);
    }

    public void setTvButtonTextColor(int textColor) {
        mTvButton.setTextColor(textColor);
    }
    public TextView getButton(){
        return mTvButton;
    }
}
