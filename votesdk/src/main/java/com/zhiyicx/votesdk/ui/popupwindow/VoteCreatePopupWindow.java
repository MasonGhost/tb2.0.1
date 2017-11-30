package com.zhiyicx.votesdk.ui.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyicx.votesdk.R;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.OnPresenterListener;
import com.zhiyicx.votesdk.manage.VoteManager;
import com.zhiyicx.votesdk.ui.view.VoteOptEdt;
import com.zhiyicx.votesdk.utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by lei on 2016/8/11.
 * 发起投票弹出框
 */
public class VoteCreatePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {
    public static final String TAG = "VoteCreatePopupWindow";
    private List<String> optionKeys = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K");
    private List<Integer> pollTimes = Arrays.asList(8, 12, 16, 20, 24);
    private int maxOptionCount = 4;//默认等于四

    private RadioGroup mPollTimeGroup;
    private LinearLayout mPollOptContainer;
    private VoteOptEdt optEdt1;
    private VoteOptEdt optEdt2;
    private TextView mPollCancelTv;
    private Button mNewOptBtn;
    private Button mSummitBtn;
    private RadioButton mTime1Rb, mTime2Rb, mTime3Rb, mTime4Rb, mTime5Rb;
    private List<RadioButton> pollTimeRbs;

    private OnPresenterListener onVoteCreateListener;
    private Activity context;
    private LayoutInflater inflater;
    private int mCid;
    private View mContentView;
    private View mLlcontaner;


    public VoteCreatePopupWindow(Context context) {
        super(context);
        init(context);
    }

    public VoteCreatePopupWindow(Context context, int cid) {
        super(context);
        this.mCid = cid;
        init(context);
    }

    public VoteCreatePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteCreatePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setPresenterListener(OnPresenterListener listener) {
        onVoteCreateListener = listener;
    }

    private void init(Context context) {
        this.context = (Activity) context;
        inflater = LayoutInflater.from(context);
        initView();
        initPop();
        initData();
    }

    /**
     * 设置投票optionkey
     *
     * @param keys
     */
    public void setOptionKeys(List<String> keys) {
        if (optionKeys != null && optionKeys.size() >= 2) {
            this.optionKeys = keys;
            maxOptionCount = optionKeys.size();
        } else
            throw new IllegalArgumentException("the List<String> option must be >=2");
    }

    /**
     * 设置最大选项数
     *
     * @param maxNum
     */
    public void setMaxOptionCount(int maxNum) {
        if (maxNum <= optionKeys.size()) {
            this.maxOptionCount = maxNum;
        } else
            throw new ArrayIndexOutOfBoundsException("the maxNum must <= optionKeys.size()");
    }

    public void setVoteTimes(List<Integer> times) {
        this.pollTimes = times;
        if (pollTimes.size() < 5) return;
        for (int i = 0; i < 5; i++) {
            pollTimeRbs.get(i).setTag(pollTimes.get(i));
            pollTimeRbs.get(i).setText(pollTimes.get(i) + "min");
        }

    }

    private void initView() {
        View v = inflater.inflate(R.layout.vote_popupwindow_create, null);
        mContentView=v;
        mLlcontaner=v.findViewById(R.id.ll_contaner);
        mPollTimeGroup = (RadioGroup) v.findViewById(R.id.rg_poll_duration);
        mPollOptContainer = (LinearLayout) v.findViewById(R.id.poll_opt_container);
        optEdt1 = (VoteOptEdt) v.findViewById(R.id.ve_poll_opt1);
        optEdt2 = (VoteOptEdt) v.findViewById(R.id.ve_poll_opt2);
        mPollCancelTv = (TextView) v.findViewById(R.id.poll_cancel);
        mNewOptBtn = (Button) v.findViewById(R.id.btn_poll_add_opt);
        mSummitBtn = (Button) v.findViewById(R.id.btn_poll_create_summit);
        mTime1Rb = (RadioButton) v.findViewById(R.id.rb_poll_duration1);
        mTime2Rb = (RadioButton) v.findViewById(R.id.rb_poll_duration2);
        mTime3Rb = (RadioButton) v.findViewById(R.id.rb_poll_duration3);
        mTime4Rb = (RadioButton) v.findViewById(R.id.rb_poll_duration4);
        mTime5Rb = (RadioButton) v.findViewById(R.id.rb_poll_duration5);
        initListener();
        pollTimeRbs = new ArrayList<>();
        pollTimeRbs.add(mTime1Rb);
        pollTimeRbs.add(mTime2Rb);
        pollTimeRbs.add(mTime3Rb);
        pollTimeRbs.add(mTime4Rb);
        pollTimeRbs.add(mTime5Rb);


        setContentView(v);
    }

