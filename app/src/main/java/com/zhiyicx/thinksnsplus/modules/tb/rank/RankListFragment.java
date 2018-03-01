package com.zhiyicx.thinksnsplus.modules.tb.rank;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListFragment extends TSListFragment<RankListContract.Presenter, RankData> implements RankListContract.View {


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tbranklist;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ranking);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<RankData>(mActivity, R.layout.item_tbrank, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, RankData rankData, int position) {

                // 设置头像
                UserAvatarView userAvatarView = holder.getView(R.id.iv_headpic);
                ImageUtils.loadCircleUserHeadPic(null, userAvatarView);

                // 排名
                holder.setText(R.id.tv_rank, "");

                // 用户名
                holder.setText(R.id.tv_name, "");

                // 好友数量
                holder.setText(R.id.tv_friends, "");

                // TBMark
                holder.setText(R.id.tv_tbmark, "");
            }
        };
        return adapter;
    }
}
