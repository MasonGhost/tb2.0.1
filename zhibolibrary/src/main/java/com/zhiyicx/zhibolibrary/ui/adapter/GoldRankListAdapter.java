package com.zhiyicx.zhibolibrary.ui.adapter;

import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;
import com.zhiyicx.zhibolibrary.ui.holder.GoldRankListHolder;

import java.util.List;

/**
 * Created by jungle on 2016/5/25.
 */
public class GoldRankListAdapter extends MoreLinearAdapter<SearchResult> {
    public GoldRankListAdapter(List<SearchResult> infos) {
        super(infos);
    }

    @Override
    public ZBLBaseHolder<SearchResult> getHolder(View v) {
        return new GoldRankListHolder(v);
    }

    @Override
    public int getLayoutId() {
        return R.layout.zb_recycle_item_rank_gold;
    }
}
