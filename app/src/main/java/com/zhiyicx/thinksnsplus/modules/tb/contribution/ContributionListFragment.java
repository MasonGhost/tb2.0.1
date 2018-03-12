package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2018/02/28/14:31
 * @Email Jliuer@aliyun.com
 * @Description 贡献列表
 */
public class ContributionListFragment extends TSListFragment<ContributionListContract.Presenter, ContributionData>
        implements ContributionListContract.View {

    /**
     * 累计贡献
     */
    public static final String TYPE_TOTAL = "all";

    /**
     * 今日贡献
     */
    public static final String TYPE_TODAY = "day";

    /**
     * bundle key
     */
    public static final String TYPE = "type";

    @Inject
    ContributionListPresenter mContributionListPresenter;

    private String mType;

    public static ContributionListFragment newInstance(String type) {
        ContributionListFragment contributionListFragment = new ContributionListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        contributionListFragment.setArguments(bundle);
        return contributionListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerContributionListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .contributionListPresenterModule(new ContributionListPresenterModule(this))
                .build().inject(this);
        mType = getArguments().getString(TYPE);
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {

        CommonAdapter adapter = new CommonAdapter<ContributionData>(mActivity, R.layout.item_contribution, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, ContributionData contributionData, int position) {
                // 设置头像
                UserAvatarView userAvatarView = holder.getView(R.id.iv_headpic);
                if (contributionData.getInviter() != null) {
                    ImageUtils.loadCircleUserHeadPic(contributionData.getInviter(), userAvatarView);
                }
                TextView tvRank = holder.getView(R.id.tv_rank);
                // 排名
                holder.setText(R.id.tv_rank, String.valueOf(position + 1));

                // 用户名
                if (contributionData.getInviter() != null) {
                    holder.setText(R.id.tv_name, contributionData.getInviter().getName());
                }

                // TBMark
                holder.setText(R.id.tv_total, ConvertUtils.numberConvert(contributionData.getObtain()));
                switch (position + 1) {
                    case 1:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.rank1));
                        holder.setTextColor(R.id.tv_total, getColor(R.color.rank1));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);
                        break;
                    case 2:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.rank2));
                        holder.setTextColor(R.id.tv_total, getColor(R.color.rank2));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);

                        break;
                    case 3:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.rank3));
                        holder.setTextColor(R.id.tv_total, getColor(R.color.rank3));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.BOLD_ITALIC);

                        break;

                    default:
                        holder.setTextColor(R.id.tv_rank, getColor(R.color.themeColor));
                        holder.setTextColor(R.id.tv_total, getColor(R.color.important_for_content));
                        tvRank.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvRank.setTypeface(tvRank.getTypeface(), Typeface.NORMAL);

                }


            }
        };
        return adapter;

    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    protected Long getMaxId(@NotNull List<ContributionData> data) {
        return (long) mListDatas.size();
    }
}
