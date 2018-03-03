package com.zhiyicx.thinksnsplus.modules.tb.rank;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.tb.contribution.ContributionData;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListFragment extends TSListFragment<RankListContract.Presenter, RankData> implements RankListContract.View {

    @BindView(R.id.tv_tbmark)
    TextView mTvTbMarkUnit;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tbranklist;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ranking);
    }

    @Override
    protected void initData() {
        super.initData();
        mTvTbMarkUnit.setText(mPresenter.getWalletGoldName());
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<RankData>(mActivity, R.layout.item_tbrank, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, RankData rankData, int position) {
                // 设置头像
                UserAvatarView userAvatarView = holder.getView(R.id.iv_headpic);
                ImageUtils.loadCircleUserHeadPic(rankData, userAvatarView);
                TextView tvRank = holder.getView(R.id.tv_rank);
                // 排名
                holder.setText(R.id.tv_rank, String.valueOf(rankData.getExtra().getRank()));

                // 用户名
                holder.setText(R.id.tv_name, rankData.getName());

                // 好友数量
                holder.setText(R.id.tv_friends, ConvertUtils.numberConvert(rankData.getExtra().getInvite_count()));

                // TBMark
                holder.setText(R.id.tv_tbmark, ConvertUtils.numberConvert(rankData.getExtra().getTb_mark_count()));
                switch (rankData.getExtra().getRank()) {
                    case 1:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.rank1));
                        holder.setTextColor(R.id.tv_friends, getColor(R.color.rank1));
                        holder.setTextColor(R.id.tv_tbmark, getColor(R.color.rank1));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);
                        break;
                    case 2:
                        holder.setTextColor(R.id.tv_rank,getColor( R.color.rank2));
                        holder.setTextColor(R.id.tv_friends, getColor(R.color.rank2));
                        holder.setTextColor(R.id.tv_tbmark, getColor(R.color.rank2));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);

                        break;
                    case 3:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.rank3));
                        holder.setTextColor(R.id.tv_friends,getColor( R.color.rank3));
                        holder.setTextColor(R.id.tv_tbmark, getColor(R.color.rank3));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);

                        break;

                    default:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.themeColor));
                        holder.setTextColor(R.id.tv_friends,getColor( R.color.important_for_content));
                        holder.setTextColor(R.id.tv_tbmark, getColor(R.color.important_for_content));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.NORMAL);

                }


            }
        };
        return adapter;
    }

    @Override
    protected Long getMaxId(@NotNull List<RankData> data) {
        return (long) mListDatas.size();
    }
}