    private void initListener() {
        mNewOptBtn.setOnClickListener(this);
        mPollCancelTv.setOnClickListener(this);
        mSummitBtn.setOnClickListener(this);
        mTime1Rb.setOnClickListener(this);
        mTime2Rb.setOnClickListener(this);
        mTime3Rb.setOnClickListener(this);
        mTime4Rb.setOnClickListener(this);
        mTime5Rb.setOnClickListener(this);
        mLlcontaner.setOnClickListener(this);
        optEdt1.addEditTextChangeListener(optionEditListener);
        optEdt2.addEditTextChangeListener(optionEditListener);

    }

    private void initPop() {
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        //设置弹出窗体需要软键盘，
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //避免软键盘挡住pop
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //  setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setAnimationStyle(R.style.style_anim_pop_vote);
        setOnDismissListener(this);
    }

    /**
     * 初始化页面数据
     * 时间+默认两个选项key
     */
    private void initData() {
        optEdt1.setOptSortText(optionKeys.get(0));
        optEdt2.setOptSortText(optionKeys.get(1));

        if (pollTimes.size() < 5) return;
        for (int i = 0; i < 5; i++) {
            pollTimeRbs.get(i).setTag(pollTimes.get(i));
            pollTimeRbs.get(i).setText(pollTimes.get(i) + "min");
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_poll_add_opt) {//add option
            if (mPollOptContainer.getChildCount() >= maxOptionCount) {
                UiUtils.showShortToast(context, "最多只有" + maxOptionCount + "项选项");
                return;
            }
            mPollOptContainer.addView(generateOptView(optionKeys.get(mPollOptContainer.getChildCount())));
            if (mPollOptContainer.getChildCount() == maxOptionCount){ // 发起投票时，如果已经达到4个选项，即D选项建立时，隐藏“添加选项
                mNewOptBtn.setVisibility(View.GONE);
            }

        } else if (i == R.id.btn_poll_create_summit) {

            summitPoll();

        } else if (i == R.id.poll_cancel) {
            UiUtils.hideSoftKeyboard(context, mSummitBtn);
            dismiss();

        } else if (i == R.id.rb_poll_duration1 || i == R.id.rb_poll_duration2 ||
                i == R.id.rb_poll_duration3 || i == R.id.rb_poll_duration4 ||
                i == R.id.rb_poll_duration5) {
            v.requestFocus();
        }else if(i==R.id.ll_contaner){
            dismiss();
        }
    }

    /**
     * 点击发起投票
     */
    private void summitPoll() {
        if (mPollTimeGroup.getCheckedRadioButtonId() == -1) {
            UiUtils.showShortToast(context, "请选择投票时间");
            return;
        }
        if (TextUtils.isEmpty(optEdt1.getOptionText()) || TextUtils.isEmpty(optEdt2.getOptionText())) {
            UiUtils.showShortToast(context, "请输入投票选项");
            return;
        }
        setSummitBtnEnable(false);
        //获得发起投票时间
        Map<String, String> opts = new LinkedHashMap<>();
        int pollTime = (int) ((RadioButton) mPollTimeGroup.findViewById(mPollTimeGroup.getCheckedRadioButtonId())).getTag();
        int count = mPollOptContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            VoteOptEdt optEdt = (VoteOptEdt) mPollOptContainer.getChildAt(i);
            if (!TextUtils.isEmpty(optEdt.getOptionText())) {
                opts.put(optEdt.getOptSortText(), optEdt.getOptionText());
            }
        }

