package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author LiuChao
 * @describe 用户个人资料，简介编辑框
 * @date 2017/1/17
 * @contact email:450127106@qq.com
 */

public class UserInfoInroduceInputView extends FrameLayout {
    protected TextView mTvLimitTip;
    protected EditText mEtContent;
    private int mLimitMaxSize;// 最大输入值
    private int mshowLimitSize;// 当输入值达到 mshowLimitSize 时，显示提示
    private String mHintContent;// 编辑框的hint提示文字
    private int mShowLines;// 编辑框显示最大行数，超过改行数就滚动

    private String mLimitTipStr = "{}/";// 添加格式符号，用户ColorPhrase

    public EditText getEtContent() {
        return mEtContent;
    }

    public UserInfoInroduceInputView(Context context) {
        super(context);
        init(context, null);
    }

    public UserInfoInroduceInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UserInfoInroduceInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_userinfo_introduce_inputview, this);
        mTvLimitTip = (TextView) findViewById(com.zhiyicx.baseproject.R.id.tv_limit_tip);
        mEtContent = (EditText) findViewById(com.zhiyicx.baseproject.R.id.et_content);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    com.zhiyicx.baseproject.R.styleable.inputLimitView);
            mLimitMaxSize = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_limitSize, context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.comment_input_max_size));
            mshowLimitSize = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_showLimitSize, context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.show_comment_input_size));
            mHintContent = array.getString(com.zhiyicx.baseproject.R.styleable.inputLimitView_hintContent);
            mShowLines = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_showLines, 0);// 如果为0就不要设置maxLine了
            array.recycle();
        } else {
            mLimitMaxSize = context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.comment_input_max_size);
            mshowLimitSize = context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.show_comment_input_size);
            mHintContent = context.getResources().getString(R.string.edit_introduce);
            mShowLines = 0;
        }

        // 初始化控件属性
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLimitMaxSize)});
        mEtContent.setHint(mHintContent);
        if (mShowLines > 0) {
            mEtContent.setLines(mShowLines);
        }
        mTvLimitTip.setVisibility(GONE);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= mshowLimitSize) {
                    mLimitTipStr = "<" + s.length() + ">" + "/" + mLimitMaxSize;
                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
                            .innerColor(ContextCompat.getColor(context, com.zhiyicx.baseproject.R.color.important_for_note))
                            .outerColor(ContextCompat.getColor(context, com.zhiyicx.baseproject.R.color.general_for_hint))
                            .format();
                    mTvLimitTip.setText(chars);
                    mTvLimitTip.setVisibility(VISIBLE);
                } else {
                    mTvLimitTip.setVisibility(GONE);
                }
            }
        });
    }

    /**
     * 获取输入内容
     *
     * @return 当前输入内容，去掉前后空格
     */
    public String getInputContent() {
        return mEtContent.getText().toString().trim();
    }

    public void setText(String content) {
        mEtContent.setText(content);
    }
}
