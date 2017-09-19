package com.zhiyicx.votesdk.ui.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.votesdk.R;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.entity.VoteOption;
import com.zhiyicx.votesdk.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by lei on 2016/8/11.
 */
public class VotePollPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    private List<String> optionkeys = Arrays.asList("A", "B", "C", "D", "E", "F", "Ë", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O");
    private int optionCount = 0;

    private TextView mCancelTv;
    private LinearLayout optContainer;

    private String selectOptionKey;//选择项key
    private String vote_id;//投票活动id

    private OnVoteOptionListener onVoteOptionListener;
    private Activity context;
    private LayoutInflater inflater;

    public void setOnVoteListener(OnVoteOptionListener listener) {
        onVoteOptionListener = listener;
    }

    public VotePollPopupWindow(Context context) {
        super(context);
        initView(context);
    }

    public VotePollPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VotePollPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 设置Optionskey
     *
     * @param optionkeys
     */
    public void setOptionKeys(List<String> optionkeys) {
        this.optionkeys = optionkeys;
        addView(optContainer);
    }

    public void setVoteInfo(VoteInfo info) {
        if (info != null) {
            vote_id = info.getVote_id();
            this.optionkeys = parseModel(info);
            addView(optContainer);
        }
    }

    /**
     * @param vote_id
     */
    public void setVoteId(String vote_id) {
        this.vote_id = vote_id;
        addView(optContainer);
    }

    /**
     * 设置选项个数
     * 使用默认optionKeys
     *
     * @param optionCount
     */
    public void setCountByDefaultKeys(int optionCount) {
        if (optionCount > optionkeys.size())
            throw new IllegalArgumentException("the optionCount must be <= optionKeys.size()");
        this.optionCount = optionCount;
        addView(optContainer);
    }

    private void initView(Context context) {
        this.context = (Activity) context;
        inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.vote_popupwindow_poll, null, false);
        mCancelTv = (TextView) v.findViewById(R.id.do_poll_cancel);
        optContainer = (LinearLayout) v.findViewById(R.id.do_poll_opt_container);
        mCancelTv.setOnClickListener(this);
        initPop(v);
    }

    /**
     * 动态添加选项内容
     *
     * @param
     */
    private void addView(LinearLayout container) {
        container.removeAllViews();
        TextView view = null;
        if (optionCount != 0) {
            for (int i = 0; i < optionCount; i++) {
                container.addView(generateOptionView(view, optionkeys.get(i)));
            }
        } else if (null != optionkeys && !optionkeys.isEmpty() && optionkeys.size() < 5) {
            for (String optionkey : optionkeys) {
                container.addView(generateOptionView(view, optionkey));
            }
        }
    }


    /**
     * 动态添加选项
     *
     * @param txt
     * @param optionKey
     * @return
     */
    private View generateOptionView(TextView txt, String optionKey) {
        txt = new TextView(context);
        txt.setClickable(true);
        txt.setBackgroundResource(R.drawable.vote_selector_pop_bg_time);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        txt.setTextColor(ContextCompat.getColor(context, R.color.vote_pop_text_ffffff));
        txt.setGravity(Gravity.CENTER);
        txt.setTag(optionKey);
        txt.setText("投" + optionKey);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.dip2px(context, 30));
        params.topMargin = UiUtils.dip2px(context, 10);
        txt.setLayoutParams(params);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onVoteOptionListener != null) {
                    selectOptionKey = String.valueOf(v.getTag());
                    onVoteOptionListener.vote(selectOptionKey);
                }
            }
        });
        return txt;
    }

    public String getSelectOptionKey() {
        if (TextUtils.isEmpty(selectOptionKey))
            throw new NullPointerException("you have to select a option firstly");

        return selectOptionKey;
    }

    public String getVoteId() {
        if (TextUtils.isEmpty(vote_id))
            throw new NullPointerException("you have to set the vote_id");

        return vote_id;
    }

    private void initPop(View v) {
        setContentView(v);
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        //设置弹出窗体需要软键盘，
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //避免软键盘挡住pop
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setAnimationStyle(R.style.style_anim_pop_vote);
        setOnDismissListener(this);
    }


    @Override
    public void onDismiss() {
        setWindowAlpha(1f);
    }


    public void show(View parent) {
        setWindowAlpha(0.9f);
        setFocusable(true);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();
    }

    public void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        context.getWindow().setAttributes(lp);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.do_poll_cancel) {
            dismiss();

        }
    }


    public interface OnVoteOptionListener {
        void vote(String optionKey);
    }


    private List<String> parseModel(VoteInfo info) {
        List<String> optionKeys = new ArrayList<>();
        if (info.getOptions() == null || info.getOptions().size() == 0) return optionKeys;
        for (VoteOption option : info.getOptions()) {
            optionKeys.add(option.getOption_key());
        }
        return optionKeys;
    }
}
