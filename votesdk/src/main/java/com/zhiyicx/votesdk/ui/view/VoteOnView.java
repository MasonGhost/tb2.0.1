package com.zhiyicx.votesdk.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.votesdk.R;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.entity.VoteOption;
import com.zhiyicx.votesdk.utils.UiUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lei on 2016/8/19.
 * 投票进行中的视图
 */
public class VoteOnView extends LinearLayout {
    private final int MESSAGE_END = 0x2;
    public static final int STATUS_END = 0;//0: 结束
    public static final int STATUS_ON = 1; // 1：投票中
    public static final int STATUS_PAUSE = 2;//2：投票暂停
    private Context context;
    private LayoutInflater mInflater;

    private LinearLayout mOptionsContainerLl;
    private TextView mTimeTv;

    private VoteInfo mVoteInfo;
    private Map<String, VoteProgress> mOptionProgresses;
    private Map<String, TextView> mOptionTvs;
    private TimeCounter mTimeCounter;


    private OnVoteOptionClickListener mOptionClickListener;
    private OnVoteEndListener mEndListener;
    private String mVoteId;


    public VoteOnView(Context context) {
        super(context);
        init(context);
    }

    public VoteOnView(Context context, VoteInfo voteInfo) {
        super(context);
        init(context);
        setVoteInfo(voteInfo);
    }

    public VoteOnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteOnView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    /**
     * 初始化view
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        mOptionProgresses = new HashMap<>();
        mOptionTvs = new HashMap<>();
        mInflater = LayoutInflater.from(context);
        initView();

    }

    private void initView() {
        removeAllViews();
        View view = View.inflate(context, R.layout.vote_view_on, null);
        mOptionsContainerLl = (LinearLayout) view.findViewById(R.id.polling_opt_container);
        mTimeTv = (TextView) view.findViewById(R.id.polling_time_tv);
        addView(view);
    }

    /**
     * 设置点击每一项item的监听
     *
     * @param listener
     */
    public void setOnVoteOptionClickListener(OnVoteOptionClickListener listener) {
        this.mOptionClickListener = listener;
    }

    public void setOnVoteEndListener(OnVoteEndListener listener) {
        this.mEndListener = listener;
    }


    //0: 结束 1：投票中 2：投票暂停
    public void setVoteInfo(VoteInfo voteInfo) {
        if (voteInfo == null) return;

        this.mVoteInfo = voteInfo;
        mTimeTv.setText("倒计时 " + UiUtils.getStandardTimeWithMinute(UiUtils.getDeadLineTime(voteInfo.getCreate_time(), voteInfo.getTime())));
        if (voteInfo.getStatus() == STATUS_ON) {//正在投票
            mTimeCounter = new TimeCounter(UiUtils.getDeadLineTime(voteInfo.getCreate_time(), voteInfo.getTime()), 1000);
            mTimeCounter.setOnTimeCounterListener(timeCounterListener);
            mTimeCounter.start();
        }
        addView();
        mVoteId = voteInfo.getVote_id();

        if (String.valueOf(mTimeTv.getText()).contains("00:00")) {
            handler.sendEmptyMessageDelayed(MESSAGE_END, 2000);
        }

    }


