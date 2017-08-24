package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListActivity.BUNDLE_RANK_BEAN;

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        int width = mContext.getResources().getDimensionPixelOffset(R.dimen.spacing_tiny);
        rvUsers.addItemDecoration(new LinearDecoration(0, 0, 0, ConvertUtils.px2dp(mContext, width)));
        rvUsers.setLayoutManager(layoutManager);
        if (rankIndexBean.getUserInfoList() != null){
            if (rankIndexBean.getUserInfoList().size() > 5){
                rvUsers.setAdapter(new RankIndexUserAdapter(mContext, rankIndexBean.getUserInfoList().subList(0, 5)));
            } else {
                rvUsers.setAdapter(new RankIndexUserAdapter(mContext, rankIndexBean.getUserInfoList()));
            }
        }
        RxView.clicks(holder.getView(R.id.ll_info_container))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    // 跳转二级页面
                    Intent intent = new Intent(mContext, RankTypeListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_RANK_BEAN, rankIndexBean);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                });
    }
}
