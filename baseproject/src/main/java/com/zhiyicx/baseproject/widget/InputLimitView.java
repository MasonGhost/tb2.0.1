package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.utils.ColorPhrase;

/**
 * @Describe 限制输入控件 view
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */

public class InputLimitView extends FrameLayout {

    protected TextView mTvLimitTip;
    protected TextView mBtSend;


    protected EditText mEtContent;
    protected EditText mEtEmpty;

    private int mLimitMaxSize;// 最大输入值
    private int mShowLimitSize;// 当输入值达到 mShowLimitSize 时，显示提示

    private String mLimitTipStr = "{}/";// 添加格式符号，用户ColorPhrase


    private OnSendClickListener mOnSendClickListener;

    public InputLimitView(Context context) {
        super(context);
        init(context, null);
    }

    public InputLimitView(Context context, int limitMaxSize, int showLimitSize) {
        super(context);
        mLimitMaxSize = limitMaxSize;
        mShowLimitSize = showLimitSize;
        init(context, null);
    }

    public InputLimitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputLimitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_input_limit_viewgroup, this);
        mTvLimitTip = (TextView) findViewById(R.id.tv_limit_tip);
        mBtSend = (TextView) findViewById(R.id.bt_send);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtEmpty = (EditText) findViewById(R.id.et_empty);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.inputLimitView);
            mLimitMaxSize = array.getInteger(R.styleable.inputLimitView_limitSize, context.getResources().getInteger(R.integer.comment_input_max_size));
            mShowLimitSize = array.getInteger(R.styleable.inputLimitView_showLimitSize, context.getResources().getInteger(R.integer.show_comment_input_size));
            array.recycle();
        }
        if (mLimitMaxSize == 0) {
            mLimitMaxSize = context.getResources().getInteger(R.integer.comment_input_max_size);
        }
        if (mLimitMaxSize == 0) {
            context.getResources().getInteger(R.integer.show_comment_input_size);
        }
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLimitMaxSize)});

        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mBtSend.setEnabled(false);
                } else {
                    mBtSend.setEnabled(true);
                }
                if (s.length() >= mShowLimitSize) {
//                    mLimitTipStr = "<" + s.length() + ">" + "/" + mLimitMaxSize;
//                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
//                            .innerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .format();
                    mLimitTipStr = s.length()  + "/" + mLimitMaxSize;
//                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
//                            .innerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .format();
                    mTvLimitTip.setText(mLimitTipStr);
                    mTvLimitTip.setVisibility(VISIBLE);
                } else {
                    mTvLimitTip.setVisibility(GONE);
                }
            }
        });

        mBtSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSendClickListener != null) {
                    mOnSendClickListener.onSendClick(v,getInputContent());
                    mEtContent.setText("");
                }
            }
        });

    }

    public EditText getEtContent() {
        return mEtContent;
    }

    /**
     * 设置发送按钮点击监听
     *
     * @param onSendClickListener
     */
    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        mOnSendClickListener = onSendClickListener;
    }

    /**
     * 设置发送按钮是否显示
     *
     * @param isVisiable true 显示
     */
    public void setSendButtonVisiable(boolean isVisiable) {
        if (isVisiable) {
            mBtSend.setVisibility(VISIBLE);
        } else {
            mBtSend.setVisibility(GONE);
        }
    }

    /**
     * 设置 hint
     *
     * @param hintStr
     */
    public void setEtContentHint(String hintStr) {
        mEtContent.setHint(hintStr);
    }

    /**
     * 清除焦点
     */
    public void clearFocus() {
        mEtContent.clearFocus();
    }

    public void getFocus() {
        mEtEmpty.clearFocus();
        mEtContent.requestFocus();
    }

    /**
     * 设置输入提示
     *
     * @param hint
     */
    public void setTvLimitHint(@StringRes int hint) {
        mTvLimitTip.setHint(hint);
    }

    /**
     * 获取输入内容
     *
     * @return 当前输入内容，去掉前后空格
     */
    public String getInputContent() {
        return mEtContent.getText().toString().trim();
    }

    public interface OnSendClickListener {
        void onSendClick(View v,String text);

    }
}