    /**
     * 添加选项item
     */
    private void addView() {
        mOptionsContainerLl.removeAllViews();
        int totalCount = mVoteInfo.getBallot();
        View item = null;
        VoteProgress progress = null;
        TextView goldCount = null;
        for (final VoteOption option : mVoteInfo.getOptions()) {
            item = generateItemView(item, progress, goldCount, option, totalCount);
            mOptionsContainerLl.addView(item);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOptionClickListener) {
                        mOptionClickListener.click(option, mVoteInfo);
                    }
                }
            });
        }

    }

    /**
     * 构造item项
     * @param item
     * @param progress
     * @param countTv
     * @param option
     * @param totalCount
     * @return
     */
    private View generateItemView(View item, VoteProgress progress, TextView countTv, VoteOption option, int totalCount) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = UiUtils.dip2px(context, 10);
        item = mInflater.inflate(R.layout.vote_item_option_result, null);
        progress = (VoteProgress) item.findViewById(R.id.item_pollresult_progress);
        countTv = (TextView) item.findViewById(R.id.item_pollresult_count_tv);
        showVoteOptionData(countTv, progress, option, totalCount);
        item.setLayoutParams(params);
        mOptionProgresses.put(option.getOption_key(), progress);
        mOptionTvs.put(option.getOption_key(), countTv);
        return item;
    }


    /**
     * 刷新同一投票活动的数据
     *
     * @param info
     */
    public void refreshOptionItemValue(VoteInfo info) {
        if (context == null) { //activity被销毁
            destoryTimeCounter();//结束计时器
            return;
        }
        if (info != null) {
            mVoteId = info.getVote_id();
            int status = info.getStatus();
            int totalCount = info.getBallot();
            for (VoteOption option : info.getOptions()) {
                mOptionTvs.get(option.getOption_key()).setText(String.valueOf(option.getOption_ballot()));
                if (option.getOption_ballot() == 0) {
                    mOptionProgresses.get(option.getOption_key()).setProgress(0);
                } else
                    mOptionProgresses.get(option.getOption_key()).setProgress(option.getOption_ballot() * 100 / totalCount);
            }
            if (STATUS_PAUSE == status) {//暂停
                //销毁计时器
                destoryTimeCounter();

            } else if (STATUS_END == status) {//结束
                //销毁计时器
                destoryTimeCounter();
            } else if (STATUS_ON == status) {//执行中

                if (mTimeCounter == null || info.getTime() != mVoteInfo.getTime()) {//计时器为空或者时间发生改变
                    mTimeCounter = new TimeCounter(UiUtils.getDeadLineTime(info.getCreate_time(), info.getTime()), 1000);
                    mTimeCounter.setOnTimeCounterListener(timeCounterListener);
                    mTimeCounter.start();
                }
            }
            this.mVoteInfo = info;
        }
    }

    /**
     * 给每一项选项的progress和textview赋值
     *
     * @param countTv
     * @param progress
     * @param option
     * @param totalCount
     */
    private void showVoteOptionData(TextView countTv, VoteProgress progress, VoteOption option, int totalCount) {
        countTv.setText(String.valueOf(option.getOption_ballot()));
        progress.setProgressDes(option.getOption_key() + ":" + option.getOption_value());
        if (option.getOption_ballot() == 0) {
            progress.setProgress(0);
        } else
            progress.setProgress(option.getOption_ballot() * 100 / totalCount);

    }

    private TimeCounter.OnTimeCounterListener timeCounterListener = new TimeCounter.OnTimeCounterListener() {
        @Override
        public void onTick(long millisUntilFinished) {
            if (mTimeTv != null) {
                mTimeTv.setText("倒计时 " + UiUtils.getStandardTimeWithMinute(millisUntilFinished));
            }
        }

        @Override
        public void onFinish() {
            if (mTimeTv != null)
                mTimeTv.setText("倒计时 " + "00:00");
            handler.sendEmptyMessageDelayed(MESSAGE_END, 1000);

        }
    };


    public interface OnVoteOptionClickListener {
        void click(VoteOption option, VoteInfo voteInfo);
    }

    public interface OnVoteEndListener {
        void voteEnd();
    }


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if (msg.what == MESSAGE_END) {
                if (mEndListener != null) {
                    if (String.valueOf(mTimeTv.getText()).contains("00:00"))
                        mEndListener.voteEnd();
                }
            }
        }
    };


    //销毁计时器
    public void destoryTimeCounter() {

        if (mTimeCounter != null) {
            mTimeCounter.cancel();
            mTimeCounter = null;
        }
    }

    public String getVoteId() {
        return mVoteId;
    }


}
