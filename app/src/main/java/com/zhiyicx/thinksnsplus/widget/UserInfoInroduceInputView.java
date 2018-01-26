package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;

import static android.support.annotation.Dimension.SP;

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
    private int mContentGrvatiy;
    private ContentChangedListener mContentChangedListener;

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
            mLimitMaxSize = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_limitSize, context.getResources().getInteger(com
                    .zhiyicx.baseproject.R.integer.comment_input_max_size));
            mshowLimitSize = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_showLimitSize, context.getResources().getInteger
                    (com.zhiyicx.baseproject.R.integer.show_comment_input_size));
            mHintContent = array.getString(com.zhiyicx.baseproject.R.styleable.inputLimitView_hintContent);
            mShowLines = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_showLines, 0);// 如果为0就不要设置maxLine了
            mContentGrvatiy = array.getInteger(com.zhiyicx.baseproject.R.styleable.inputLimitView_content_gravity, Gravity.LEFT);// 如果为0就不要设置maxLine了
            mEtContent.setGravity(mContentGrvatiy);
            if (array.getDimensionPixelSize(R.styleable.inputLimitView_content_size, 0) != 0) {
                mEtContent.setTextSize(SP, ConvertUtils.px2dp(getContext(), array.getDimension(R.styleable.inputLimitView_content_size, 0)));
            }

            array.recycle();
        } else {
            mLimitMaxSize = context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.comment_input_max_size);
            mshowLimitSize = context.getResources().getInteger(com.zhiyicx.baseproject.R.integer.show_comment_input_size);
            mHintContent = context.getResources().getString(R.string.edit_introduce);
            mShowLines = 0;
        }

        // 初始化控件属性  2*mLimitMaxSize 用于兼容 emoji
        mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2 * mLimitMaxSize)});
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
                if (mContentChangedListener != null) {
                    mContentChangedListener.contentChanged(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {  // 一下是处理适配 emoji, 让emoji 算成一个长度
                int praseContentLength = ConvertUtils.stringLenghtDealForEmoji(s);
                mLimitTipStr = "<" + praseContentLength + ">" + "/" + mLimitMaxSize;
                int emojiNum = ConvertUtils.stringEmojiCount(s);
                mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mLimitMaxSize + emojiNum + 1)});
                if (praseContentLength > mLimitMaxSize) {
                    String sholdShowContent = s.toString().substring(0, s.toString().length() - (praseContentLength - mLimitMaxSize));
                    int sholdShowEmojiNum = ConvertUtils.stringEmojiCount(sholdShowContent);
                    int offset = emojiNum - sholdShowEmojiNum;
                    if (offset > 0) {
                        ConvertUtils.emojiStrLenght(offset);
                        sholdShowContent = sholdShowContent.substring(0, sholdShowContent.length() - offset);
                    }
                    mEtContent.setText(sholdShowContent);
                    mEtContent.setSelection(sholdShowContent.length());
                }
                if (praseContentLength >= mshowLimitSize) {
                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
                            .innerColor(ContextCompat.getColor(context, com.zhiyicx.baseproject.R.color.normal_for_assist_text))
                            .outerColor(ContextCompat.getColor(context, com.zhiyicx.baseproject.R.color.normal_for_assist_text))
                            .format();
                    mTvLimitTip.setText(chars);
                    mTvLimitTip.setVisibility(VISIBLE);
                    mEtContent.setPadding(0, 0, 0, context.getResources().getDimensionPixelSize(R.dimen.spacing_big_large));
                } else {
                    mTvLimitTip.setVisibility(GONE);
                    mEtContent.setPadding(0, 0, 0, 0);
                }
            }
        });
        setBackgroundResource(R.color.white);
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

    /**
     * 因为控件使用了TextChangedListener，无法在外面再次创建一个监听
     * 获取控件，在某些时候需要用到
     */
    public void setContentChangedListener(ContentChangedListener contentChangedListener) {
        mContentChangedListener = contentChangedListener;
    }

    public interface ContentChangedListener {
        void contentChanged(CharSequence s);
    }
}
