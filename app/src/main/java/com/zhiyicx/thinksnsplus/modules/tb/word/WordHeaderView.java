package com.zhiyicx.thinksnsplus.modules.tb.word;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.WordResourceBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailHeader;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * Created by Administrator on 2018/4/3.
 */

public class WordHeaderView {
    /**
     * 当没有 title 时， 描述最多显示 2 行
     */
    private static final int DEFAULT_RESOURCE_DES_MAX_LINES = 2;
    private Context mContext;
    private TextView mTvTitle;
    public UserInfoInroduceInputView mEtWordContent;
    public LoadingButton mBtWord;
    private View mWordHeader;
    private AnimationDrawable mLoginAnimationDrawable;

    public View getWordHeader() {
        return mWordHeader;
    }

    public WordHeaderView(Context context) {
        this.mContext = context;
        mWordHeader = LayoutInflater.from(context).inflate(R.layout
                .header_fragment_word, null);
        mWordHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTvTitle = (TextView) mWordHeader.findViewById(R.id.tv_title);
        mEtWordContent = (UserInfoInroduceInputView) mWordHeader.findViewById(R.id.et_word_content);
        mBtWord = (LoadingButton) mWordHeader.findViewById(R.id.bt_word);
        initListener();
    }

    /**
     * 设置头部动态信息
     *
     * @param wordDetial
     */
    public void setWordDetial(WordResourceBean wordDetial) {
        if (!TextUtils.isEmpty(wordDetial.getTitle())) {
            mTvTitle.setText(wordDetial.getTitle());
        } else {
            mTvTitle.setText(wordDetial.getDes());
            mTvTitle.setMaxLines(DEFAULT_RESOURCE_DES_MAX_LINES);
        }
    }

    private void initListener() {
        mEtWordContent.setBackgroundResource(R.drawable.shape_rect_bg_boder_gray);
        // 内容变化监听
        RxTextView.textChanges(mEtWordContent.getEtContent())
                .map(charSequence -> charSequence.toString().replaceAll(" ", "").length() > 0)
                .subscribe(aBoolean ->
                        mBtWord.setEnabled(aBoolean)
                );
    }

    public void onDestroyView(){
        if (mLoginAnimationDrawable != null && mLoginAnimationDrawable.isRunning()) {
            mLoginAnimationDrawable.stop();
        }
    }

    public void showLoading(){
        mBtWord.handleAnimation(true);
        mBtWord.setEnabled(!TextUtils.isEmpty(mEtWordContent.getInputContent()));
        mLoginAnimationDrawable = mBtWord.getAnimationDrawable();
    }

    public void hideLoading(){
        mBtWord.handleAnimation(false);
        mBtWord.setEnabled(!TextUtils.isEmpty(mEtWordContent.getInputContent()));
    }
}
