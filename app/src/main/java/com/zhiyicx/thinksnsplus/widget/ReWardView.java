package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.list.RewardListFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class ReWardView extends FrameLayout {
    private static final int DEFAULT_SHOW_IMAGE_SZIE = 10;
    protected TextView mBtRewards;
    protected TextView mTvRewardsTip;
    protected RecyclerView mRVUsers;
    protected ImageView mIvRightArrow;

    private OnRewardsClickListener mOnRewardsClickListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommonAdapter mCommonAdapter;
    private List<RewardsListBean> mListData = new ArrayList<>();
    private long mSourceId;
    private RewardType mRewardType = RewardType.INFO;


    public ReWardView(@NonNull Context context) {
        this(context, null);
    }

    public ReWardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReWardView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_rewards, this);

        mBtRewards = (TextView) findViewById(R.id.bt_rewards);
        mTvRewardsTip = (TextView) findViewById(R.id.tv_rewards_tip);
        mRVUsers = (RecyclerView) findViewById(R.id.rv_users);
        mIvRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        initRvUsers();
        initListener();
    }

    private void initRvUsers() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRVUsers.setLayoutManager(mLayoutManager);
        mRVUsers.setHasFixedSize(true);
        mRVUsers.addItemDecoration(new LinearDecoration(0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small), 0));
        mCommonAdapter = new CommonAdapter<RewardsListBean>(getContext(), R.layout.item_rewards_user_image, mListData) {
            @Override
            protected void convert(ViewHolder holder, RewardsListBean rewardsListBean, int position) {

                ImageUtils.loadUserHead(rewardsListBean.getUser(), (ImageView) holder.getView(R.id.iv_head), false);

                RxView.clicks(holder.getView(R.id.iv_head))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
//                        .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(getContext(), rewardsListBean.getUser()));
                        .subscribe(aVoid -> RewardListFragment.startRewardActivity(getContext(), mRewardType, mSourceId, mListData));

            }
        };
        mRVUsers.setAdapter(mCommonAdapter);

    }

    private void initListener() {
        // 打赏
        RxView.clicks(mBtRewards)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnRewardsClickListener != null) {
                        mOnRewardsClickListener.onRewardClick();
                    }
                    RewardFragment.startRewardActivity(getContext(), mRewardType, mSourceId);

                });
        // 打赏用户列表
        RxView.clicks(mIvRightArrow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    RewardListFragment.startRewardActivity(getContext(), mRewardType, mSourceId, mListData);
                });
    }

    /**
     * 更新打赏总量
     *
     * @param rewardsCountBean the total rewad data
     */
    public void updateRewardsCount(RewardsCountBean rewardsCountBean,String moneyName) {

        if (rewardsCountBean == null) {
            return;
        }
        if (TextUtils.isEmpty(rewardsCountBean.getAmount())) {
            rewardsCountBean.setAmount("0");
        }
        double amout = 0;
        try {
            amout = Double.parseDouble(rewardsCountBean.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String result = getResources().getString(R.string.reward_show, "<" + rewardsCountBean.getCount() + ">", "<" + getResources().getString(R.string.money_format, amout) + ">", moneyName);
        CharSequence charSequence = ColorPhrase.from(result).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.money))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        mTvRewardsTip.setText(charSequence);

    }

    /**
     * 更新打赏用户列表
     *
     * @param data user list for this rewad source
     */
    public void updateRewardsUser(List<RewardsListBean> data) {
        if (data == null) {
            return;
        }
        mListData.clear();
        if (data.size() > DEFAULT_SHOW_IMAGE_SZIE) {
            mIvRightArrow.setVisibility(VISIBLE);
            mListData.addAll(data.subList(0, DEFAULT_SHOW_IMAGE_SZIE - 1));
        } else {

            mListData.addAll(data);
            if (mListData.isEmpty()) {
                mIvRightArrow.setVisibility(GONE);
            } else {
                mIvRightArrow.setVisibility(VISIBLE);
            }
        }
        mCommonAdapter.notifyDataSetChanged();

    }

    /**
     * 更新资源 id
     *
     * @param sourceId source id for this reward
     */
    public void updateSourceId(long sourceId) {
        this.mSourceId = sourceId;
    }

    /**
     * 更新打赏类型
     *
     * @param rewardTyped
     */
    public void updateRewardType(RewardType rewardTyped) {
        this.mRewardType = rewardTyped;
    }

    /**
     * 初始化打赏数据
     *
     * @param sourceId         source id for this reward
     * @param listData         user list for this rewad source
     * @param rewardsCountBean the total rewad data
     */
    public void initData(long sourceId, List<RewardsListBean> listData, RewardsCountBean rewardsCountBean, RewardType rewardType,String moneyName) {
        updateSourceId(sourceId);
        updateRewardsUser(listData);
        updateRewardsCount(rewardsCountBean,moneyName);
        updateRewardType(rewardType);
    }

    public void setOnRewardsClickListener(OnRewardsClickListener onRewardsClickListener) {
        mOnRewardsClickListener = onRewardsClickListener;
    }

    public interface OnRewardsClickListener {

        void onRewardClick();
    }
}