        doCreateVote(null, opts, pollTime);

    }

    private void setSummitBtnEnable(boolean enable) {
        mSummitBtn.setEnabled(enable);
        if (enable) {
            mSummitBtn.setBackgroundResource(R.drawable.vote_shape_bg_progress_blue);
        } else {
            mSummitBtn.setBackgroundResource(R.drawable.vote_shape_btn_unable_gray);
        }
    }

    /**
     * 请求服务器创建投票
     *
     * @param title
     * @param options
     * @param time
     */
    private void doCreateVote(String title, Map<String, String> options, int time) {
        Log.v(TAG, "POP---" + "doCreateVote " + options.toString());
        VoteManager.newBuilder().with(context).userType(VoteManager.TYPE_PRESENTER).cid(mCid).setListener(new OnPresenterListener() {
            @Override
            public void onVoteCreateSuccess(VoteInfo voteInfo) {
                Log.v(TAG, "POP---  onVoteCreateSuccess" + String.valueOf(voteInfo));
                super.onVoteCreateSuccess(voteInfo);
                setSummitBtnEnable(true);
                UiUtils.hideSoftKeyboard(context, mSummitBtn);
                dismiss();
                if (onVoteCreateListener != null) {
                    onVoteCreateListener.onVoteCreateSuccess(voteInfo);
                }

            }

            @Override
            public void onVoteCreateFailure(String code, String message) {
                super.onVoteCreateFailure(code, message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                setSummitBtnEnable(true);
                if (onVoteCreateListener != null) {
                    onVoteCreateListener.onVoteCreateFailure(code, message);
                }
            }
        }).build().createVote(title, options, time);

    }


    /**
     * 动态添加选项
     *
     * @param sortStr 选项序列号
     * @return
     */
    private VoteOptEdt generateOptView(String sortStr) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) UiUtils.dip2px(context, 30));
        layoutParams.topMargin = (int) UiUtils.dip2px(context, 5);
        VoteOptEdt optEdt = new VoteOptEdt(context);
        optEdt.setLayoutParams(layoutParams);
        optEdt.setOptSortText(sortStr);
        optEdt.requestFocus();
        return optEdt;
    }

    @Override
    public void onDismiss() {
        setWindowAlpha(1f);
    }


    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
        setWindowAlpha(1f);
    }

    public void show(View parent) {
        clearData();
        setWindowAlpha(0.9f);
        setFocusable(true);

        showAtLocation(mContentView, Gravity.BOTTOM, 0, 0);
        update();
    }

    /**
     * 清除数据
     */
    private void clearData() {
        mPollTimeGroup.clearCheck();
        optEdt1.setOptionText("");
        optEdt2.setOptionText("");
        setSummitBtnEnable(true);
        int optCount = mPollOptContainer.getChildCount();
        if (optCount > 2) {
            for (int i = optCount - 1; i >= 2; i--) {
                mPollOptContainer.removeView(mPollOptContainer.getChildAt(i));
            }
        }
    }


    public void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha; //0.0-1.0
        context.getWindow().setAttributes(lp);

    }


    //给编辑投票选项edittext监听，前两个不为空改变背景——可以提交
    VoteOptEdt.OnTextChangeListener optionEditListener = new VoteOptEdt.OnTextChangeListener() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(optEdt1.getOptionText()) && !TextUtils.isEmpty(optEdt2.getOptionText())) {
                mSummitBtn.setBackgroundResource(R.drawable.vote_shape_btn_enable_blue);
            } else {
                mSummitBtn.setBackgroundResource(R.drawable.vote_shape_btn_unable_gray);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
