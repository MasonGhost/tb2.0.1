package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexAdapter extends CommonAdapter<RankIndexBean>{

    public RankIndexAdapter(Context context, List<RankIndexBean> datas) {
        super(context, R.layout.item_rank_index, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RankIndexBean rankIndexBean, int position) {
        holder.setText(R.id.tv_rank_type, rankIndexBean.getSubCategory());
        RecyclerView rvUsers = holder.getView(R.id.rv_users);
        rvUsers.setNestedScrollingEnabled(false);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 5);
        rvUsers.setLayoutManager(layoutManager);
        if (rankIndexBean.getUserInfoList().size() > 5){
            rvUsers.setAdapter(new RankIndexUserAdapter(mContext, rankIndexBean.getUserInfoList().subList(0, 5)));
        } else {
            rvUsers.setAdapter(new RankIndexUserAdapter(mContext, rankIndexBean.getUserInfoList()));
        }
    }
}
