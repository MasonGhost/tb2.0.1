package com.zhiyicx.votesdk.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.votesdk.R;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.entity.VoteOption;
import com.zhiyicx.votesdk.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lei on 2016/8/19.
 * 投票结束显示的视图
 */
public class VoteEndView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private RelativeLayout containers;//右边视图容器
    private LinearLayout optionItemContainers;//投票结果item容器
    private ImageView mStampIv;//胜负印章
    private ImageView mArrowRightIv;
    private TextView mTxtTv;

    private VoteInfo mVoteInfo;
    private int mFirstMaxItemPos;//第一个票数最多的选项（可能多个）
    private Map<String, VoteProgress> optionProgress;
    private List<String> maxOptkeys;//保存票数最多的选项key

    public VoteEndView(Context context) {
        super(context);
        init(context);
    }

    public VoteEndView(Context context, VoteInfo voteInfo) {
        super(context);
        init(context);
        setVoteInfo(voteInfo);
    }

    public VoteEndView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoteEndView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        optionProgress = new HashMap<>();
        maxOptkeys = new ArrayList<>();
        initView();
    }

    private void initView() {
        removeAllViews();
        View view = inflater.inflate(R.layout.vote_view_end, null);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(params);
        containers = (RelativeLayout) view.findViewById(R.id.poll_end_show_container);
        optionItemContainers = (LinearLayout) view.findViewById(R.id.poll_result_container);
        mStampIv = (ImageView) view.findViewById(R.id.poll_end_stamp_iv);
        mArrowRightIv = (ImageView) view.findViewById(R.id.poll_end_arrow_iv);
        mTxtTv = (TextView) view.findViewById(R.id.poll_end_left_tv);

        mTxtTv.setOnClickListener(this);
        mArrowRightIv.setOnClickListener(this);
        optionItemContainers.setOnClickListener(this);

        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(view);
    }


    /**
     * 设置投票信息，添加选项结果item
     *
     * @param voteInfo
     */
    public void setVoteInfo(VoteInfo voteInfo) {
        this.mVoteInfo = voteInfo;
        if (mVoteInfo != null) {
            addItemView();
            setStampLocation();
        }
    }


    /**
     * 添加选项结果item
     */
    private void addItemView() {
        optionItemContainers.removeAllViews();
        int totalCount = mVoteInfo.getBallot();
        List<VoteOption> options = mVoteInfo.getOptions();

        View item = null;
        VoteProgress progress = null;
        TextView goldCount = null;

        VoteOption option = null;
        int maxCount = 0;//记录投票最大值
        int optionSize = options.size();

        for (int i = 0; i < optionSize; i++) {
            option = options.get(i);
            //保存选项第一个最大者position 和 所有最大key
            int optionCount = option.getOption_ballot();
            if (optionCount > maxCount) {
                maxOptkeys.clear();
                maxCount = optionCount;
                mFirstMaxItemPos = i;
                maxOptkeys.add(option.getOption_key());
            } else if (optionCount == maxCount) {
                maxOptkeys.add(option.getOption_key());
            }

            optionItemContainers.addView(generateVoteReulstItem(item, progress, goldCount, option, totalCount));
        }


    }


    /**
     * 根据票数最大者设置输赢印章位置
     */
    private void setStampLocation() {
        int optionSize = mVoteInfo.getOptions().size();
        //通过得到最大票数的position个个数来计算戳的位置、样式
        if (optionSize == maxOptkeys.size()) {
            mStampIv.setImageResource(R.mipmap.vote_ico_seal_draw);
        } else {
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = UiUtils.dip2px(context, 53);
            params.topMargin = UiUtils.dip2px(context, 25 * mFirstMaxItemPos);
            mStampIv.setImageResource(R.mipmap.vote_ico_seal_win);
            mStampIv.setLayoutParams(params);
            for (String optKey : optionProgress.keySet()) {
                optionProgress.get(optKey).setProgressDrawable(getResources().getDrawable(R.drawable.vote_shape_bg_progress_gray));
                for (String maxKey : maxOptkeys) {
                    if (optKey.equals(maxKey)) {
                        optionProgress.get(optKey).setProgressDrawable(getResources().getDrawable(R.drawable.vote_shape_bg_progress_blue));
                    }
                }
            }

        }
    }


    /**
     * 生成选项结果item
     *
     * @param item
     * @param progress
     * @param goldCount
     * @param option
     * @param totalCount
     * @return
     */
    private View generateVoteReulstItem(View item, VoteProgress progress, TextView goldCount, VoteOption option, int totalCount) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = UiUtils.dip2px(context, 10);
        item = inflater.inflate(R.layout.vote_item_option_result, null);
        progress = (VoteProgress) item.findViewById(R.id.item_pollresult_progress);
        goldCount = (TextView) item.findViewById(R.id.item_pollresult_count_tv);
        showVoteOptionData(goldCount, progress, option, totalCount);
        item.setLayoutParams(params);

        optionProgress.put(option.getOption_key(), progress);
        return item;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.poll_end_left_tv) {
            mTxtTv.setVisibility(View.GONE);
            containers.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.vote_slide_in_from_left);
            containers.startAnimation(animation);

        } else if (id == R.id.poll_end_arrow_iv || id == R.id.poll_end_show_container) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.vote_slide_out_from_left);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTxtTv.setVisibility(View.VISIBLE);
                    containers.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            containers.startAnimation(animation);
        }
    }
}
