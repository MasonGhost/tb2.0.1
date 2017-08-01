package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletPresenter;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
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

public class ReWardsView extends FrameLayout {

    protected TextView mBtRewards;
    protected TextView mTvRewardsTip;
    protected RecyclerView mRVUsers;
    protected ImageView mIvRightArrow;

    private RecyclerView.LayoutManager mLayoutManager;
    private CommonAdapter mCommonAdapter;
    private List<RewardsListBean> mListData = new ArrayList<>();


    public ReWardsView(@NonNull Context context) {
        this(context, null);
    }

    public ReWardsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReWardsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
        mLayoutManager = new LinearLayoutManager(getContext());
        mRVUsers.setLayoutManager(mLayoutManager);
        mRVUsers.setHasFixedSize(true);
        mRVUsers.addItemDecoration(new LinearDecoration(0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small), getResources().getDimensionPixelOffset(R.dimen.spacing_small)));
        mCommonAdapter = new CommonAdapter<RewardsListBean>(getContext(), R.layout.item_rewards_user_image, mListData) {
            @Override
            protected void convert(ViewHolder holder, RewardsListBean rewardsListBean, int position) {

                ImageUtils.loadCircleUserHeadPic(rewardsListBean.getUser(), holder.getView(R.id.iv_head));

                RxView.clicks(holder.getView(R.id.iv_head))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(getContext(), rewardsListBean.getUser()));

            }
        };

    }

    private void initListener() {
        // 打赏
        RxView.clicks(mBtRewards)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> LogUtils.d("h rewards h"));
    }

    /**
     * 更新打赏总量
     *
     * @param rewardsCountBean
     */
    public void updateRewardsCount(RewardsCountBean rewardsCountBean) {

        if (rewardsCountBean == null) {
            return;
        }

        mTvRewardsTip.setText(getResources().getString(R.string.rewards_show, rewardsCountBean.getCount(), rewardsCountBean.getAmount()));

    }

    /**
     * 更新打赏用户列表
     * @param data
     */
    public void updateRewardsUser(List<RewardsListBean> data) {
        if (data == null) {
            return;
        }
        mListData.clear();
        mListData.addAll(data);
        mCommonAdapter.notifyDataSetChanged();
    }

}
