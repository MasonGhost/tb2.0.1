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
import android.widget.Button;
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
    protected Button mBtSend;
    protected EditText mEtContent;

    private int mLimitMaxSize;// 最大输入值
    private int mshowLimitSize;// 当输入值达到 mshowLimitSize 时，显示提示

    private String mLimitTipStr = "{}/";// 添加格式符号，用户ColorPhrase


    private OnSendClickListener mOnSendClickListener;

    public InputLimitView(Context context) {
        super(context);
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
        mBtSend = (Button) findViewById(R.id.bt_send);
        mEtContent = (EditText) findViewById(R.id.et_content);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.inputLimitView);
            mLimitMaxSize = array.getInteger(R.styleable.inputLimitView_limitSize, context.getResources().getInteger(R.integer.comment_input_max_size));
            mshowLimitSize = array.getInteger(R.styleable.inputLimitView_showLimitSize, context.getResources().getInteger(R.integer.show_comment_input_size));
            array.recycle();
        } else {
            mLimitMaxSize = context.getResources().getInteger(R.integer.comment_input_max_size);
            mshowLimitSize = context.getResources().getInteger(R.integer.show_comment_input_size);
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
                if (TextUtils.isEmpty(s)) {
                    mBtSend.setEnabled(false);
                } else {
                    mBtSend.setEnabled(true);
                }
                if (s.length() >= mshowLimitSize) {
                    mLimitTipStr = "<" + s.length() + ">" + "/" + mLimitMaxSize;
                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
                            .innerColor(ContextCompat.getColor(context, R.color.important_for_note))
                            .outerColor(ContextCompat.getColor(context, R.color.general_for_hint))
                            .format();
                    mTvLimitTip.setText(chars);
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
                    mOnSendClickListener.onSendClick(getInputContent());
                }
            }
        });

    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        mOnSendClickListener = onSendClickListener;
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
        void onSendClick(String text);

    }
}
