package com.zhiyicx.votesdk.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.votesdk.R;


/**
 * Created by lei on 2016/8/10.
 * 发起投票创建选项
 */
public class VoteOptEdt extends RelativeLayout {
    private TextView mTv;//选项序列
    private EditText mEt;//选项描述（编辑选项答案）

    private OnTextChangeListener textChangeListener;

    public VoteOptEdt(Context context) {
        super(context);
        initView(context);
    }

    public VoteOptEdt(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VoteOptEdt(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.vote_pop_item_option_edit, this, true);
        mTv = (TextView) v.findViewById(R.id.item_poll_sort_tv);
        mEt = (EditText) v.findViewById(R.id.item_poll_opt_et);

        mEt.addTextChangedListener(editTextWatcher);
    }

    public void setOptSortText(String optSort) {
        mTv.setText(optSort);
    }

    public void addEditTextChangeListener(OnTextChangeListener listener){
        this.textChangeListener = listener;
    }


    public void setOptionText(String optStr) {
        mEt.setText(optStr);
    }

    public void setOptionHint(String optStr) {
        mEt.setHint(optStr);
    }

    public String getOptionText() {
        return String.valueOf(mEt.getText());
    }

    public String getOptSortText() {
        return String.valueOf(mTv.getText());
    }

    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (textChangeListener!=null){
                textChangeListener.beforeTextChanged(s,start,count,after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (textChangeListener!=null){
                textChangeListener.onTextChanged(s,start,before,count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (textChangeListener!=null){
                textChangeListener.afterTextChanged(s);
            }
        }
    };

    public  interface OnTextChangeListener{
        void beforeTextChanged(CharSequence s, int start, int count, int after);
        void onTextChanged(CharSequence s, int start, int before, int count);
        void afterTextChanged(Editable s);
    }
}
