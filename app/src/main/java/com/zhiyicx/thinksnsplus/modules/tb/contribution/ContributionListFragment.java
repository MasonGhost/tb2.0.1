package com.zhiyicx.thinksnsplus.modules.tb.contribution;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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
    public static final String TYPE_TOTAL = "total";

    /**
     * 今日贡献
     */
    public static final String TYPE_TODAY = "today";

    /**
     * bundle key
     */
    public static final String TYPE = "type";

    @Inject
    ContributionListPresenter mContributionListPresenter;

    public static ContributionListFragment newInstance(String type) {
        ContributionListFragment contributionListFragment = new ContributionListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        contributionListFragment.setArguments(bundle);
        return contributionListFragment;
    }

    @Override
    protected void initData() {
        DaggerContributionListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .contributionListPresenterModule(new ContributionListPresenterModule(this))
                .build().inject(this);
        super.initData();
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
    protected RecyclerView.Adapter getAdapter() {

        CommonAdapter adapter = new CommonAdapter<ContributionData>(mActivity, R.layout.item_contribution, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, ContributionData contributionData, int position) {
                // 设置头像
                UserAvatarView userAvatarView = holder.getView(R.id.iv_headpic);
                ImageUtils.loadCircleUserHeadPic(null, userAvatarView);

                // 排名
                holder.setText(R.id.tv_rank, "");

                // 用户名
                holder.setText(R.id.tv_name, "");

                // 总数？
                holder.setText(R.id.tv_total, "");
            }
        };
        return adapter;

    }
}
