package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Describe 打赏排行榜
 * @Author Jungle68
 * @Date 2017/1/
 * @Contact master.jungle68@gmail.com
 */
public class RewardListFragment extends TSListFragment<RewardListContract.Presenter, RewardsListBean> implements RewardListContract.View {
    private static final String BUNDLE_REWARD_TYPE = "reward_type";
    private static final String BUNDLE_SOURCE_ID = "source_id";
    private static final String BUNDLE_DATA = "data";

    private RewardType mRewardType;
    private long mSourceId;
    private List<RewardsListBean> mRewardsListBeen = new ArrayList<>();

    public static RewardListFragment newInstance(Bundle bundle) {
        RewardListFragment rankFragment = new RewardListFragment();
        rankFragment.setArguments(bundle);
        return rankFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null || getArguments().getLong(BUNDLE_SOURCE_ID) == 0) {
            throw new IllegalArgumentException("source_id  not be 0");
        }

        mRewardType = (RewardType) getArguments().getSerializable(BUNDLE_REWARD_TYPE);
        mSourceId = getArguments().getLong(BUNDLE_SOURCE_ID);
        if (getArguments().getSerializable(BUNDLE_DATA) != null) {
            mRewardsListBeen.addAll((Collection<? extends RewardsListBean>) getArguments().getSerializable(BUNDLE_DATA));
        }
    }

    @Override
    protected CommonAdapter<RewardsListBean> getAdapter() {
        return new RewardListAdapter(getContext(), R.layout.item_reward_user, mListDatas, mRewardType);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.reward_list);
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    public RewardType getCurrentType() {
        return mRewardType;
    }

    @Override
    public long getSourceId() {
        return mSourceId;
    }

    @Override
    public List<RewardsListBean> getCacheData() {
        return mRewardsListBeen;
    }

    /**
     * @param context    not application context clink
     * @param rewardType reward type {@link RewardType}
     */
    public static void startRewardActivity(Context context, RewardType rewardType, long sourceId, List<RewardsListBean> data) {

        Intent intent = new Intent(context, RewardListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REWARD_TYPE, rewardType);
        bundle.putSerializable(BUNDLE_SOURCE_ID, sourceId);
        bundle.putParcelableArrayList(BUNDLE_DATA, (ArrayList<? extends Parcelable>) data);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }

    @Override
    protected Long getMaxId(@NotNull List<RewardsListBean> data) {
        if (mListDatas.size() > 0) {
            return (long) mListDatas.size();
        }
        return DEFAULT_PAGE_MAX_ID;
    }
}
